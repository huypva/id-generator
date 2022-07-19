package io.codebyexample.helloworld.dataprovider.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import io.codebyexample.helloworld.core.usecase.GreetUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author huypva
 */
@SpringBootTest
class RandomIdProviderTest {

  @Autowired
  RandomIdProvider randomIdProvider;

  @BeforeEach
  void setup() {
//    this.randomIdProvider = new RandomIdProvider();
  }

  @Test
  void genId() {
    assertThat(randomIdProvider).isNotNull();
    assertThat(randomIdProvider.genId()).isGreaterThan(Integer.MIN_VALUE);
    assertThat(randomIdProvider.genId()).isLessThan(Integer.MAX_VALUE);
  }
}