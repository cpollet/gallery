package net.cpollet.kozan.generator;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public final class Main {
    public static void main(String[] args) throws InterruptedException {
        Generator<Integer> randomIntegerGenerator = new RandomIntegerGenerator(0, 10);
        for (int i = 0; i < 10; i++) {
            log(randomIntegerGenerator, i);
        }

        Generator<Integer> constantGenerator = new ConstantGenerator<>(0);
        for (int i = 0; i < 10; i++) {
            log(constantGenerator, i);
        }

        Generator<Integer> loopingGenerator = new LoopingGenerator<>(1, 2);
        for (int i = 0; i < 10; i++) {
            log(loopingGenerator, i);
        }

        Generator<Integer> sequenceGenerator = new SequenceGenerator<>(0, v -> v + 1);
        for (int i = 0; i < 10; i++) {
            log(sequenceGenerator, i);
        }

        Generator<Integer> integerIncrementGenerator = new IntegerIncrementGenerator(0, 3);
        for (int i = 0; i < 10; i++) {
            log(integerIncrementGenerator, i);
        }

        Generator<Integer> supplierGenerator = new SupplierGenerator<>(() -> LocalDateTime.now().getNano());
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1L);
            log(supplierGenerator, i);
        }
    }

    private static void log(Generator<Integer> randomIntegerGenerator, int i) {
        log.info("{}: {}", i, randomIntegerGenerator.next());
    }
}
