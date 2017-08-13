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
    Stage currentStage;

    public ExchangeController(Stage stage){
        this.currentStage = stage;
    }

    //FXMLLoader loader = new FXMLLoader(getClass().getResource("views/converter.fxml"));


    public void initExchanger(String fxml){
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            currentStage.setScene(scene);

        }catch(IOException ie){
            ie.printStackTrace();
        }
    }
}
