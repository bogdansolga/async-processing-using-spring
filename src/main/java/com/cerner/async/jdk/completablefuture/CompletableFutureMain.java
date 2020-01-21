package com.cerner.async.jdk.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * A few {@link CompletableFuture} usage samples
 */
public class CompletableFutureMain {

    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS / 4);

    public static void main(String[] args) {
        simpleProductsOperations();

        shutdownExecutor();
    }

    private static void simpleProductsOperations() {
        final ProductProcessor productProcessor = new ProductProcessor();

        final CompletableFuture<Long> getProductsStock = productProcessor.getProductsStock("iPad");
        final Function<Long, CompletableFuture<Double>> getProductsPrice = productProcessor.getProductsPrice();
        final Function<Double, CompletableFuture<String>> getProductsDisplayText = productProcessor.getDisplayedText();

        /*
            The three processing stages:
                - 1) get products stock
                - 2) get products price, for the resulted stock
                - 3) get the displayed text, for the products price and stock
        */
        final String displayedText = getProductsStock.thenComposeAsync(getProductsPrice)
                                                     .thenComposeAsync(getProductsDisplayText)
                                                     .exceptionally(Throwable::getMessage)
                                                     .join();
        System.out.println("Got the text '" + displayedText + "'");

        shutdownExecutor();
    }

    private static void notifyFinishedTasks() {
        System.out.println(Thread.currentThread().getName() + " - All good");
    }

    private static void shutdownExecutor() {
        ((ExecutorService) EXECUTOR).shutdown();
        System.out.println("The executor was properly shutdown");
    }
}