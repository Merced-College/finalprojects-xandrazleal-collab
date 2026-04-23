package com.example.soundboard;

import java.util.Objects;

public class fxItem {
    private String name;
    private String filePath;

    public fxItem() {}

    public fxItem(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof fxItem)) return false;
        fxItem other = (fxItem) o;
        return Objects.equals(name, other.name) &&
                Objects.equals(filePath, other.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, filePath);
    }
}