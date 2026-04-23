module com.example.soundboard {
    requires javafx.controls;
    requires javafx.fxml;



    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires com.formdev.flatlaf;
    requires atlantafx.base;
    requires javafx.media;

    opens com.example.soundboard to javafx.fxml;
    exports com.example.soundboard;
}