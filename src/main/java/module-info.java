module com.example.arkanoidscreen {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.arkanoidscreen to javafx.fxml;
    exports com.example.arkanoidscreen;
}