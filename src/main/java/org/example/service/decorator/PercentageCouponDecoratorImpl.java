package org.example.service.decorator;

import org.example.model.dto.ShoppingCartRequest;
import org.example.service.product.CartProduct;
import org.example.support.ProductProcessingResult;
import org.example.util.ProductPaymentUtil;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PercentageCouponDecoratorImpl implements CouponDecorator{
    private final double discountPercentage = 10.0;
    private final double thresholdAmount = 1000.0;

    @Override
    public ProductProcessingResult process(ProductProcessingResult input) {
        HashMap<Integer,CartProduct> discountedCartItems = new HashMap<>();

        for(var entry : input.getCartProductsList().entrySet()) {
            var productId = entry.getKey();
            var cartProduct = entry.getValue();
            var product = cartProduct.getProduct();

            double itemTotal = product.getOriginalPrice()*cartProduct.getQuantity();
            if(itemTotal>1000){
                var discounted = ProductPaymentUtil.applyDiscountToProduct(product , discountPercentage);
                cartProduct = cartProduct.toBuilder().product(discounted).build();
            }
            discountedCartItems.put(productId, cartProduct);
        }

        return ProductProcessingResult.builder()
                .cartProductsList(discountedCartItems)
                .build();
    }

    @Override
    public boolean isApplicable(ShoppingCartRequest request) {
         return request.getCartProductLists().values()
                 .stream()
                 .anyMatch(cartProduct -> {
                     double itemTotal = cartProduct.getProduct().getOriginalPrice()*cartProduct.getQuantity();
                     return itemTotal>thresholdAmount;
                 });
    }
}
