package io.codebyexample.helloworld.dataprovider.id;

import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * @author huypva
 */
@Component
public class RandomIdProvider implements IdProvider {

  private Random random;

  public RandomIdProvider () {
    random = new Random();
  }

  @Override
  public int genId() {
    return random.nextInt();
  }
}
