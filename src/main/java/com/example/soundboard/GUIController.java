package com.example.soundboard;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIController {

    //@FXML links the item created here with the item in the FXML sheet, with this they act as the same, where changing one will change the
    //other
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

    @FXML
    private Button openFileBtn;

    @FXML
    private Button resetBtn;

    //Creates instance of custom FileManager Class
    private final FileManager files = new FileManager();

    //ArrayList for class fxItem
    private ArrayList<fxItem> fxList;

    //Hashmap for fxItem
    private Map<fxItem, Button> buttonMap = new HashMap<>();


    //Gridpane is used to add buttons in order, allowing 5 rows with infinite column
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

    @FXML
    CheckBox layerCheckBox;

    //MediaPlayer is what plays the audio files., The List of MediaPLayer handles the overlapping audio.
    private MediaPlayer currentPlayer;
    private Boolean mediaIsPlaying = false;
    private List<MediaPlayer> layeredPlayers = new ArrayList<>();

    @FXML
    private VBox volumeBox;


    @FXML
    public void initialize() throws IOException {
        //starts Filemanager setup, imports the fxItem list from files, and adds them to panel
        files.fxSetup();
        fxList = files.getFxAudio();
        setupFxPanel();

         //Initialises the event listener for the audio folder button, reset button, addAudio button
        openAudioFolder();
        resetGUI();
        selectAudio();

        //sets volume to 100 on startup, initializes slider listener and search button
        volumeSlider.setValue(volumeSlider.getMax());
        volumeSlider();
        search();
        System.out.println("Intitialize successfull");
    }


    public void setupFxPanel() {

        //Adds buttons depending on how many items are in fxList
        for (int rows = 0; rows < fxList.size(); rows++) {
            fxItem item = fxList.get(rows);

            //creates new button with fxItem name
            Button btn = new Button(item.getName());
            //Sets which column & row the button will be in the fxGrid.

            //remainder will be 1-5 which sets the column
            int column = rows % maxButtonperRow;

            //division will set the rows
            int row = rows / maxButtonperRow;


            //adds event listener for button press that calls playSound
            btn.setOnAction(event -> playSound(btn, item));

            //Adds button to grid
            fxGrid.add(btn, column, row);


        }

    }

    //Arguments are passed by the buttons created above
    public void playSound(Button btn, fxItem sound) {
        System.out.println(btn.getText());
        System.out.println(sound.getFilePath());

        //Creates a new media called PlayFx that links to the fxItem linked to the button that called it.
        Media playfx = new Media(new File(sound.getFilePath()).toURI().toString());

        //The Media object is used to tell the MediaPlayer where the sound files are and how to encode it.
        MediaPlayer mediaPlayer = new MediaPlayer(playfx);

        //If layered checkbox is clicked, calls a custom function that adds player to a List to track it.

        //Else it stops current Audio player with a fadeout, and plays starts new Audio Player with a fadeIn
        if (layerCheckBox.isSelected()) {


            startLayeredPlayer(mediaPlayer, sound.getName());

        } else {

            if (mediaIsPlaying && currentPlayer != null) {
                //Timeline works by "Animating" the player. This tells the player that over 1 second change the current audio to 0
                Timeline fadeOut = new Timeline(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(currentPlayer.volumeProperty(), 0.0))
                );
                //When finished Stops and completely removes player from program
                fadeOut.setOnFinished(e -> {
                    currentPlayer.stop();
                    currentPlayer.dispose();

                    //starts new player with new Media
                    startNewMedia(mediaPlayer);
                });

                fadeOut.play();
            } else {
                startNewMedia(mediaPlayer);
            }
        }

        //Reads if loopcheckbox is clicked.
        loopCheckBox.setOnAction(event -> {

            //if checkbox is clicked sets cycle to value INDEFINITE, meaning infinite Cycle is amount of times played
            int cycle = getLoopCycleCount();

            if (currentPlayer != null) {
                currentPlayer.setCycleCount(cycle);
            }
            //Sets for all players active
            for (MediaPlayer player : layeredPlayers) {
                player.setCycleCount(cycle);
            }
        });
        //if checkbox is clicked on it creates a new volumeSlider for each instance, if unchecked it destroys all mediaPlayer
        layerCheckBox.setOnAction(event -> {
            if (!layerCheckBox.isSelected()) {
                for (MediaPlayer player : layeredPlayers) {
                    player.stop();
                    player.dispose();
                }
                layeredPlayers.clear();
                Node temp = volumeBox.getChildren().get(0);
                Node temp2 = volumeBox.getChildren().get(1);
                volumeBox.getChildren().removeIf(node -> node instanceof HBox);
                volumeBox.getChildren().add(temp);
                volumeBox.getChildren().add(temp2);
            }
        });

        //when media is over, sets global boolean to false
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaIsPlaying = false;

        });
        //If there's sound playing, pause it.
        pauseBtn.setOnAction(event -> {
            if (currentPlayer != null) {
                currentPlayer.pause();
            }
        });
            //If there's sound paused, play it
        playBtn.setOnAction(event -> {
            if (currentPlayer != null) {
                currentPlayer.play();
            }
        });


        //Stops all audio players, destroys them, and does so for all layered audioPLayers and gets rid of all extra
        //Volume Sliders
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
            volumeBox.getChildren().removeIf(node -> node instanceof HBox);
        });


    }
    //getLoopCycle, if loopcheckbox is clicked, loopcycle = Infinite, else equals 1
    private int getLoopCycleCount() {
        return loopCheckBox.isSelected() ? MediaPlayer.INDEFINITE : 1;
    }

    //Starts the sound of a mediaplayer with a fade in over 1 second, and sets global Boolean to true.
    private void startNewMedia(MediaPlayer mediaPlayer) {
        currentPlayer = mediaPlayer;

        currentPlayer.setCycleCount(getLoopCycleCount());

        mediaPlayer.setVolume(0.0);
        mediaPlayer.play();

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(mediaPlayer.volumeProperty(), volume)
                )
        );
        fadeIn.play();

        mediaIsPlaying = true;

        currentPlayer.setOnEndOfMedia(() -> mediaIsPlaying = false);
    }

    //Creates the main Volume Slider
    public void volumeSlider() {

        //gets value of slider position when slider is moved, executes whenever slider is changed
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            //converts volume slider return to a double and divides it to get the position where the slider is at so the css can create a
            //volume bar effect
            double percentage = (newVal.doubleValue() - volumeSlider.getMin()) / (volumeSlider.getMax() - volumeSlider.getMin()) * 100;

            //Sets a blue bar behind the slider point, gray above it
            String style = String.format(
                    "-fx-background-color: linear-gradient(to right, #153773 0%%, #153773 %1$f%%, #3C3F41 %1$f%%, #3C3F41 100%%); -fx-background-radius: 5px; -fx-pref-height: 6px;",
                    percentage
            );
            volumeSlider.lookup(".track").setStyle(style);

            //sets volume to double
            volume = newVal.doubleValue() / 100;

            //If a mediaPlayer exists, set it to new volume
            if (currentPlayer != null) {
                currentPlayer.setVolume(volume);
            }

            //sets all layered audio players to the volume(stacks with the user set volume also)
            for (MediaPlayer player : layeredPlayers) {
                player.setVolume(volume);
            }

            System.out.println("Volume: " + volume);


        });


    }
    //Creates a list of layered player and plays them.
    private void startLayeredPlayer(MediaPlayer player, String name) {
        player.setCycleCount(getLoopCycleCount());
        //adds player to layeredPLayers List, sets volume and play
        layeredPlayers.add(player);

        player.setVolume(volume);
        player.play();


        //Creates a new mini Volume Slider whenever a layered player is added
        Label label = new Label(name);

        Slider slider = new Slider(0, 1, volume);
        slider.setPrefWidth(150);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            player.setVolume(newVal.doubleValue());
        });


        //Creates a stop button that when clicked stops audio and deletes mini audioslider
        Button stopBtn = new Button("X");
        HBox container = new HBox(10, label, slider, stopBtn);


        stopBtn.setOnAction(e -> {
            player.stop();
            player.dispose();
            layeredPlayers.remove(player);

            //This is needed so It only deletes the ones linked to the created Hbox so
            //the main slider isn't deleted
            volumeBox.getChildren().removeIf(node -> node instanceof HBox);
        });


        volumeBox.getChildren().add(container);

        //Stops and deletes when Audio has ended
        player.setOnEndOfMedia(() -> {
            player.stop();
            player.dispose();
            layeredPlayers.remove(player);
            volumeBox.getChildren().removeIf(node -> node instanceof HBox);
            System.out.println("Audio Ended");
        });
    }


    @FXML

    //links button to addAudio from fileManager
    public void selectAudio() throws IOException {
        addBtn.setOnAction(event -> {

            files.addAudio();
            System.out.println("Add audio pressed");
            //updates fxList with added audio files
            fxList = files.getFxAudio();

            //rebuilds button layout with any added audioFiles
            rebuildGrid(fxList);

        });


    }
    //Links a button to openFolder in FileManager
    public void openAudioFolder() throws IOException {
        openFileBtn.setOnAction(event -> {

            try {

                files.openFolder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


    }

    //Resets fxList and rebuilds button Grid
    private void resetGUI() {
        resetBtn.setOnAction(event -> {

            fxList.clear();
            try {
                files.fxSetup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fxList = files.getFxAudio();
            rebuildGrid(fxList);
            System.out.println("Reset button pressed");
        });
    }

    //Creates the search Algorithm
    private void search() {

        //Updates searchbar and calls search whenever it is changed
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            //Creates a filter, if the newVal from searchbar returns null, sets it to "" to avoid errors, else sets the value to lowercase for easier parsing.
            String filter = newVal == null ? "" : newVal.toLowerCase();

            //If nothing in filter, rebuilds Grid
            if (filter.isEmpty()) {
                rebuildGrid(fxList);
                return;
            }


            List<fxItem> sortedList = new ArrayList<>(fxList);
            //Sorts fxList
            quickSort(sortedList, 0, sortedList.size() - 1);

            //Calls a binary search that returns every result with the added filter
            int index = binarySearch(sortedList, filter);

            //New list of results for returned binary search
            List<fxItem> results = new ArrayList<>();

            if (index != -1) {
                //if return not -1 sorted list index to results
                results.add(sortedList.get(index));
            } else {
                //Adds to sorted list for each item returned from results
                for (fxItem item : sortedList) {
                    if (item.getName().toLowerCase().contains(filter)) {
                        results.add(item);
                    }
                }
            }
            //Rebuilds grid on each update
            rebuildGrid(results);
        });
    }

    //Recursive implementation of quicksort, checks the middle and upper pivot points untill value is found
    private void quickSort(List<fxItem> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);

            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

        //Creates the partition middle value for the upper and lower bounds
    private int partition(List<fxItem> list, int low, int high) {
        String pivot = list.get(high).getName().toLowerCase();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).getName().toLowerCase().compareTo(pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }
    //Swaps two elements
    private void swap(List<fxItem> list, int i, int j) {
        fxItem temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    //Binary search implementation, checks the middle of list, checks if value is higher or smaller, then checks new half untill found
    private int binarySearch(List<fxItem> list, String target) {
        int left = 0;
        int right = list.size() - 1;

        target = target.toLowerCase();

        while (left <= right) {
            int mid = (left + right) / 2;
            String midVal = list.get(mid).getName().toLowerCase();

            int cmp = midVal.compareTo(target);

            if (cmp == 0) {
                return mid; // found
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1; // not found
    }

    //Rebuild grid

    public void rebuildGrid(List<fxItem> list) {
        //clears hashmap
        buttonMap.clear();

        //For each item list creates a playsound button and adds it to a Hashmap
        for (int i = 0; i < list.size(); i++) {
            fxItem item = list.get(i);
            Button btn = new Button(item.getName());

            int column = i % 5;
            int row = i / 5;

            btn.setOnAction(e -> playSound(btn, item));

            fxGrid.add(btn, column, row);

            buttonMap.put(item, btn);

        }

    }

}