package io.github.huypva.idgenerator.config;

import io.github.huypva.idgenerator.config.entity.ConfigEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.sql.DataSource;

/**
 * @author huypva
 */
public class ConfigInitializerImpl implements ConfigInitializer {

  DataSource dataSource;

  public ConfigInitializerImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ConfigEntity initializeConfig() throws Exception {
    String selectSql = "SELECT date_bits, worker_id_bits, sequence_bits, epoch_date FROM config";
    try (Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      if (resultSet.next()) {
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setDateBits(resultSet.getInt("date_bits"));
        configEntity.setWorkerIdBits(resultSet.getInt("worker_id_bits"));
        configEntity.setSequenceBits(resultSet.getInt("sequence_bits"));
        // Don't using java.sql.Date to prevent Timezone issues for MySQL 8.0.14 and later,
        // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-known-issues-limitations.html
        configEntity.setEpochDate(LocalDate.parse(resultSet.getString("epoch_date")));
        return configEntity;
      } else {
        throw new Exception("Table `config` must not empty!");
      }
    }
  }
}
