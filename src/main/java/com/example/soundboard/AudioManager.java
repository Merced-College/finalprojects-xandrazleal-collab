package com.example.soundboard;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.CheckBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioManager {

    /*
    Author: Xandra Leal
    Class: AudioManager

    Description:
    Manages the AudioPlayers, that plays mp3 Files
    Able to Loop Audio, Layer Audio, Play, Pause, and Stop
  */

    //MediaPlayer is what plays the audio files, The List of MediaPLayer handles the overlapping audio.
    private MediaPlayer currentPlayer;
    private final List<MediaPlayer> layeredPlayers = new ArrayList<>();

    private boolean isPlaying = false;
    private double volume = 1.0;

    private final CheckBox loopCheckBox;
    private final CheckBox layerCheckBox;

    private AudioUIManager uiManager;


    //sets class check and loop box to global check and loop box
    public AudioManager(CheckBox loopCheckBox, CheckBox layerCheckBox) {
        this.loopCheckBox = loopCheckBox;
        this.layerCheckBox = layerCheckBox;
    }

    public void setUIManager(AudioUIManager uiManager) {
        this.uiManager = uiManager;
    }

//If layerBox is clicked adds Layered player, else Starts new Player with fxItem.
    public void playSound(fxItem item) {
        Media media = new Media(new File(item.getFilePath()).toURI().toString());
        MediaPlayer player = new MediaPlayer(media);

        if (layerCheckBox.isSelected()) {
            startLayered(player, item.getName());
        } else {
            if (isPlaying && currentPlayer != null) {
                fadeOutAndSwitch(player);
            } else {
                startNew(player);
            }
        }
    }
    //Starts a new Audio player with a fade in of 1 second. stops when audio ends.
    private void startNew(MediaPlayer player) {
        currentPlayer = player;

        player.setCycleCount(getLoopCount());
        player.setVolume(0);
        player.play();

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(player.volumeProperty(), volume))
        );
        fadeIn.play();

        isPlaying = true;
        player.setOnEndOfMedia(() -> isPlaying = false);
    }

    //Fades out the current player and starts a new audioPlayer if one is already playing.
    private void fadeOutAndSwitch(MediaPlayer newPlayer) {
        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(currentPlayer.volumeProperty(), 0))
        );

        fadeOut.setOnFinished(e -> {
            currentPlayer.stop();
            currentPlayer.dispose();
            startNew(newPlayer);
        });

        fadeOut.play();
    }

    //Starts a layered Player, adds it too the Layered player list
    //Calls UI manager to update volume sliders
    private void startLayered(MediaPlayer player, String name) {
        player.setCycleCount(getLoopCount());
        player.setVolume(volume);

        layeredPlayers.add(player);
        player.play();

        if (uiManager != null) {
            uiManager.createLayeredControl(player, name);
        }

        player.setOnEndOfMedia(() -> {
            stopPlayer(player);
            if (uiManager != null) uiManager.removePlayerUI(player);
        });
    }

    //Stops current player
    public void stopPlayer(MediaPlayer player) {
        player.stop();
        player.dispose();
        layeredPlayers.remove(player);
    }

    //Stops all Players current and in Layered list
    public void stopAll() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
            isPlaying = false;
        }

        for (MediaPlayer p : layeredPlayers) {
            p.stop();
            p.dispose();
        }

        layeredPlayers.clear();

        if (uiManager != null) {
            uiManager.clearAll();
        }
    }

    //Pauses current player
    public void pause() {
        if (currentPlayer != null) currentPlayer.pause();
    }

    //resumes current player
    public void resume() {
        if (currentPlayer != null) currentPlayer.play();
    }

    //If a mediaPlayer exists, set it to new volume and sets volume for each player in LayeredPlayers
    public void setVolume(double volume) {
        this.volume = volume;

        if (currentPlayer != null) currentPlayer.setVolume(volume);

        for (MediaPlayer p : layeredPlayers) {
            p.setVolume(volume);
        }
    }

    //If looping is checked updates all players to loop.
    public void updateLooping() {
        int cycle = getLoopCount();

        if (currentPlayer != null) currentPlayer.setCycleCount(cycle);

        for (MediaPlayer p : layeredPlayers) {
            p.setCycleCount(cycle);
        }
    }
    //getLoopCycle, if loopcheckbox is clicked, loopcycle = Infinite, else equals 1
    private int getLoopCount() {
        return loopCheckBox.isSelected() ? MediaPlayer.INDEFINITE : 1;
    }
}