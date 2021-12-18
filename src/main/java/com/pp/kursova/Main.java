package com.pp.kursova;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Main extends Application {
    private static Logger logger = Logger.getLogger("generalLogger");
    public static void main(String[] args) {
        logger.info("application has started");
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 770, 460);
        stage.setTitle("CoffeeShop");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        logger.info("application has stopped");
    }
}