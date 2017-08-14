package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Tomek on 2017-08-13.
 */
public class ExchangeController {


    public void initExchanger(String fxml, Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxml));
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
