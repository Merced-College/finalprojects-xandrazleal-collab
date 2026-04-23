package com.example.soundboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;


public class GUIMain extends Application {


    @Override
    public void start(Stage stage) {

        Application.setUserAgentStylesheet(
                new atlantafx.base.theme.PrimerDark().getUserAgentStylesheet()
        );

        System.out.println("START METHOD RUNNING");
        try {
            System.out.println("Starting UI...");

            FXMLLoader loader = new FXMLLoader(
                    GUIMain.class.getResource("/com/example/soundboard/Scene.fxml")
            );

            Scene scene = new Scene(loader.load(), 600, 400);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}






