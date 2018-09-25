package com.learncamel.springboot.domain;

import java.math.BigDecimal;

public class Item {

    // JSON Input message
    // {"transactionType":"add", "sku":"111", "itemDescription":"Apple IPhone", "itemPrice":"199"}

    private String transactionType;
    private String sku;
    private String itemDescription;
    private BigDecimal itemPrice;


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "transactionType='" + transactionType + '\'' +
                ", sku='" + sku + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemPrice=" + itemPrice +
                '}';
    }
}
