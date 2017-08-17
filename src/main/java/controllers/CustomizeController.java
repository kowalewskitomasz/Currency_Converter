package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Tomek on 2017-08-17.
 */
public class CustomizeController {

    public void initCustomize(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/customize.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Currencies App - By Tomek Kowalewski");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }


}
