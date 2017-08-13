package controllers;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class MainController {
    private ConverterController converterController;
    private ExchangeController exchangeController;
    private Stage primaryStage;

    public void calculatorButtonClicked(ActionEvent actionEvent) {
    }

    public void exchangeButtonClicked(ActionEvent actionEvent) {
        converterController.initConverter("views/converter.fxml");
    }
}
