package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Tomek on 2017-08-13.
 */
public class ConverterController {

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("views/converter.fxml"));

    Stage currentStage;

    public void setStage (Stage stage){
        currentStage = stage;
    }

    public void initConverter(String fxml){
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();

        }catch(IOException ie){
            ie.printStackTrace();
        }
    }
}
