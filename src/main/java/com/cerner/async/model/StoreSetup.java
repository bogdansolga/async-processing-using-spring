package com.cerner.async.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class StoreSetup {

    private static Store defaultStore;

    static {
        final Section tabletsSection = new Section(1, StoreSection.Tablets, buildDefaultTablets());
        final Section monitorsSection = new Section(2, StoreSection.Monitors, buildDefaultMonitors());
        final Section laptopsSection = new Section(3, StoreSection.Laptops, buildDefaultLaptops());

        defaultStore = new Store(1, "Goodies", "Over there",
                                 new HashSet<>(Arrays.asList(tabletsSection, monitorsSection, laptopsSection)));
    }

    public static Store getDefaultStore() {
        return defaultStore;
    }

    private StoreSetup() {}

    private static List<Product> buildDefaultTablets() {
        return Arrays.asList(
                new Product(1, "Google Nexus 7 2013", 200),
                new Product(2, "Apple iPad Pro 9.7", 300),
                new Product(3, "Samsung Galaxy Tab S2", 350),
                new Product(4, "Microsoft Surface Pro 4", 400)
        );
    }

    private static List<Product> buildDefaultMonitors() {
        return Arrays.asList(
                new Product(5, "Samsung CF791", 500),
                new Product(6, "LG 32UD99", 550),
                new Product(7, "Samsung CH711", 600)
        );
    }

    private static List<Product> buildDefaultLaptops() {
        return Arrays.asList(
                new Product(10, "Lenovo X11", 1500),
                new Product(11, "Apple MacBook Pro", 2000)
        );
    }
}
