package com.pp.kursova.model;

public class InstantJarCoffee extends Coffee {
    public InstantJarCoffee(String coffeeType, int priceForL, int sortsCount, double packVolume) {
        super(coffeeType, priceForL, sortsCount, packVolume);
    }

    @Override
    public int calculateSortPrice(int sort) {
        return super.getPriceForKg() / sort;
    }
}