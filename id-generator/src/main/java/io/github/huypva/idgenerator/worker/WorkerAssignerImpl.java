package io.github.huypva.idgenerator.worker;

import io.github.huypva.idgenerator.worker.entity.WorkerEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huypva
 */
@Slf4j
public class WorkerAssignerImpl implements WorkerAssigner {

  DataSource dataSource;

  public WorkerAssignerImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public WorkerEntity assignWorker(long maxWorkerId) throws Exception {
    WorkerEntity newWorker = null;

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    LocalDate today = LocalDate.now();
    try {
      connection = this.dataSource.getConnection();
      connection.setAutoCommit(false);

      String selectSql = "SELECT worker_id, last_date FROM worker FOR UPDATE";
      preparedStatement = connection.prepareStatement(selectSql);
      resultSet = preparedStatement.executeQuery();
      WorkerEntity worker = null;
      if (resultSet.next()) {
        worker = new WorkerEntity();
        worker.setWorkerId(resultSet.getLong("worker_id"));
        //Do not using java.sql.Date to prevent Timezone issues for MySQL 8.0.14 and later, https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-known-issues-limitations.html
        worker.setLastDate(LocalDate.parse(resultSet.getString("last_date")));
      } else {
        throw new Exception("Table must not empty!");
      }

      if (today.compareTo(worker.getLastDate()) <= 0) {
        newWorker = new WorkerEntity();
        if (worker.getWorkerId() + 1 <= maxWorkerId) {
          newWorker.setLastDate(worker.getLastDate());
          newWorker.setWorkerId(worker.getWorkerId() + 1);
        } else {
          newWorker.setLastDate(worker.getLastDate().plusDays(1));
          newWorker.setWorkerId(0);
        }
      } else {
        newWorker = new WorkerEntity();
        newWorker.setLastDate(today);
        newWorker.setWorkerId(0);
      }

      String updateSql = "UPDATE worker SET last_date = ?, worker_id = ? WHERE last_date = ? AND worker_id = ?";
      preparedStatement = connection.prepareStatement(updateSql);

      //Do not using java.sql.Date to prevent Timezone issues for MySQL 8.0.14 and later, https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-known-issues-limitations.html
      preparedStatement.setString(1, newWorker.getLastDate().toString());
      preparedStatement.setLong(2, newWorker.getWorkerId());
      preparedStatement.setString(3, worker.getLastDate().toString());
      preparedStatement.setLong(4, worker.getWorkerId());

      int effectRow = preparedStatement.executeUpdate();
      if (effectRow < 1) {
        throw new Exception("Can't update database!");
      }

      connection.commit();
      return newWorker;
    } finally{
      if (resultSet != null) {
        resultSet.close();
      }

      if (connection != null) {
        connection.close();
      }

      if (preparedStatement != null) {
        preparedStatement.close();
      }
    }
  }

}
