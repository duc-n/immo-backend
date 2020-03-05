package com.cele.immo.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {
    @Test
    public void thenMany() {
        StepVerifier.create(Flux.just(1, 2, 3).thenMany(Flux.just("test", "test2")))
                .expectNext("test", "test2")
                .verifyComplete();
    }

    @Test
    public void testFluxString() throws InterruptedException {
        Flux<String> source = Flux.just("John", "Monica", "Mark", "Cloe", "Frank", "Casper", "Olivia", "Emily", "Cate")
                .filter(name -> name.length() == 4)
                .map(String::toUpperCase);

        StepVerifier
                .create(source)
                .expectNext("JOHN")
                .expectNextMatches(name -> name.startsWith("MA"))
                .expectNext("CLOE", "CATE")
                .expectComplete()
                .verify();
    }
}
