package com.example.soundboard;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;


public class AudioUIManager {

    /*
    Author: Xandra Leal
    Class: AudioUIManager

    Description:
    Manages Audio UI Elements Including Volume Sliders, and Creating Volume
    Sliders for each Layered Audio Player
  */


    private final VBox volumeBox;
    private final Slider mainSlider;

    // Track UI elements tied to each MediaPlayer as a HashMap
    private final Map<MediaPlayer, HBox> playerUIMap = new HashMap<>();

    private final AudioManager audioManager;

    //Setup global AudioUIManager
    public AudioUIManager(VBox volumeBox, Slider mainSlider, AudioManager audioManager) {
        this.volumeBox = volumeBox;
        this.mainSlider = mainSlider;
        this.audioManager = audioManager;

        setupMainVolume();
    }

    // Main volume slider
    private void setupMainVolume() {
        mainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100;
            audioManager.setVolume(volume);
        });
    }

    // Create UI for layered player
    //Adds Volume Slider, Stop Button, updates player on HashMap
    public void createLayeredControl(MediaPlayer player, String name) {

        Label label = new Label(name);

        Slider slider = new Slider(0, 1, 1);
        slider.setPrefWidth(150);

        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                player.setVolume(newVal.doubleValue())
        );

        Button stopBtn = new Button("X");

        HBox container = new HBox(10, label, slider, stopBtn);

        stopBtn.setOnAction(e -> {
            audioManager.stopPlayer(player);
            removePlayerUI(player);
        });

        playerUIMap.put(player, container);
        volumeBox.getChildren().add(container);
    }

    //Removes the layered controller from Hashmap and all elements in volumeBox, including Slider
    //and exit button
    public void removePlayerUI(MediaPlayer player) {
        HBox container = playerUIMap.remove(player);

        if (container != null) {
            volumeBox.getChildren().remove(container);
        }
    }

    //Removes every instance in HashMap and Clears all.
    public void clearAll() {
        playerUIMap.clear();
        volumeBox.getChildren().removeIf(node -> node instanceof HBox);
    }
}