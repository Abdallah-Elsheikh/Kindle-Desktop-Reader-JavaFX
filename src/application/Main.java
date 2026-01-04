package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        Scene sc = new Scene(root);

        // --- السطر البرمجي السحري لتفعيل التصميم الجديد ---
        sc.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Kindle Reader - Desktop Version");
        stage.setScene(sc);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}