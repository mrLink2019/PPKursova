package com.pp.kursova.controller;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.pp.kursova.model.Coffee;
import com.pp.kursova.model.Van;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Storage implements Serializable {
    private ArrayList<Coffee> coffeeStorage;
    private ArrayList<Van> vansStorage;
    private static Storage instance = null;

    private Storage() {
        coffeeStorage = new ArrayList<Coffee>();
        vansStorage = new ArrayList<Van>();
    }

    public static synchronized Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public String[][] getCoffeeFromDb() {
        String resultArr[][] = new String[10][4];
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("SQLExpress");
        dataSource.setServerName("MON");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("CoffeeShop");
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)) {
                ResultSet resultSet = connection.prepareStatement(
                        "SELECT * FROM dbo.CoffeeInfo").executeQuery();
                int i = 0;
                while (resultSet.next()) {
                    resultArr[i][0] = resultSet.getString(2);
                    resultArr[i][1] = String.valueOf(resultSet.getInt(3));
                    resultArr[i][2] = String.valueOf(resultSet.getInt(4));
                    resultArr[i][3] = String.valueOf(resultSet.getInt(5));
                    i++;
                }
                return resultArr;
            } else {
                throw new SQLException("connection isn't valid");
            }
        } catch (SQLServerException SQLEx) {
            SQLEx.printStackTrace();
        } catch (SQLException SQLEx) {
            SQLEx.printStackTrace();
        }
        return null;
    }

    public void addVan(String name, int vanVolume) {
        vansStorage.add(new Van(name, vanVolume));
    }

    public boolean deleteVan (Van selectedVan) {
        if(!vansStorage.isEmpty()) {
            return vansStorage.remove(selectedVan);
        }
        return false;
    }

    public synchronized void addCoffee(Coffee newCoffee) {
        coffeeStorage.add(newCoffee);
    }

    public synchronized boolean deleteCoffee (Coffee selectedCoffee) {
        if(!coffeeStorage.isEmpty()) {
            return coffeeStorage.remove(selectedCoffee);
        }
        return false;
    }

    public void setVansStorage(ArrayList<Van> vansStorage) {
        this.vansStorage = vansStorage;
    }

    public ArrayList<Van> getVansStorage() {
        return new ArrayList<Van>(vansStorage);
    }

    public void setCoffeeStorage(ArrayList<Coffee> coffeeStorage) {
        this.coffeeStorage = coffeeStorage;
    }

    public ArrayList<Coffee> getCoffeeStorage() {
        return new ArrayList<Coffee>(coffeeStorage);
    }
}