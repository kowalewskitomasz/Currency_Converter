package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    Stage stage;

    @FXML
    private Button exchangeButton;
    @FXML
    private Button calculatorButton;


    public void calculatorButtonClicked(ActionEvent actionEvent) {

        stage = (Stage) calculatorButton.getScene().getWindow();
        ConverterController converterController = new ConverterController();
        converterController.initConverter(stage);
    }

    public void exchangeButtonClicked(ActionEvent actionEvent) {
        stage = (Stage) exchangeButton.getScene().getWindow();
        ExchangeController exchangeController = new ExchangeController();
        exchangeController.initExchanger(stage);
    }
}
