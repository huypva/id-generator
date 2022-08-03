package io.github.huypva.idgeneratorbenchmark;

import io.github.huypva.idgenerator.IdGenerator;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author huypva
 * */
@Slf4j
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Threads(1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@SpringBootApplication
public class SpringBootBenchmark {

	public static IdGenerator idGenerator;

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(SpringBootBenchmark.class.getName() + ".*")
				.shouldFailOnError(true)
				.shouldDoGC(true)
				.build();

		new Runner(opt).run();
	}

	@Setup(Level.Trial)
	public void initialize() {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootBenchmark.class, new String[]{});
		idGenerator = context.getBean(IdGenerator.class);
	}

	@Benchmark
	public long genId() {
		return  idGenerator.genId();
	}

	@Benchmark
	@Threads(2)
	public long twoThreadGenId() {
		return  idGenerator.genId();
	}

	@Benchmark
	@Threads(4)
	public long fourThreadGenId() {
		return  idGenerator.genId();
	}

	@Benchmark
	@Threads(16)
	public long sixteenThreadGenId() {
		return  idGenerator.genId();
	}
}
