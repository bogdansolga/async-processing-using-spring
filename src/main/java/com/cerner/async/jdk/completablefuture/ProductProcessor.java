package com.cerner.async.jdk.completablefuture;

import com.cerner.async.model.Section;
import com.cerner.async.model.Store;
import com.cerner.async.model.StoreSetup;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.IntStream;

class ProductProcessor {

    private static final Random RANDOM = new Random();

    private static final int MAX_SLEEP_TIME = 2000;

    CompletableFuture<Long> getProductsStock(final String productName) {
        return CompletableFuture.supplyAsync(() -> {
            displayStageAndThreadName("Getting the product stock for '" + productName + "'");
            sleepALittle();

            final Store store = StoreSetup.getDefaultStore();
            return store.getStoreSections()
                        .stream()
                        .map(Section::getProducts)
                        .flatMap(products -> products.orElse(new ArrayList<>())
                                                     .stream())
                        .filter(product -> product.getName().contains(productName))
                        .count();
        });
    }

    CompletableFuture<Long> getReserveStock(final String productName) {
        return CompletableFuture.supplyAsync(() -> {
            displayStageAndThreadName("Getting the reserve stock for '" + productName + "'");
            sleepALittle();

            return IntStream.of(RANDOM.nextInt(100))
                            .count();
        });
    }

    Function<Long, CompletableFuture<Double>> getProductsPrice() {
        return productsStock -> {
            displayStageAndThreadName("Getting the product price, for the stock " + productsStock);
            sleepALittle();

            //if (true) throw new RuntimeException("Shift happens");

            return CompletableFuture.supplyAsync(() -> productsStock * RANDOM.nextDouble() * 1000);
        };
    }

    Function<Double, CompletableFuture<String>> getDisplayedText() {
        return productsPrice -> {
            displayStageAndThreadName("Getting the displayed text");
            sleepALittle();

            return CompletableFuture.supplyAsync(() -> "The price of the products is " + productsPrice);
        };
    }

    // simulates a long running processing
    private void sleepALittle() {
        try {
            Thread.sleep(getRandomSleepDuration());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getRandomSleepDuration() {
        return RANDOM.nextInt(MAX_SLEEP_TIME);
    }

    private void displayStageAndThreadName(final String operationName) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + operationName + "...");
    }
}
