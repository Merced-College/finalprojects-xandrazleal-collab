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