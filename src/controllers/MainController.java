package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button exchangeButton;


    public void calculatorButtonClicked(ActionEvent actionEvent) {
        System.out.println("calculator button clicked");
    }

    public void exchangeButtonClicked(ActionEvent actionEvent) {
        Stage stage;
        stage = (Stage) exchangeButton.getScene().getWindow();
        ExchangeController exchangeController = new ExchangeController();
        exchangeController.initExchanger("views/exchange.fxml", stage);
        System.out.println("exchange button clicked");
    }
}
