package com.example.soundboard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIController {
/*
    Author: Xandra Leal
    Class: GUIController

    Description:
    Manages GUI Elements Calls Functions in AudioManager and AudioUIManager, Handles all
    event handlers
  */

    //@FXML links the item created here with the item in the FXML sheet, with this they act as the same, where changing one will change the
    //other
    @FXML private Button addBtn, pauseBtn, playBtn, stopBtn, openFileBtn, resetBtn;
    @FXML private TextField searchBar;
    @FXML private Slider volumeSlider;
    @FXML private CheckBox loopCheckBox, layerCheckBox;
    //Gridpane is used to add buttons in order, allowing 5 rows with infinite column
    @FXML private GridPane fxGrid;
    @FXML private VBox volumeBox;

    //Creates instance of custom FileManager Class
    private final FileManager files = new FileManager();

    //ArrayList for class fxItem
    private ArrayList<fxItem> fxList;

    private AudioManager audioManager;
    private AudioUIManager audioUIManager;

    private final int maxButtonPerRow = 5;

    @FXML
    public void initialize() throws IOException {
        //starts Filemanager setup, imports the fxItem list from files
        files.fxSetup();
        fxList = files.getFxAudio();

        // Initialize managers
        audioManager = new AudioManager(loopCheckBox, layerCheckBox);
        audioUIManager = new AudioUIManager(volumeBox, volumeSlider, audioManager);

        audioManager.setUIManager(audioUIManager);

        //Intializes event Listeners in each class
        setupFxPanel();
        wireControls();
        setupSearch();

        volumeSlider.setValue(100);
    }

    private void wireControls() {
        //Event Listeners for Pause, Play, Stop
        pauseBtn.setOnAction(e -> audioManager.pause());
        playBtn.setOnAction(e -> audioManager.resume());
        stopBtn.setOnAction(e -> audioManager.stopAll());

        //Event listener for Checkbox change and Add File button
        loopCheckBox.setOnAction(e -> audioManager.updateLooping());

        addBtn.setOnAction(e -> {
            files.addAudio();
            fxList = files.getFxAudio();
            rebuildGrid(fxList);
        });

        //Button to open Audio Folder
        openFileBtn.setOnAction(e -> {
            try {
                files.openFolder();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Button to reset GUI
        resetBtn.setOnAction(e -> {
            fxList.clear();
            try {
                files.fxSetup();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            fxList = files.getFxAudio();
            rebuildGrid(fxList);
        });
    }

    //Setup for Initialization
    private void setupFxPanel() {
        rebuildGrid(fxList);
    }

    //Rebuilds Grid
    public void rebuildGrid(List<fxItem> list) {
        fxGrid.getChildren().clear();

        //Adds buttons depending on how many items are in fxList
        for (int i = 0; i < list.size(); i++) {
            fxItem item = list.get(i);

            //creates new button with fxItem name
            Button btn = new Button(item.getName());
            //remainder will be 1-5 which sets the column
            int col = i % maxButtonPerRow;
            //division will set the rows
            int row = i / maxButtonPerRow;
            //adds event listener for button press that calls playSound
            btn.setOnAction(e -> audioManager.playSound(item));
            //Adds button to grid
            fxGrid.add(btn, col, row);
        }
    }

    private void setupSearch() {

        //Updates searchbar and calls search whenever it is change
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            //Creates a filter, if the newVal from searchbar returns null, sets it to "" to avoid errors, else sets the value to lowercase for easier parsing.
            String filter = newVal == null ? "" : newVal.toLowerCase();
            //If nothing in filter, rebuilds Grid
            if (filter.isEmpty()) {
                rebuildGrid(fxList);
                return;
            }
            //New list of results for returned binary search
            List<fxItem> results = new ArrayList<>();

            //Sorts through fxList and adds every file that passes the filter
            for (fxItem item : fxList) {
                if (item.getName().toLowerCase().contains(filter)) {
                    results.add(item);
                }
            }

            rebuildGrid(results);
        });
    }
}