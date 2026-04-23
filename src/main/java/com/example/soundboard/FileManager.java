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
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FileManager {

    private ArrayList<fxItem> fxList = new ArrayList<>();
    private ArrayList<musicItem> musicList = new ArrayList<>();

    String baseDir = new File(System.getProperty("user.dir")).getAbsolutePath();
    File fxDir = new File(baseDir + "/AudioFile/");
    private FilenameFilter mp3Filter;
    private JFileChooser chooser = new JFileChooser();
    private FileNameExtensionFilter filter;
    private fxItem addSound;



    public void fxSetup() throws IOException {

        if (!fxDir.exists()) {
            fxDir.mkdirs();
        }

        System.out.println("Dir exists: " + fxDir.exists());
        System.out.println("Files: " + Arrays.toString(fxDir.listFiles()));
        System.out.println("FileManager list size: " + fxList.size());


        mp3Filter = new FilenameFilter() {
            @Override
            public boolean accept(File fxDir, String name) {
                return name.endsWith(".mp3");
            }
        };

        File[] mp3Files = fxDir.listFiles(mp3Filter);

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





    public ArrayList<fxItem> getFxAudio(){
        return fxList;
    }


    public void addAudio() {

        System.out.println(UIManager.getSystemLookAndFeelClassName());

        filter = new FileNameExtensionFilter("MP3 Files","mp3");

        chooser.setFileFilter(filter);
        addSound = new fxItem();


        int returnVal = chooser.showOpenDialog(null);
        File sourceFile;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            sourceFile = chooser.getSelectedFile();
            uploadToFolder(sourceFile, fxDir.toString());

            addSound.setFilePath(chooser.getSelectedFile().getPath());
            addSound.setName(chooser.getSelectedFile().getName());
            fxList.add(addSound);
            System.out.println("Adding: " + addSound.getName());
        }



    }

    private void uploadToFolder (File sourceFile, String dir){
        try {
            Path targetPath = Paths.get(dir, sourceFile.getName());
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void openFolder() throws IOException {
        if(Desktop.isDesktopSupported()){
            Desktop.getDesktop().open(fxDir);
        }
        fxList.clear();
        fxSetup();
    }



    }





