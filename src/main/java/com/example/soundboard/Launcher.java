package com.example.soundboard;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javafx.application.Application;

import javax.swing.*;
import java.io.IOException;

public class Launcher {


    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Application.launch(GUIMain.class, args);

    }
}
