package io.github.huypva.idgeneratorsample;

import io.github.huypva.idgenerator.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * @author huypva
 * */
@Slf4j
@SpringBootApplication
public class Application {

	@Autowired
	IdGenerator idGenerator;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@EventListener(ApplicationReadyEvent.class)
	public void ready() {
		long id = idGenerator.genId();
		log.info("Id: {}", id);
		log.info("Parse id: {}", idGenerator.parseId(id));
	}

}
