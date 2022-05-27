package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    private BigDecimal getUnitaryTax() {
        return getPrice().divide(valueOf(100)).multiply(getCategory().getTaxPercentage()).setScale(2, HALF_UP);
    }

    private BigDecimal getUnitaryTaxedAmount() {
        return getPrice().add(getUnitaryTax()).setScale(2, HALF_UP);
    }

    public BigDecimal getTaxAmount(BigDecimal itemQuantity) {
        return getUnitaryTax().multiply(itemQuantity);
    }

    public BigDecimal getTotalTaxedAmount(BigDecimal itemQuantity) {
        return getUnitaryTaxedAmount().multiply(itemQuantity).setScale(2, HALF_UP);
    }
}
