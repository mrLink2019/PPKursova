package com.pp.kursova.view;

import com.pp.kursova.controller.Storage;
import com.pp.kursova.model.*;
import com.pp.kursova.model.javafx.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {
    private Logger logger = Logger.getLogger("generalLogger");
    private Logger errorLogger = Logger.getLogger("errorLogger");
    private Van selectedVan;
    private Coffee selectedCoffee;
    private Storage storage;

    @FXML private Label resultLabel;
    @FXML private Label logLabel;
    @FXML private VBox layoutVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        storage = Storage.getInstance();
        resultLabel.setText("Виберіть дію:");
    }

    @FXML
    protected void viewVansInStorageMenuItem () {
        logger.info("viewVansInStorageMenuItm clicked");
        printVans(storage.getVansStorage());
        logLabel.setText("Переглянуто фургони на складі");
    }

    @FXML
    protected void viewCoffeeInStorageMenuItem () {
        logger.info("viewCoffeeInStorageMenuItm clicked");
        printCoffee(storage.getCoffeeStorage());
        logLabel.setText("Переглянуто каву на складі");
    }

    @FXML
    protected void viewCoffeeInVanMenuItem () {
        logger.info("viewCoffeeInVanMenuItm clicked");
        chooseVan(storage.getVansStorage());
        if(!storage.getVansStorage().isEmpty()) {
            Button chooseButton = new Button();
            chooseButton.setText("Вибрати виділений фургон");
            chooseButton.setStyle("-fx-font-size:18");
            chooseButton.setPrefWidth(300);
            chooseButton.setPrefHeight(40);
            chooseButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (selectedVan != null) {
                        if (!selectedVan.getCargo().isEmpty()) {
                            printCoffee(selectedVan.getCargo());
                            resultLabel.setText("Вміст фургону: " + selectedVan);
                        } else {
                            resultLabel.setText("Фургон порожній");
                            clearLayoutBox();
                        }
                    } else {
                        resultLabel.setText("Помилка! виберіть фургон");
                    }
                }
            });
            layoutVBox.getChildren().add(chooseButton);
            logLabel.setText("Переглянуто каву в фургоні");
        } else {
            resultLabel.setText("Немає доступних фургонів");
            clearLayoutBox();
        }
    }

    @FXML
    protected void addVanToStorageMenuItem () {
        logger.info("addVanToStorageMenuItm clicked");
        clearLayoutBox();
        layoutVBox.setPrefWidth(300);
        resultLabel.setText("Введіть дані: ");

        Label nameLabel = new Label();
        nameLabel.setPrefWidth(300);
        nameLabel.setPrefHeight(25);
        nameLabel.setStyle("-fx-font-size:18");
        nameLabel.setText("Введіть назву фургону:");

        TextField nameTextField = new TextField();
        nameTextField.setPrefWidth(300);
        nameTextField.setPrefHeight(25);
        nameTextField.setStyle("-fx-font-size:18");

        Label volumeLabel = new Label();
        volumeLabel.setPrefWidth(300);
        volumeLabel.setPrefHeight(25);
        volumeLabel.setStyle("-fx-font-size:18");
        volumeLabel.setText("Введіть об'єм:");

        TextField volumeTextField = new TextField();
        volumeTextField.setPrefWidth(300);
        volumeTextField.setPrefHeight(25);
        volumeTextField.setStyle("-fx-font-size:18");

        Button createButton = new Button();
        createButton.setText("Створити новий фургон");
        createButton.setStyle("-fx-font-size:18");
        createButton.setPrefWidth(300);
        createButton.setPrefHeight(40);
        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    String vanName = nameTextField.getText();
                    int vanVolume = Integer.parseInt(volumeTextField.getText());
                    if(!vanName.isBlank() && vanVolume > 0) {
                        if(checkDuplicateName(vanName)) {
                            storage.addVan(vanName, vanVolume);
                            clearLayoutBox();
                            resultLabel.setText("Виберіть дію:");
                            logLabel.setText("Успішно додано новий фургон до складу");
                            logger.info("added new van");
                        } else {
                            resultLabel.setText("Таке ім'я уже існує, виберіть нове: ");
                        }
                    } else {
                        resultLabel.setText("Помилка! неправильні дані");
                    }
                } catch (NumberFormatException nfe) {
                    resultLabel.setText("Помилка! неправильні дані");
                }
            }
        });
        layoutVBox.getChildren().addAll(nameLabel, nameTextField, volumeLabel, volumeTextField, createButton);
    }

    @FXML
    protected void addCoffeeToStorageMenuItem () {
        logger.info("addCoffeeToStorageMenuItm clicked");
        clearLayoutBox();
        resultLabel.setText("Виберіть вид кави: ");

        String coffeeResult[][] = storage.getCoffeeFromDb();
        ToggleGroup group = new ToggleGroup();
        RadioButton buttonArr[] = new RadioButton[20];
        int i = 0;
            while (coffeeResult[i][0] != null) {
                buttonArr[i] = new RadioButton(coffeeResult[i][0]);
                buttonArr[i].setToggleGroup(group);
                buttonArr[i].setId(String.valueOf(i));
                layoutVBox.getChildren().add(buttonArr[i]);
                i++;
            }
        buttonArr[0].setSelected(true);

        Button chooseButton = new Button();
        chooseButton.setText("Вибрати");
        chooseButton.setStyle("-fx-font-size:18");
        chooseButton.setPrefWidth(300);
        chooseButton.setPrefHeight(40);
        chooseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
                int coffeeId = Integer.parseInt(selectedButton.getId());
                switch (coffeeResult[coffeeId][0]) {
                    case "Зернова кава":
                        setSelectedCoffee(new BeanCoffee(coffeeResult[coffeeId][0],
                                Integer.parseInt(coffeeResult[coffeeId][1]),
                                Integer.parseInt(coffeeResult[coffeeId][2]),
                                Integer.parseInt(coffeeResult[coffeeId][3])));
                        break;
                    case "Мелена кава":
                        setSelectedCoffee(new GroundCoffee(coffeeResult[coffeeId][0],
                                Integer.parseInt(coffeeResult[coffeeId][1]),
                                Integer.parseInt(coffeeResult[coffeeId][2]),
                                Integer.parseInt(coffeeResult[coffeeId][3])));
                        break;
                    case "Розчинна кава в пакетиках":
                        setSelectedCoffee(new InstantBagCoffee(coffeeResult[coffeeId][0],
                                Integer.parseInt(coffeeResult[coffeeId][1]),
                                Integer.parseInt(coffeeResult[coffeeId][2]),
                                Integer.parseInt(coffeeResult[coffeeId][3])));
                        break;
                    case "Розчинна кава в банках":
                        setSelectedCoffee(new InstantJarCoffee(coffeeResult[coffeeId][0],
                                Integer.parseInt(coffeeResult[coffeeId][1]),
                                Integer.parseInt(coffeeResult[coffeeId][2]),
                                Integer.parseInt(coffeeResult[coffeeId][3])));
                        break;
                    default:
                        setSelectedCoffee(null);
                }
                chooseCoffeeSort(coffeeResult[coffeeId]);
            }
        });
        layoutVBox.getChildren().add(chooseButton);
    }

    @FXML
    protected void deleteVanFromStorageMenuItem () {
        logger.info("deleteVanFromStorageMenuItm clicked");
        chooseVan(storage.getVansStorage());
        if(!storage.getVansStorage().isEmpty()) {
            Button deleteButton = new Button();
            deleteButton.setText("Видалити виділений фургон");
            deleteButton.setStyle("-fx-font-size:18");
            deleteButton.setPrefWidth(300);
            deleteButton.setPrefHeight(40);
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    if(selectedVan != null) {
                        storage.deleteVan(selectedVan);
                        logger.info("van deleted");
                        clearLayoutBox();
                        resultLabel.setText("Виберіть дію:");
                    } else {
                        resultLabel.setText("Помилка! виберіть фургон");
                    }
                }
            });
            layoutVBox.getChildren().add(deleteButton);
            logLabel.setText("Видалено фургон зі складу");
        }
    }

    @FXML
    protected void deleteCoffeeFromStorageMenuItem () {
        logger.info("deleteCoffeeFromStorageMenuItm clicked");
        chooseCoffee(storage.getCoffeeStorage());
        if(!storage.getCoffeeStorage().isEmpty()) {
            Button deleteButton = new Button();
            deleteButton.setText("Видалити виділену каву");
            deleteButton.setStyle("-fx-font-size:18");
            deleteButton.setPrefWidth(300);
            deleteButton.setPrefHeight(40);
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    if(selectedCoffee != null) {
                        storage.deleteCoffee(selectedCoffee);
                        logger.info("coffee deleted");
                        clearLayoutBox();
                        resultLabel.setText("Виберіть дію:");
                    } else {
                        resultLabel.setText("Помилка! виберіть каву");
                    }
                }
            });
            layoutVBox.getChildren().add(deleteButton);
            logLabel.setText("Видалено каву зі складу");
        }
    }

    @FXML
    protected void transferCoffeeFromStorageToVanMenuItem () {
        logger.info("transferCoffeeFromStorageToVanMenuItm clicked");
        if(!storage.getCoffeeStorage().isEmpty()) {
            if (!storage.getVansStorage().isEmpty()) {
                chooseCoffee(storage.getCoffeeStorage());
                Button selectCoffeeButton = new Button();
                selectCoffeeButton.setText("Вибрати виділену каву");
                selectCoffeeButton.setStyle("-fx-font-size:18");
                selectCoffeeButton.setPrefWidth(300);
                selectCoffeeButton.setPrefHeight(40);
                selectCoffeeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        if(selectedCoffee != null ) {
                            chooseVan(storage.getVansStorage());
                            Button selectVanButton = new Button();
                            selectVanButton.setText("Вибрати виділений фургон");
                            selectVanButton.setStyle("-fx-font-size:18");
                            selectVanButton.setPrefWidth(300);
                            selectVanButton.setPrefHeight(40);
                            selectVanButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    if(selectedVan != null ) {
                                        storage.deleteCoffee(selectedCoffee);
                                        selectedVan.addCargo(selectedCoffee);
                                        resultLabel.setText("Виберіть дію:");
                                        logLabel.setText("Перенесено каву зі складу в фургон");
                                        logger.info("transferred Coffee From Storage To Van");
                                        clearLayoutBox();
                                    } else {
                                        resultLabel.setText("Помилка! виберіть фургон");
                                    }
                                }
                            });
                            layoutVBox.getChildren().add(selectVanButton);
                        } else {
                            resultLabel.setText("Помилка! виберіть каву");
                        }
                    }
                });
                layoutVBox.getChildren().add(selectCoffeeButton);
            } else {
                resultLabel.setText("Немає доступних фургонів");
                clearLayoutBox();
            }
        } else {
            resultLabel.setText("Немає доступної кави");
            clearLayoutBox();
        }
    }

    @FXML
    protected void transferCoffeeFromVanToStorageMenuItem () {
        logger.info("transferCoffeeFromVanToStorageMenuItm clicked");
        if(!storage.getVansStorage().isEmpty()) {
            chooseVan(storage.getVansStorage());
            Button selectVanButton = new Button();
            selectVanButton.setText("Вибрати виділений фургон");
            selectVanButton.setStyle("-fx-font-size:18");
            selectVanButton.setPrefWidth(300);
            selectVanButton.setPrefHeight(40);
            selectVanButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    if(selectedVan != null ) {
                        if(!selectedVan.getCargo().isEmpty()) {
                            chooseCoffee(selectedVan.getCargo());
                            Button selectCoffeeButton = new Button();
                            selectCoffeeButton.setText("Вибрати виділену каву");
                            selectCoffeeButton.setStyle("-fx-font-size:18");
                            selectCoffeeButton.setPrefWidth(300);
                            selectCoffeeButton.setPrefHeight(40);
                            selectCoffeeButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    if(selectedCoffee != null ) {
                                        selectedVan.deleteCargo(selectedCoffee);
                                        storage.addCoffee(selectedCoffee);
                                        clearLayoutBox();
                                        logger.info("transferred Coffee From Van To Storage");
                                        resultLabel.setText("Виберіть дію:");
                                    } else {
                                        resultLabel.setText("Помилка! виберіть каву");
                                    }
                                }
                            });
                            layoutVBox.getChildren().add(selectCoffeeButton);
                        } else {
                            resultLabel.setText("Немає кави у вибраному фургоні");
                            clearLayoutBox();
                        }

                    } else {
                        resultLabel.setText("Помилка! виберіть фургон");
                    }
                }
            });
            layoutVBox.getChildren().add(selectVanButton);
            logLabel.setText("Перенесено каву з фургону на склад");
        } else {
            resultLabel.setText("Немає доступних фургонів");
            clearLayoutBox();
        }
    }

    @FXML
    protected void sortCoffeeInVanMenuItem () {
        logger.info("sortCoffeeInVanMenuItm clicked");
        chooseVan(storage.getVansStorage());
        if(!storage.getVansStorage().isEmpty()) {
            Button chooseButton = new Button();
            chooseButton.setText("Вибрати виділений фургон");
            chooseButton.setStyle("-fx-font-size:18");
            chooseButton.setPrefWidth(300);
            chooseButton.setPrefHeight(40);
            chooseButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (selectedVan != null) {
                        if (!selectedVan.getCargo().isEmpty()) {
                            selectedVan.sortCargo();
                            printCoffee(selectedVan.getCargo());
                            logger.info("sorted coffee in van");
                            resultLabel.setText("Посортований вміст фургону: " + selectedVan);
                        } else {
                            resultLabel.setText("Фургон порожній");
                            clearLayoutBox();
                        }
                    } else {
                        resultLabel.setText("Помилка! виберіть фургон");
                    }
                }
            });
            layoutVBox.getChildren().add(chooseButton);
            logLabel.setText("Посортовано каву в фургоні");
        } else {
            resultLabel.setText("Немає доступних фургонів");
            clearLayoutBox();
        }
    }

    @FXML
    protected void findCoffeeInVanMenuItem () {
        logger.info("findCoffeeInVanMenuItm clicked");
        chooseVan(storage.getVansStorage());
        if(!storage.getVansStorage().isEmpty()) {
            Button chooseButton = new Button();
            chooseButton.setText("Вибрати виділений фургон");
            chooseButton.setStyle("-fx-font-size:18");
            chooseButton.setPrefWidth(300);
            chooseButton.setPrefHeight(40);
            chooseButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (selectedVan != null) {
                        if (!selectedVan.getCargo().isEmpty()) {
                            clearLayoutBox();
                            layoutVBox.setPrefWidth(300);
                            resultLabel.setText("Введіть дані: ");

                            Label minSortLabel = new Label();
                            minSortLabel.setPrefWidth(300);
                            minSortLabel.setPrefHeight(25);
                            minSortLabel.setStyle("-fx-font-size:18");
                            minSortLabel.setText("Введіть мінімальний сорт:");

                            TextField minSortTextField = new TextField();
                            minSortTextField.setPrefWidth(300);
                            minSortTextField.setPrefHeight(25);
                            minSortTextField.setStyle("-fx-font-size:18");

                            Label maxSortLabel = new Label();
                            maxSortLabel.setPrefWidth(300);
                            maxSortLabel.setPrefHeight(25);
                            maxSortLabel.setStyle("-fx-font-size:18");
                            maxSortLabel.setText("Введіть максимальний сорт:");

                            TextField maxSortTextField = new TextField();
                            maxSortTextField.setPrefWidth(300);
                            maxSortTextField.setPrefHeight(25);
                            maxSortTextField.setStyle("-fx-font-size:18");

                            Button searchButton = new Button();
                            searchButton.setText("Виконати пошук");
                            searchButton.setStyle("-fx-font-size:18");
                            searchButton.setPrefWidth(300);
                            searchButton.setPrefHeight(40);
                            searchButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    try {
                                        int minSort = Integer.parseInt(minSortTextField.getText());
                                        int maxSort = Integer.parseInt(maxSortTextField.getText());
                                        if(minSort > 0 && maxSort > minSort) {
                                            printCoffee(selectedVan.findCargo(minSort, maxSort));
                                            resultLabel.setText("Результи пошуку в фургоні: " + selectedVan);
                                            logLabel.setText("Виконано пошук кави в фургоні");
                                            logger.info("find coffee successfully executed");
                                        } else {
                                            resultLabel.setText("Помилка! неправильні дані");
                                        }
                                    } catch (NumberFormatException nfe) {
                                        resultLabel.setText("Помилка! неправильні дані");
                                    }
                                }
                            });
                            layoutVBox.getChildren().addAll(minSortLabel, minSortTextField,
                                    maxSortLabel, maxSortTextField, searchButton);
                        } else {
                            resultLabel.setText("Фургон порожній");
                            clearLayoutBox();
                        }
                    } else {
                        resultLabel.setText("Помилка! виберіть фургон");
                    }
                }
            });
            layoutVBox.getChildren().add(chooseButton);
            logLabel.setText("Пошук кави в фургоні");
        } else {
            resultLabel.setText("Немає доступних фургонів");
            clearLayoutBox();
        }
    }

    @FXML
    protected void saveToFileMenuItem() {
        logger.info("saveToFileBtn clicked");
        logger.info("trying to save Coffee data to file");
        clearLayoutBox();
        try(ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(
                "src/main/resources/savedData/coffee.dat"))) {
            oos1.writeObject(storage.getCoffeeStorage());
            oos1.close();
            logger.info("Coffee data successfully saved");
        } catch (FileNotFoundException fnfe) {
            errorLogger.error(fnfe.toString());
        } catch (IOException ioe) {
            errorLogger.error(ioe.toString());
        } catch(Exception e) {
            errorLogger.error(e.toString());
        }

        logger.info("trying to save Vans data to file");
        try(ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(
                "src/main/resources/savedData/vans.dat"
        ))) {
            oos2.writeObject(storage.getVansStorage());
            oos2.close();
            logger.info("Vans data successfully saved");
        } catch (FileNotFoundException fnfe) {
            errorLogger.error(fnfe.toString());
        } catch (IOException ioe) {
            errorLogger.error(ioe.toString());
        } catch(Exception e) {
            errorLogger.error(e.toString());
            e.printStackTrace();
        }
        resultLabel.setText("Виберіть дію: ");
        logLabel.setText("Збереження в файл завершено");
        logger.info("full data successfully saved");
    }

    @FXML
    protected void loadFromFileMenuItem () {
        logger.info("loadFromFileBtn clicked");
        logger.info("trying to load Coffee data from file");
        clearLayoutBox();
        try(ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream(
                "src/main/resources/savedData/coffee.dat"))) {
            storage.setCoffeeStorage(((ArrayList<Coffee>)ois1.readObject()));
            ois1.close();
            logger.info("Coffee data successfully restored");
        } catch(Exception ex){
            errorLogger.error(ex.toString());
        }
        logger.info("trying to load Vans data from file");
        try(ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(
                "src/main/resources/savedData/vans.dat"))) {
            storage.setVansStorage(((ArrayList<Van>)ois2.readObject()));
            ois2.close();
            logger.info("Vans data successfully restored");
        } catch(Exception ex){
            errorLogger.error(ex.toString());
        }
        resultLabel.setText("Виберіть дію: ");
        logLabel.setText("Відновлення з файлу завершено");
        logger.info("full data successfully restored");
    }

    @FXML
    protected void exitMenuItem () {
        logger.info("exitBtn clicked");
        Platform.exit();
    }

    @FXML
    private void chooseVan(ArrayList<Van> vanList) {
        selectedVan = null;
        clearLayoutBox();
        if(!vanList.isEmpty()) {
            resultLabel.setText("Виберіть фургон: ");

            ListView listView = new ListView();
            listView.setPrefWidth(450);
            listView.setPrefHeight(280);
            listView.getItems().addAll(vanList);
            listView.setCellFactory(new VanCellFactory());
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Van>() {
                public void changed(ObservableValue<? extends Van> ov, final Van oldValue, final Van newValue) {
                    setSelectedVan(newValue);
                }
            });
            layoutVBox.getChildren().add(listView);
        } else {
            resultLabel.setText("Список пустий");
        }
    }

    @FXML
    private void chooseCoffee(ArrayList<Coffee> coffeeList) {
        selectedCoffee = null;
        clearLayoutBox();
        if(!coffeeList.isEmpty()) {
            resultLabel.setText("Виберіть каву: ");

            ListView listView = new ListView();
            listView.setPrefWidth(450);
            listView.setPrefHeight(280);
            listView.getItems().addAll(coffeeList);
            listView.setCellFactory(new CoffeeCellFactory());
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Coffee>() {
                public void changed(ObservableValue<? extends Coffee> ov,
                                    final Coffee oldValue, final Coffee newValue) {
                    setSelectedCoffee(newValue);
                }
            });
            layoutVBox.getChildren().add(listView);
        } else {
            resultLabel.setText("Список пустий");
        }
    }

    @FXML
    private void chooseCoffeeSort(String coffeeArr[]) {
        clearLayoutBox();
        int sortsCount = Integer.parseInt(coffeeArr[2]);
        resultLabel.setText("Виберіть сорт кави: ");

        ToggleGroup group = new ToggleGroup();
        RadioButton buttonArr[] = new RadioButton[10];
        HBox hBoxArr[] = new HBox[10];
        Label labelArr[] = new Label[10];
        for (int i = 0; i < sortsCount; i++) {
            hBoxArr[i] = new HBox();
            hBoxArr[i].setSpacing(10);

            buttonArr[i] = new RadioButton(String.valueOf((i + 1)));
            buttonArr[i].setToggleGroup(group);
            buttonArr[i].setId(String.valueOf((i + 1)));

            labelArr[i] = new Label("Ціна за 1л: " + selectedCoffee.calculateSortPrice((i+1)));
            labelArr[i].setPrefWidth(300);
            labelArr[i].setPrefHeight(15);
            labelArr[i].setStyle("-fx-font-size:14");
            hBoxArr[i].getChildren().addAll(buttonArr[i], labelArr[i]);
            layoutVBox.getChildren().add(hBoxArr[i]);
        }
        buttonArr[0].setSelected(true);

        Button chooseButton = new Button();
        chooseButton.setText("Вибрати");
        chooseButton.setStyle("-fx-font-size:18");
        chooseButton.setPrefWidth(300);
        chooseButton.setPrefHeight(40);
        chooseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
                chooseFullCoffeePrice(Integer.parseInt(selectedButton.getId()));
            }
        });
        layoutVBox.getChildren().add(chooseButton);
    }

    @FXML
    private void chooseFullCoffeePrice(int coffeeSort) {
        clearLayoutBox();
        layoutVBox.setPrefWidth(300);
        resultLabel.setText("Введіть дані: ");

        Label fullCoffeePriceLabel = new Label();
        fullCoffeePriceLabel.setPrefWidth(300);
        fullCoffeePriceLabel.setPrefHeight(25);
        fullCoffeePriceLabel.setStyle("-fx-font-size:18");
        fullCoffeePriceLabel.setText("Вкажіть загальну ціну:");

        TextField fullCoffeePriceTextField = new TextField();
        fullCoffeePriceTextField.setPrefWidth(300);
        fullCoffeePriceTextField.setPrefHeight(25);
        fullCoffeePriceTextField.setStyle("-fx-font-size:18");

        Button addButton = new Button();
        addButton.setText("Добавити каву на склад");
        addButton.setStyle("-fx-font-size:18");
        addButton.setPrefWidth(300);
        addButton.setPrefHeight(40);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    int fullCoffeePrice = Integer.parseInt(fullCoffeePriceTextField.getText());
                    if(Integer.parseInt(fullCoffeePriceTextField.getText()) > 0) {
                        selectedCoffee.setSort(coffeeSort);
                        selectedCoffee.setFullPrice(fullCoffeePrice);
                        selectedCoffee.calculateFullVolume();
                        selectedCoffee.packCoffee();
                        storage.addCoffee(selectedCoffee);
                        logLabel.setText("Успішно додано каву на склад");
                        logger.info("new Coffee added to storage");
                        resultLabel.setText("Виберіть дію:");
                        clearLayoutBox();
                    } else {
                        resultLabel.setText("Помилка! вкажіть ціну більзу за 0");
                    }
                } catch (NumberFormatException nfe) {
                    resultLabel.setText("Помилка! неправильні дані");
                }
            }
        });
        layoutVBox.getChildren().addAll(fullCoffeePriceLabel, fullCoffeePriceTextField, addButton);
    }

    @FXML
    private void printCoffee(ArrayList<Coffee> coffeeList) {
        clearLayoutBox();
        if (!coffeeList.isEmpty()) {
            resultLabel.setText("Список кави на складі:");
            ListView listView = new ListView();
            listView.setPrefWidth(450);
            listView.setPrefHeight(330);
            layoutVBox.getChildren().add(listView);
            listView.getItems().addAll(coffeeList);
            listView.setCellFactory(new CoffeeCellFactory());
        } else {
            resultLabel.setText("Список пустий");
        }
    }

    @FXML
    private void printVans(ArrayList<Van> vanList) {
        clearLayoutBox();
        if (!vanList.isEmpty()) {
            resultLabel.setText("Список фургонів на складі:");
            ListView listView = new ListView();
            listView.setPrefWidth(450);
            listView.setPrefHeight(330);
            layoutVBox.getChildren().add(listView);
            listView.getItems().addAll(vanList);
            listView.setCellFactory(new VanCellFactory());
        } else {
            resultLabel.setText("Список пустий");
        }
    }

    @FXML
    private boolean checkDuplicateName(String vanName) {
        boolean duplicate = true;
        if(!storage.getVansStorage().isEmpty()) {
            while (duplicate) {
                for(int i = 0; i < storage.getVansStorage().size(); i++) {
                    if(!storage.getVansStorage().get(i).getName().equals(vanName)) {
                        duplicate = false;
                    } else {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    @FXML
    private void clearLayoutBox() {
        layoutVBox.setPrefWidth(450);
        if(!layoutVBox.getChildren().isEmpty()) {
            layoutVBox.getChildren().clear();
        }
    }

    private void setSelectedVan(Van selectedVan) {
        this.selectedVan = selectedVan;
    }

    private void setSelectedCoffee(Coffee selectedCoffee) {
        this.selectedCoffee = selectedCoffee;
    }
}