package com.vendoza.service;

import com.vendoza.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductUpdateNotifier {

    private static ProductUpdateNotifier instance;
    private List<ProductUpdateListener> listeners = new ArrayList<>();

    private ProductUpdateNotifier() {}

    public static ProductUpdateNotifier getInstance() {
        if (instance == null) {
            instance = new ProductUpdateNotifier();
        }
        return instance;
    }

    public void addListener(ProductUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ProductUpdateListener listener) {
        listeners.remove(listener);
    }

    public void notifyProductUpdated(Product product) {
        for (ProductUpdateListener listener : listeners) {
            listener.onProductUpdated(product);
        }
    }

    public interface ProductUpdateListener {
        void onProductUpdated(Product product);
    }
}