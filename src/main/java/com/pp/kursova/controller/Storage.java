package com.pp.kursova.controller;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.pp.kursova.model.*;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class Storage implements Serializable {
    private static Storage instance = null;
    private static int coffeeInVanId = 1;
    private Storage() {
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
        dataSource.setDatabaseName("Coffee_Shop");
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

    public int addCoffeeToVan(Van selectedVan, Coffee coffee) {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("SQLExpress");
        dataSource.setServerName("MON");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("Coffee_shop");
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)) {

                String insertQuery = "INSERT INTO dbo.CoffeeInVanStorage VALUES (?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(insertQuery);
                statement.setInt(1, coffeeInVanId);
                statement.setInt(2, selectedVan.getVanId());
                statement.setInt(3, coffee.getCoffeeId());
                int result = statement.executeUpdate();
                coffeeInVanId++;
                return result;
            } else {
                throw new SQLException("connection isn't valid");
            }
        } catch (SQLServerException SQLEx) {
            SQLEx.printStackTrace();
        } catch (SQLException SQLEx) {
            SQLEx.printStackTrace();
        }
        return 0;
    }

    public int addVan(String name, int vanVolume) {
        Van newVan = new Van(name, vanVolume);
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("SQLExpress");
        dataSource.setServerName("MON");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("Coffee_shop");
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)) {
                String query = "INSERT INTO dbo.VansStorage VALUES (?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, newVan.getVanId());
                statement.setString(2, newVan.getName());
                statement.setInt(3, newVan.getVanVolume());
                statement.setInt(4, newVan.getCargoPrice());
                statement.setDouble(5, newVan.getCargoVolume());
                int result = statement.executeUpdate();
                return result;
            } else {
                throw new SQLException("connection isn't valid");
            }
        } catch (SQLServerException SQLEx) {
            SQLEx.printStackTrace();
        } catch (SQLException SQLEx) {
            SQLEx.printStackTrace();
        }
        return 0;
    }

    public int addVan(Van newVan) {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("SQLExpress");
        dataSource.setServerName("MON");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("Coffee_shop");
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)) {
                String query = "INSERT INTO dbo.VansStorage VALUES (?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, newVan.getVanId());
                statement.setString(2, newVan.getName());
                statement.setInt(3, newVan.getVanVolume());
                statement.setInt(4, newVan.getCargoPrice());
                statement.setDouble(5, newVan.getCargoVolume());
                int result = statement.executeUpdate();
                return result;
            } else {
                throw new SQLException("connection isn't valid");
            }
        } catch (SQLServerException SQLEx) {
            SQLEx.printStackTrace();
        } catch (SQLException SQLEx) {
            SQLEx.printStackTrace();
        }
        return 0;
    }

    public int deleteVan (Van selectedVan) {
        if(!getVansStorage().isEmpty()) {
            SQLServerDataSource dataSource = new SQLServerDataSource();
            dataSource.setUser("admin");
            dataSource.setPassword("SQLExpress");
            dataSource.setServerName("MON");
            dataSource.setPortNumber(1433);
            dataSource.setDatabaseName("Coffee_shop");
            try (Connection connection = dataSource.getConnection()){
                if(connection.isValid(1)) {
                    String query = "DELETE FROM dbo.VansStorage WHERE VanId = ?;";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, selectedVan.getVanId());
                    int result = statement.executeUpdate();
                    return result;
                } else {
                    throw new SQLException("connection isn't valid");
                }
            } catch (SQLServerException SQLEx) {
                SQLEx.printStackTrace();
            } catch (SQLException SQLEx) {
                SQLEx.printStackTrace();
            }
        }
        return 0;
    }

    public int addCoffee(Coffee newCoffee) {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("SQLExpress");
        dataSource.setServerName("MON");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("Coffee_shop");
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)) {
                String query = "INSERT INTO dbo.CoffeeStorage VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, newCoffee.getCoffeeId());
                statement.setString(2, newCoffee.getCoffeeType());
                statement.setInt(3, newCoffee.getSortsCount());
                statement.setInt(4, newCoffee.getPriceForKg());
                statement.setInt(5, newCoffee.getSort());
                statement.setDouble(6, newCoffee.getPackVolume());
                statement.setInt(7, newCoffee.getFullPrice());
                statement.setDouble(8, newCoffee.getFullVolume());
                int result = statement.executeUpdate();
                return result;
            } else {
                throw new SQLException("connection isn't valid");
            }
        } catch (SQLServerException SQLEx) {
            SQLEx.printStackTrace();
        } catch (SQLException SQLEx) {
            SQLEx.printStackTrace();
        }
        return 0;
    }

    public int deleteCoffee (Coffee selectedCoffee) {
        if(!getCoffeeStorage().isEmpty()) {
            SQLServerDataSource dataSource = new SQLServerDataSource();
            dataSource.setUser("admin");
            dataSource.setPassword("SQLExpress");
            dataSource.setServerName("MON");
            dataSource.setPortNumber(1433);
            dataSource.setDatabaseName("Coffee_shop");
            try (Connection connection = dataSource.getConnection()) {
                if (connection.isValid(1)) {
                    String query = "DELETE FROM dbo.CoffeeStorage WHERE CoffeeId = ?;";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, selectedCoffee.getCoffeeId());
                    int result = statement.executeUpdate();
                    return result;
                } else {
                    throw new SQLException("connection isn't valid");
                }
            } catch (SQLServerException SQLEx) {
                SQLEx.printStackTrace();
            } catch (SQLException SQLEx) {
                SQLEx.printStackTrace();
            }
        }
        return 0;
    }

    public void setVansStorage(ArrayList<Van> vansStorage) {
        vansStorage.forEach(van -> addVan(van));
    }

    public ArrayList<Van> getVansStorage() {
        ArrayList<Van> vansList = new ArrayList<>();
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("SQLExpress");
        dataSource.setServerName("MON");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("Coffee_shop");
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)) {
                ResultSet resultSet = connection.prepareStatement(
                        "SELECT * FROM dbo.VansStorage").executeQuery();
                int i = 0;
                while (resultSet.next()) {
                    Van van = new Van(resultSet.getString(2), resultSet.getInt(3));
                    van.setVanId(resultSet.getInt(1));
                    van.setCargoPrice(resultSet.getInt(4));
                    van.setCargoVolume(resultSet.getInt(5));
                    vansList.add(van);
                    i++;
                }
            } else {
                throw new SQLException("connection isn't valid");
            }
        } catch (SQLServerException SQLEx) {
            SQLEx.printStackTrace();
        } catch (SQLException SQLEx) {
            SQLEx.printStackTrace();
        }
        return vansList;
    }

    public void setCoffeeStorage(ArrayList<Coffee> coffeeStorage) {
        coffeeStorage.forEach(coffee -> addCoffee(coffee));
    }

    public ArrayList<Coffee> getCoffeeStorage() {
        ArrayList<Coffee> coffeeList = new ArrayList<>();
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("SQLExpress");
        dataSource.setServerName("MON");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("Coffee_shop");
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)) {
                ResultSet resultSet = connection.prepareStatement(
                        "SELECT * FROM dbo.CoffeeStorage").executeQuery();
                int i = 0;
                while (resultSet.next()) {
                    Coffee coffee;
                    switch (resultSet.getString(2)) {
                        case "Р—РµСЂРЅРѕРІР° РєР°РІР°":
                            coffee = new BeanCoffee(resultSet.getString(2),
                                    resultSet.getInt(4),
                                    resultSet.getInt(3),
                                    resultSet.getDouble(6));
                            coffee.setCoffeeId(resultSet.getInt(1));
                            coffee.setSort(resultSet.getInt(5));
                            coffee.setFullPrice(resultSet.getInt(7));
                            coffee.setFullVolume(resultSet.getDouble(8));
                            break;
                        case "РњРµР»РµРЅР° РєР°РІР°":
                            coffee = new GroundCoffee(resultSet.getString(2),
                                    resultSet.getInt(4),
                                    resultSet.getInt(3),
                                    resultSet.getDouble(6));
                            coffee.setCoffeeId(resultSet.getInt(1));
                            coffee.setSort(resultSet.getInt(5));
                            coffee.setFullPrice(resultSet.getInt(7));
                            coffee.setFullVolume(resultSet.getDouble(8));
                            break;
                        case "Р РѕР·С‡РёРЅРЅР° РєР°РІР° РІ РїР°РєРµС‚РёРєР°С…":
                            coffee = new InstantBagCoffee(resultSet.getString(2),
                                    resultSet.getInt(4),
                                    resultSet.getInt(3),
                                    resultSet.getDouble(6));
                            coffee.setCoffeeId(resultSet.getInt(1));
                            coffee.setSort(resultSet.getInt(5));
                            coffee.setFullPrice(resultSet.getInt(7));
                            coffee.setFullVolume(resultSet.getDouble(8));
                            break;
                        case "Р РѕР·С‡РёРЅРЅР° РєР°РІР° РІ Р±Р°РЅРєР°С…":
                            coffee = new InstantJarCoffee(resultSet.getString(2),
                                    resultSet.getInt(4),
                                    resultSet.getInt(3),
                                    resultSet.getDouble(6));
                            coffee.setCoffeeId(resultSet.getInt(1));
                            coffee.setSort(resultSet.getInt(5));
                            coffee.setFullPrice(resultSet.getInt(7));
                            coffee.setFullVolume(resultSet.getDouble(8));
                            break;
                        default:
                            coffee = null;
                    }
                    coffeeList.add(coffee);
                    i++;
                }
            } else {
                throw new SQLException("connection isn't valid");
            }
        } catch (SQLServerException SQLEx) {
            SQLEx.printStackTrace();
        } catch (SQLException SQLEx) {
            SQLEx.printStackTrace();
        }
        return coffeeList;
    }
}