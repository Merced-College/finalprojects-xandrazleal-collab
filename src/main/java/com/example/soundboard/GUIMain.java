package com.example.soundboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

//tells the program this is the start of the gui
public class GUIMain extends Application {


    @Override
        //The start of the gui, when calling stage Javafx creates a dedicated thread for event listeners and such.
    public void start(Stage stage) {

        //Sets Style to darkmode
        Application.setUserAgentStylesheet(
                new atlantafx.base.theme.PrimerDark().getUserAgentStylesheet()
        );

        System.out.println("START METHOD RUNNING");
        try {
            System.out.println("Starting UI...");
            //Creates class FXMLoader to read from the FXML sheet and points it to directory

            //This is what connects  GUIMain and GUIController, their both getting their values and updating the same FXML file
            FXMLLoader loader = new FXMLLoader(
                    GUIMain.class.getResource("/com/example/soundboard/Scene.fxml")
            );

            //Creates a new Scene(Window) and shows it.
            Scene scene = new Scene(loader.load(), 600, 400);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}







