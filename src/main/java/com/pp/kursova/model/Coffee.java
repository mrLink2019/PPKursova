package com.pp.kursova.model;

import java.io.Serializable;

public abstract class Coffee implements Serializable {
    private final String coffeeType;
    private final int sortsCount;
    private final double packVolume;
    private int sort = 1, priceForL, fullPrice = 0;
    private double fullVolume = 0;

    Coffee (String coffeeType, int priceForL, int sortsCount, double packVolume) {
        this.coffeeType = coffeeType;
        this.priceForL = priceForL;
        this.sortsCount = sortsCount;
        this.packVolume = packVolume;
    }

    public abstract int calculateSortPrice(int sort);

    public void packCoffee() {
        this.fullVolume += packVolume;
    }

    public void calculateFullVolume() {
        this.fullVolume = Math.round(((double) this.fullPrice / calculateSortPrice(this.sort)) * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return coffeeType + " " + sort + " сорту, ціна за 1л " + calculateSortPrice(sort) +
                ", загальна ціна " + fullPrice + ", об'єм " + (fullVolume* 100.00) / 100.00 + "л";
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    public int getSortsCount() {
        return sortsCount;
    }

    public boolean setSort(int sort) {
        if(sort > sortsCount) {
            return false;
        } else {
            this.sort = sort;
            return true;
        }
    }

    public int getSort() {
        return sort;
    }

    public int getPriceForL() {
        return priceForL;
    }

    public void setFullPrice(int fullPrice) {
        this.fullPrice = fullPrice;
    }

    public int getFullPrice() {
        return fullPrice;
    }

    public void setFullVolume(double fullVolume) {
        this.fullVolume = fullVolume;
    }

    public double getFullVolume() {
        return fullVolume;
    }
}