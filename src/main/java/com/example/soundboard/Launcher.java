package com.example.soundboard;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javafx.application.Application;

import javax.swing.*;
import java.io.IOException;

public class Launcher {

//main files
    public static void main(String[] args) throws IOException {

        //Sets the File chooser to dark mode (This uses swing)
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Launches
        Application.launch(GUIMain.class, args);

    }
}

//Note: Launcher and GUIMain are different classes because Javafx needs to "Extend application", which is telling it
//the start of the GUI App. If it's on the class that has main, it will execute before Application.launch causing it to crash
//in specific scenarios depending on how its packaged. So calling it through Application.launch circumvents this problem.

//For JavaFX I used the main Documentation combined with a javaFX exampleProject created by jjenkov

// https://openjfx.io/javadoc/26/

// https://github.com/jjenkov/javafx-examples

//All code is original, ChatGPT was used to help explain what some functions do and how the logic is supposed to flow
//but no generated code was used.

//I used the Ide's recommendation to generate Try catch statements