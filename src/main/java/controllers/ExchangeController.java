package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.ArrayOfExchangeRatesTable;
import model.CurrencyData;
import model.Rate;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tomek on 2017-08-13.
 */
public class ExchangeController {

    @FXML
    private Button backButton;
    @FXML
    private Button customizeButton;
    @FXML
    private TableView exchangeTableView;


    public void initExchanger(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/exchange.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);


            CurrencyData currencyData = new CurrencyData();
            ArrayOfExchangeRatesTable arrayOfExchangeRatesTable = currencyData.init();
            System.out.println("stop");

            populateTableView(arrayOfExchangeRatesTable);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void backButtonAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/main.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException ie) {
            ie.printStackTrace();
        }

    }

    public void customizeButtonAction(ActionEvent actionEvent) {
    }

    public void populateTableView(ArrayOfExchangeRatesTable arrayOfExchangeRatesTable){
        List<Rate> rateList = arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList();
        for(Rate element: rateList){
            TableColumn column = new TableColumn(element.getCode());
            exchangeTableView.getColumns().addAll(column); //null pointer
        }
    }
}
