package com.pp.kursova.model.javafx;

import com.pp.kursova.model.Coffee;
import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

public class CoffeeCell extends ListCell<Coffee> {
    @Override
    public void updateItem(Coffee item, boolean empty) {
        super.updateItem(item, empty);

        int index = this.getIndex();
        String name = null;

        if (item != null && !empty) {
            name = (index + 1) + " " + item.toString();
        }
        this.setText(name);
        this.setFont(Font.font(15));
        this.setStyle("-fx-background-color: transparent;");
        if(this.isPressed()) {
            this.setStyle("-fx-border-color: rgba(214,143,130,255);");
        }
        setGraphic(null);
    }
}