package com.pp.kursova.model.javafx;

import com.pp.kursova.model.Van;
import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

public class VanCell extends ListCell<Van> {
    @Override
    public void updateItem(Van item, boolean empty) {
        super.updateItem(item, empty);

        int index = this.getIndex();
        String name = null;

        if (item != null && !empty) {
            name = (index + 1) + " " + item.toString();
        }
        this.setText(name);
        this.setFont(Font.font(15));
        setGraphic(null);
    }
}