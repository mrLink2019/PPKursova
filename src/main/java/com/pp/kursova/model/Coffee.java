package com.pp.kursova.model;

import java.io.Serializable;

public abstract class Coffee implements Serializable {
    static int id = 1;
    private String coffeeType;
    private int sortsCount, priceForKg;
    private double packVolume;
    private int sort = 1, fullPrice = 0, coffeeId;
    private double fullVolume = 0;

    Coffee (String coffeeType, int priceForKg, int sortsCount, double packVolume) {
        this.coffeeType = coffeeType;
        this.priceForKg = priceForKg;
        this.sortsCount = sortsCount;
        this.packVolume = packVolume;
        this.coffeeId = id;
        id++;
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
        return coffeeType + " " + sort + " сорту, ціна за 1кг " + calculateSortPrice(sort) +
                ", загальна ціна " + fullPrice + ", об'єм " + (fullVolume * 100.00) / 100.00 + "л";
    }

    public int getCoffeeId() {
        return this.coffeeId;
    }

    public void setCoffeeId(int coffeeId) {
        this.coffeeId = coffeeId;
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

    public int getPriceForKg() {
        return priceForKg;
    }

    public double getPackVolume() {
        return this.packVolume;
    }

    public void setPackVolume(double packVolume) {
        this.packVolume = packVolume;
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