package com.pp.kursova.model.javafx;

import com.pp.kursova.model.Coffee;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CoffeeCellFactory implements Callback<ListView<Coffee>, ListCell<Coffee>>  {
    @Override
    public ListCell<Coffee> call(ListView<Coffee> coffeeListView) {
        return new CoffeeCell();
    }
}