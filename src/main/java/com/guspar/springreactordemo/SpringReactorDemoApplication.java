package com.guspar.springreactordemo;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner {

    private static List<String> dishes = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(SpringReactorDemoApplication.class);

    @Override
    public void run(String... args) throws Exception {
        dishes.add("Cuy con papas");
        dishes.add("Arroz con poloo");
        //m1doNext();
        // m2map();

        // m3flatMap();
        // m4range();
        //m5dElayElements();
        //m6zipWhith();
        // m7Mergue();
        //m8filter();
        //m9takeLast();
        //m10takefirts();
        //m11DefaulIfEmpty();
        // m12onErrorMap();
        m13retry();
    }

    public void m13retry() throws InterruptedException {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.delayElements(Duration.ofSeconds(1)).doOnNext(x -> {
                    log.info("Conectando");
                    throw new ArithmeticException("BAD NUMBER");
                }).retry(3).onErrorReturn("ERrorrPlace reinicie")
                .subscribe(log::error);
        Thread.sleep(210000);
    }

    public void m12onErrorMap() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.doOnNext(x -> {
            throw new ArithmeticException("BAD NUMBER");
        }).onErrorReturn("Errorpleasereboot").subscribe(log::info);

        // .onErrorMap(ex -> new Exception(ex.getMessage())).subscribe(log::info);
    }

    public void m11DefaulIfEmpty() {
        dishes = new ArrayList<>();
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.map(x -> "P2: " + x).defaultIfEmpty("Lista Vacia").subscribe(log::info);
    }

    public void m10takefirts() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.take(1).subscribe(log::info);
    }

    public void m9takeLast() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.takeLast(1).subscribe(log::info);
    }

    public void m8filter() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.filter(x -> x.startsWith("C")).subscribe(log::info);
    }

    public void m7Mergue() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        List<String> clientes = new ArrayList<>();
        clientes.add("cliente1");
        clientes.add("cliente2");
        Flux<String> fx2 = Flux.fromIterable(clientes);

        Flux.merge(fx1, fx2).subscribe(log::info);

    }

    public void m6zipWhith() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        List<String> clientes = new ArrayList<>();
        clientes.add("cliente1");
        clientes.add("cliente2");
        Flux<String> fx2 = Flux.fromIterable(clientes);

        fx1.zipWith(fx2, (d, c) -> d + "-" + c).subscribe(log::info);
    }

    public void m5dElayElements() throws InterruptedException {
        Flux.range(0, 10).delayElements(Duration.ofSeconds(2))
                .doOnNext(x -> log.info(x.toString())).subscribe();

        Thread.sleep(21000);
    }

    public void m4range() {
        Flux<Integer> fx1 = Flux.range(0, 10);
        fx1.map(x -> x + 1).subscribe(x -> log.info(x.toString()));
    }

    public void m3flatMap() {
        Mono.just("Gustavo").map(x -> 35).subscribe(e -> log.info("Data " + e));

        Mono.just("Gustavo").map(x -> Mono.just(32)).subscribe(e -> log.info("Data " + e.subscribe(u -> log.info("Data " + u))));
        // Mono.just("Gustavo").flatMap(x -> Mono.just(32)).subscribe(e -> log.info("Data " + e));
    }

    public void m2map() {
        Flux<String> fx1 = Flux.fromIterable(dishes);
        fx1.map(String::toUpperCase).subscribe(log::info);
    }

    public void m1doNext() {
        //para saber que hace cada valor
        Flux<String> fx = Flux.fromIterable(dishes);
        fx.doOnNext(f -> log.info(f)).subscribe();
    }

    public void createMono() {
        Mono<String> m1 = Mono.just("Hola mundo reactivo");
        m1.subscribe(p -> log.info("REACTORS   " + p));
    }

    public void createFlux() {
        Flux<String> f = Flux.fromIterable(dishes);
        // f.subscribe(flu -> log.info("Plato " + flu));
        f.collectList().subscribe(l -> log.info("Plato" + l));
    }
}
