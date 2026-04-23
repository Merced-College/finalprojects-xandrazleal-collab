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

    @FXML
    private Button openFileBtn;

    @FXML
    private Button resetBtn;


    private final FileManager files = new FileManager();
    private ArrayList<fxItem> fxList;
    private ArrayList<Button> buttonList = new ArrayList<>();



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
    private VBox volumeBox;



    @FXML
    public void initialize() throws IOException {
        files.fxSetup();
        fxList = files.getFxAudio();
        setupFxPanel();

        openAudioFolder();
        resetGUI();
        selectAudio();
        volumeSlider.setValue(volumeSlider.getMax());
        volumeSlider();
        search();
        System.out.println("Test");
    }


    public void setupFxPanel() {
        fxGrid.getChildren().clear();
        buttonList.clear();

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
            buttonList.add(btn);
        }

    }

    public void playSound(Button btn, fxItem sound){
        System.out.println(btn.getText());
        System.out.println(sound.getFilePath());

        Media playfx = new Media(new File(sound.getFilePath()).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(playfx);


        if (layerCheckBox.isSelected()) {


            startLayeredPlayer(mediaPlayer, sound.getName());

        } else {

            if (mediaIsPlaying && currentPlayer != null) {

                Timeline fadeOut = new Timeline(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(currentPlayer.volumeProperty(), 0.0))
                );

                fadeOut.setOnFinished(e -> {
                    currentPlayer.stop();
                    currentPlayer.dispose();


                    startNewMedia(mediaPlayer);
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
                Node temp = volumeBox.getChildren().get(0);
                Node temp2 = volumeBox.getChildren().get(1);
                volumeBox.getChildren().removeIf(node -> node instanceof HBox);
                volumeBox.getChildren().add(temp);
                volumeBox.getChildren().add(temp2);
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
            volumeBox.getChildren().removeIf(node -> node instanceof HBox);
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
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(mediaPlayer.volumeProperty(), volume)
                )
        );
        fadeIn.play();

        mediaIsPlaying = true;

        currentPlayer.setOnEndOfMedia(() -> mediaIsPlaying = false);
    }


    public void volumeSlider(){


        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {

            double percentage = (newVal.doubleValue() - volumeSlider.getMin()) / (volumeSlider.getMax() - volumeSlider.getMin()) * 100;


            String style = String.format(
                    "-fx-background-color: linear-gradient(to right, #153773 0%%, #153773 %1$f%%, #3C3F41 %1$f%%, #3C3F41 100%%); -fx-background-radius: 5px; -fx-pref-height: 6px;",
                    percentage
            );
            volumeSlider.lookup(".track").setStyle(style);

            volume = newVal.doubleValue() / 100;

            if (currentPlayer != null) {
                currentPlayer.setVolume(volume);
            }

            for (MediaPlayer player : layeredPlayers) {
                player.setVolume(volume);
            }

            System.out.println("Volume: " + volume);


        });



    }

    private void startLayeredPlayer(MediaPlayer player, String name) {
        player.setCycleCount(getLoopCycleCount());
        layeredPlayers.add(player);

        player.setVolume(volume);
        player.play();

        Label label = new Label(name);

        Slider slider = new Slider(0, 1, volume);
        slider.setPrefWidth(150);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            player.setVolume(newVal.doubleValue());
        });

        Button stopBtn = new Button("X");
        HBox container = new HBox(10, label, slider, stopBtn);


        stopBtn.setOnAction(e -> {
            player.stop();
            player.dispose();
            layeredPlayers.remove(player);
            volumeBox.getChildren().removeIf(node -> node instanceof HBox);
        });




        volumeBox.getChildren().add(container);

        player.setOnEndOfMedia(() -> {
            player.stop();
            player.dispose();
            layeredPlayers.remove(player);
            volumeBox.getChildren().removeIf(node -> node instanceof HBox);
            System.out.println("boop");
        });
    }



    @FXML
    public void selectAudio() throws IOException {
        addBtn.setOnAction(event -> {

            files.addAudio();
            System.out.println("Button press");
            fxList = files.getFxAudio();
            rebuildGrid(fxList);

        });



    }

    public void openAudioFolder() throws IOException {
        openFileBtn.setOnAction(event -> {

            try {

                files.openFolder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });



    }

    private void resetGUI(){
        resetBtn.setOnAction(event -> {

            fxList.clear();
            try {
                files.fxSetup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fxList = files.getFxAudio();
            rebuildGrid(fxList);
            System.out.println("Button press");
        });
    }




    private void search() {
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {

            String filter = newVal == null ? "" : newVal.toLowerCase();

            if (filter.isEmpty()) {
                rebuildGrid(fxList);
                return;
            }


            List<fxItem> sortedList = new ArrayList<>(fxList);
            quickSort(sortedList, 0, sortedList.size() - 1);


            int index = binarySearch(sortedList, filter);

            List<fxItem> results = new ArrayList<>();

            if (index != -1) {

                results.add(sortedList.get(index));
            } else {

                for (fxItem item : sortedList) {
                    if (item.getName().toLowerCase().contains(filter)) {
                        results.add(item);
                    }
                }
            }

            rebuildGrid(results);
        });
    }

    private void quickSort(List<fxItem> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);

            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

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

    private void swap(List<fxItem> list, int i, int j) {
        fxItem temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

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

    public void rebuildGrid(List<fxItem> list) {
        fxGrid.getChildren().clear();
        buttonList.clear();

        int maxColumns = 5;

        for (int i = 0; i < list.size(); i++) {
            fxItem item = list.get(i);

            Button btn = new Button(item.getName());

            int column = i % maxColumns;
            int row = i / maxColumns;

            btn.setOnAction(e -> playSound(btn, item));

            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            fxGrid.add(btn, column, row);
            buttonList.add(btn);
        }
    }

}