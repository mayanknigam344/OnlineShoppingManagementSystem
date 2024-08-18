package org.example.service.product.items;

import org.example.service.product.Product;
import org.example.service.product.ProductType;

public class Item1 extends Product {

    double originalPrice;

     public Item1(String name, ProductType productType, double originalPrice) {
         super(name,productType);
         this.originalPrice = originalPrice;
    }

    @Override
    public double getPrice() {
        return originalPrice;
    }
}
