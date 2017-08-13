package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;

public class MainController {
    //private ConverterController converterController;
    private ExchangeController exchangeController;

    @FXML
    Button exchangeButton;


//    private Stage primaryStage; RACZEJ ŹLE
//
//
//    public void setPrimaryStage(Stage stage){
//        this.primaryStage = stage;
//    }



    public void calculatorButtonClicked(ActionEvent actionEvent) {
        System.out.println("calculator button clicked");
    }

    public void exchangeButtonClicked(ActionEvent actionEvent) {
        //exchangeButton.getScene();
        //ExchangeController exchangeController = new ExchangeController(primaryStage); ŹLE
        exchangeController.initExchanger("views/converter.fxml");
        System.out.println("exchange button clicked");
    }
}
