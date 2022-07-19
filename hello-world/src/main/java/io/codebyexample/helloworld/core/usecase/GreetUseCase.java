package io.codebyexample.helloworld.core.usecase;

import io.codebyexample.helloworld.core.entity.Greeting;

/**
 * @author huypva
 */
public interface GreetUseCase {

  Greeting greet(String name);
}
