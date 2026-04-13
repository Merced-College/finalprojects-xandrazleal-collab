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

    public void start(Stage stage) throws IOException, IOException {
        Application.setUserAgentStylesheet(
                new atlantafx.base.theme.PrimerDark().getUserAgentStylesheet()
        );
        

        FXMLLoader Loader = new FXMLLoader(GUIMain.class.getResource("Scene.fxml"));
        Scene scene = new Scene(Loader.load(), 600, 400);
       stage.setScene(scene);
       stage.show();


    }


}






