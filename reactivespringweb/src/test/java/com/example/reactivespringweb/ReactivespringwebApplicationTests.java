package com.example.reactivespringweb;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class ReactivespringwebApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void simpleFluxExample() {
		Flux<String> fluxColors = Flux.just("red", "green", "blue");
		fluxColors.log().subscribe(System.out::println);
	}

	@Test
	public void backpressureExample() {
		Flux.range(1,6)
				.subscribe(new Subscriber<>() {
					private Subscription s;
					int counter;

					@Override
					public void onSubscribe(Subscription s) {
						System.out.println("onSubscribe");
						this.s = s;
						System.out.println("Requesting 2 emissions");
						s.request(2);
					}

					@Override
					public void onNext(Integer i) {
						System.out.println("onNext " + i);
						counter++;
						if (counter == 5)
							s.cancel();
						if (counter % 2 == 0) {
							System.out.println("Requesting 2 emissions");
							s.request(2);
						}
					}

					@Override
					public void onError(Throwable t) {
						System.err.println("onError");
					}

					@Override
					public void onComplete() {
						System.out.println("onComplete");
					}
				});
	}
}
