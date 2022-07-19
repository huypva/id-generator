package io.codebyexample.helloworld.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import io.codebyexample.helloworld.core.entity.Greeting;
import io.codebyexample.helloworld.dataprovider.id.RandomIdProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author huypva
 */
@ExtendWith(SpringExtension.class)
@Import(GreetUseCaseImpl.class)
class GreetUseCaseImplTest {

  @Autowired
  GreetUseCaseImpl greetUseCase;

  @MockBean
  RandomIdProvider randomIdProvider;

  @Test
  void givenExistingGreetingWhenGreetThenReturnGreeting() {
    Greeting valueDefault = new Greeting(1, "Hello World!");
    given(randomIdProvider.genId()).willReturn(1);

    Greeting greeting = greetUseCase.greet("World");
    assertThat(valueDefault).usingRecursiveComparison().isEqualTo(greeting);
  }
}