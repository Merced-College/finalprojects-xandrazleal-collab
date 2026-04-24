package com.example.soundboard;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FileManager {

    /*
Author: Xandra Leal
Class: FileManager

Description:
Manages File systems
includes functions for adding, opening, Sorting and Searching Audio.
 */
    //Creates new ArrayList
    private ArrayList<fxItem> fxList = new ArrayList<>();

    //Gets directory of folder program is in
    String baseDir = new File(System.getProperty("user.dir")).getAbsolutePath();

    //gets directory Audio Files are stored in
    File fxDir = new File(baseDir + "/AudioFile/");

    //Adds Filters and Choosers
    private FilenameFilter mp3Filter;
    private JFileChooser chooser = new JFileChooser();
    private FileNameExtensionFilter filter;

    //new fxItem
    private fxItem addSound;



    public void fxSetup() throws IOException {
        //Creates audioFolder in program file location
        if (!fxDir.exists()) {
            fxDir.mkdirs();
        }

        System.out.println("Dir exists: " + fxDir.exists());
        System.out.println("Files: " + Arrays.toString(fxDir.listFiles()));
        System.out.println("FileManager list size: " + fxList.size());

        //Creates a filter for mp3 files in specific
        mp3Filter = new FilenameFilter() {
            @Override

            //Only true if file ends in .mp3
            public boolean accept(File fxDir, String name) {
                return name.endsWith(".mp3");
            }
        };

        //Array File is added for each item true in mp3Filter
        File[] mp3Files = fxDir.listFiles(mp3Filter);

        //Creates fxItem and adds to arrayList for each true file
        if (mp3Files != null) {
            for (File fileName : mp3Files) {

                fxItem newFile = new fxItem();
                newFile.setFilePath(fileName.getPath());
                newFile.setName(fileName.getName());
                fxList.add(newFile);
                System.out.println("Adding: " + fileName.getName());


            }

        }

    }




//returns arrayList
    public ArrayList<fxItem> getFxAudio(){
        return fxList;
    }

//Uses JFileChooser to create a new file Chooser gui that only accepts mp3 files
    public void addAudio() {

        System.out.println(UIManager.getSystemLookAndFeelClassName());

        filter = new FileNameExtensionFilter("MP3 Files","mp3");

        chooser.setFileFilter(filter);
        addSound = new fxItem();


        int returnVal = chooser.showOpenDialog(null);
        File sourceFile;

        //If choice is valid sets fxList addSound and add it to fxList
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            sourceFile = chooser.getSelectedFile();
            uploadToFolder(sourceFile, fxDir.toString());

            addSound.setFilePath(chooser.getSelectedFile().getPath());
            addSound.setName(chooser.getSelectedFile().getName());
            fxList.add(addSound);
            System.out.println("Adding: " + addSound.getName());
        }



    }
    //Copies user selected file adds it to folder. replaces any same named files
    private void uploadToFolder (File sourceFile, String dir){
        try {
            Path targetPath = Paths.get(dir, sourceFile.getName());
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //Opens audio folder
    public void openFolder() throws IOException {
        if(Desktop.isDesktopSupported()){
            Desktop.getDesktop().open(fxDir);
        }
        fxList.clear();
        fxSetup();
    }

    public void quickSort(java.util.List<fxItem> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);

            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    //Creates the partition middle value for the upper and lower bounds
    public int partition(java.util.List<fxItem> list, int low, int high) {
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
    public void swap(java.util.List<fxItem> list, int i, int j) {
        fxItem temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    //Binary search implementation, checks the middle of list, checks if value is higher or smaller, then checks new half untill found
    public int binarySearch(List<fxItem> list, String target) {
        int left = 0;
        int right = list.size() - 1;

        target = target.toLowerCase();

        while (left <= right) {
            int mid = (left + right) / 2;
            String midVal = list.get(mid).getName().toLowerCase();

            int check = midVal.compareTo(target);

            if (check == 0) {
                return mid; // found
            } else if (check < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1; // not found
    }



    }





