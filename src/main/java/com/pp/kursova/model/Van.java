package com.pp.kursova.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Van implements Serializable {
    private static int id = 1;
    private int vanId;
    private String name;
    private ArrayList<Coffee> cargo;
    private int cargoPrice = 0, vanVolume;
    private double cargoVolume = 0;

    public Van(String name, int vanVolume) {
        this.name = name;
        this.vanVolume = vanVolume;
        this.vanId = id;
        id++;
        cargo = new ArrayList<Coffee>();
    }

    private void calculateCargoPrice() {
        this.cargoPrice = 0;
        this.cargo.forEach((item -> {this.cargoPrice += item.getFullPrice();}));
    }

    private void calculateCargoVolume() {
        this.cargoVolume = 0;
        this.cargo.forEach((item -> {this.cargoVolume += item.getFullVolume();}));
    }

    public boolean addCargo(Coffee cargo) {
        if(hasVolumeForCargo(cargo)) {
            this.cargo.add(cargo);
            this.calculateCargoPrice();
            this.calculateCargoVolume();
            return true;
        } else {
            return false;
        }
    }

    public boolean hasVolumeForCargo(Coffee cargo) {
        if(this.cargoVolume + cargo.getFullVolume() <= this.vanVolume) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteCargo(Coffee selectedCargo) {
        if(!this.cargo.isEmpty()) {
            boolean isDone = this.cargo.remove(selectedCargo);
            this.calculateCargoPrice();
            this.calculateCargoVolume();
            return isDone;
        } else {
            return false;
        }
    }

    public void sortCargo() {
        Comparator<Coffee> sortByFullPriceUp = Comparator.comparing(Coffee::getFullPrice);
        Collections.sort(cargo, sortByFullPriceUp.reversed());
    }

    public ArrayList<Coffee> findCargo(int minSort, int maxSort) {
        ArrayList<Coffee> result = new ArrayList<Coffee>();
        if(!this.cargo.isEmpty()) {
            this.cargo.forEach((item -> {
                if(item.getSort() >= minSort && item.getSort() <= maxSort) {
                    result.add(item);
                }
            }));
        }
        return result;
    }

    @Override
    public String toString() {
        return "ім'я: " + this.name + "; об'єм: " + this.vanVolume + ";";
    }

    public int getVanId() {
        return vanId;
    }

    public void setVanId(int vanId) {
        this.vanId = vanId;
    }

    public String getName() {
        return new String(name);
    }

    public ArrayList<Coffee> getCargo() {
        return new ArrayList<Coffee>(cargo);
    }

    public int getCargoPrice() {
        return cargoPrice;
    }

    public void setCargoPrice(int cargoPrice) {
        this.cargoPrice = cargoPrice;
    }

    public double getCargoVolume() {
        return cargoVolume;
    }

    public void setCargoVolume(int cargoVolume) {
        this.cargoVolume = cargoVolume;
    }

    public int getVanVolume() {
        return vanVolume;
    }
}