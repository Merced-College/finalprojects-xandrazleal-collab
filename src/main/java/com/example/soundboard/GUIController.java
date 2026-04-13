package com.example.soundboard;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIController {

    @FXML
    private TableView<fxItem> fxTable;

    @FXML
    private TableColumn<fxItem, Void> r1;

    @FXML
    private Button addBtn;

    @FXML
    private Button pauseBtn;
    @FXML
    private Button playBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchBar;


    private final FileManager files = new FileManager();
    private ArrayList<fxItem> fxList;



    @FXML
    private GridPane fxGrid;
    int maxButtonperRow = 5;

    private double volume = 1.0;

    @FXML
    Slider volumeSlider;

    @FXML
    CheckBox loopCheckBox;

    @FXML
    Button stopBtn;

    @FXML CheckBox layerCheckBox;

    private MediaPlayer currentPlayer;
    private Boolean mediaIsPlaying = false;
    private List<MediaPlayer> layeredPlayers = new ArrayList<>();


    @FXML
    public void initialize() throws IOException {
        files.fxSetup();
        fxList = files.getFxAudio();
        setupFxPanel();
        openAudioFolder();
        volumeSlider.setValue(volumeSlider.getMax());
        volumeSlider();
        System.out.println("Test");
    }


    public void setupFxPanel() {


        int maxRows = fxList.size();
        int maxColumns = 5;
        for (int rows = 0; rows < maxRows; rows++) {
            Button btn = new Button(fxList.get(rows).getName());

            int column = rows % maxColumns;
            int row = rows / maxColumns;


            int finalRows = rows;
            btn.setOnAction(event -> {
                playSound(btn, fxList.get(finalRows));
            });

            fxGrid.setHgap(10);
            fxGrid.setVgap(10);
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            fxGrid.add(btn, column, row);
        }

    }

    public void playSound(Button btn, fxItem sound){
        System.out.println(btn.getText());
        System.out.println(sound.getFilePath());

        Media playfx = new Media(new File(sound.getFilePath()).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(playfx);


        if (layerCheckBox.isSelected()) {

            // ✅ LAYER MODE → don't stop anything
            startLayeredPlayer(mediaPlayer);

        } else {

            if (mediaIsPlaying && currentPlayer != null) {

                Timeline fadeOut = new Timeline(
                        new KeyFrame(Duration.seconds(2),
                                new KeyValue(currentPlayer.volumeProperty(), 0.0))
                );

                fadeOut.setOnFinished(e -> {
                    currentPlayer.stop();
                    currentPlayer.dispose();


                    startNewMedia(mediaPlayer); // ✅ start AFTER fade-out
                });

                fadeOut.play();

            } else {
                startNewMedia(mediaPlayer);
            }
        }



        loopCheckBox.setOnAction(event -> {

                    int cycle = getLoopCycleCount();

                    if (currentPlayer != null) {
                        currentPlayer.setCycleCount(cycle);
                    }

                    for (MediaPlayer player : layeredPlayers) {
                        player.setCycleCount(cycle);
                    }
                });

        layerCheckBox.setOnAction(event -> {
            if (!layerCheckBox.isSelected()) {
                // Turning OFF layering → stop all layered sounds
                for (MediaPlayer player : layeredPlayers) {
                    player.stop();
                    player.dispose();
                }
                layeredPlayers.clear();
            }
        });





        mediaPlayer.setOnEndOfMedia(() ->{
           mediaIsPlaying = false;

        });

        pauseBtn.setOnAction(event -> {
            if (currentPlayer != null) {
                currentPlayer.pause();
            }
        });

        playBtn.setOnAction(event -> {
            if (currentPlayer != null) {
                currentPlayer.play();
            }
        });

        stopBtn.setOnAction(event -> {

            if (currentPlayer != null) {
                currentPlayer.stop();
                currentPlayer.dispose();
                currentPlayer = null;
                mediaIsPlaying = false;
            }

            // Stop layered players
            for (MediaPlayer player : layeredPlayers) {
                player.stop();
                player.dispose();
            }
            layeredPlayers.clear();
        });






    }

    private int getLoopCycleCount() {
        return loopCheckBox.isSelected() ? MediaPlayer.INDEFINITE : 1;
    }

    private void startNewMedia(MediaPlayer mediaPlayer) {
        currentPlayer = mediaPlayer;

        currentPlayer.setCycleCount(getLoopCycleCount());

        mediaPlayer.setVolume(0.0);
        mediaPlayer.play();

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(mediaPlayer.volumeProperty(), volume)
                )
        );
        fadeIn.play();

        mediaIsPlaying = true;

        currentPlayer.setOnEndOfMedia(() -> mediaIsPlaying = false);
    }

    private void startLayeredPlayer(MediaPlayer player){
        currentPlayer.setCycleCount(getLoopCycleCount());
        layeredPlayers.add(player);
        player.setVolume(0.0);
        player.play();

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(player.volumeProperty(), volume)
                )
        );
        fadeIn.play();

        player.setOnEndOfMedia(() -> layeredPlayers.remove(player));
    }





    public void volumeSlider(){


        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {

            double percentage = (newVal.doubleValue() - volumeSlider.getMin()) / (volumeSlider.getMax() - volumeSlider.getMin()) * 100;


            String style = String.format(
                    "-fx-background-color: linear-gradient(to right, #153773 0%%, #153773 %1$f%%, #3C3F41 %1$f%%, #3C3F41 100%%); -fx-background-radius: 5px; -fx-pref-height: 6px;",
                    percentage
            );
            volumeSlider.lookup(".track").setStyle(style);

            int slvolume = newVal.intValue();
            volume = (double) slvolume /100;
            System.out.println("Volume: " + volume);
        });



    }



    @FXML
    public void openAudioFolder(){
        addBtn.setOnAction(event -> {

            files.addAudio();
            System.out.println("Button press");
            setupFxPanel();
        });


    }

    public void search(){
        searchBar.setOnAction(event -> {
            if(!searchBar.getText().isEmpty()){

            }
        });


    }

}