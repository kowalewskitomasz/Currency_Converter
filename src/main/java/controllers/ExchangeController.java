package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
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
    private GridPane exchangeGridPane;


    public void initExchanger(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/exchange.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            scene.getStylesheets().add("styles");

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void initialize(){
        CurrencyData currencyData = new CurrencyData();
        ArrayOfExchangeRatesTable arrayOfExchangeRatesTable = currencyData.init();
        System.out.println("stop");

        populateTableView(arrayOfExchangeRatesTable);
        calculateExchangeRate(arrayOfExchangeRatesTable);
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
        Stage stage = new Stage();
        CustomizeController customizeController = new CustomizeController();
        customizeController.initCustomize(stage);
    }

    public void populateTableView(ArrayOfExchangeRatesTable arrayOfExchangeRatesTable){
        List<Rate> rateList = arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList();
        exchangeGridPane.setPadding(new Insets(10, 10, 10, 10));
        int verticalColumn = 2;
        int horizontalColumn = 2;

        setLabelStyle("PLN", 0, 1);
        setLabelStyle("PLN", 1, 0);

        for(Rate element: rateList){
            if(element.getCode().equals("GBP") || element.getCode().equals("USD") || element.getCode().equals("EUR") || element.getCode().equals("CHF") || element.getCode().equals("JPY") || element.getCode().equals("DKK") || element.getCode().equals("NOK") || element.getCode().equals("SEK") || element.getCode().equals("RUB")) {

                if(verticalColumn <= 5) {
                    setLabelStyle(element.getCode(), 0, verticalColumn);
                    verticalColumn++;
                }

                setLabelStyle(element.getCode(), horizontalColumn, 0);
                horizontalColumn++;
            }
        }
        System.out.println("waiting");
    }

    public void setLabelStyle(String name, int columnIndex, int rowIndex){
        Label label = new Label(name);
        label.setPrefSize(70,50);
        label.setFont(new Font("Open Sans", 14));
        label.getStyleClass().add("game-grid");
        GridPane.setFillWidth(label, true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        exchangeGridPane.add(label, columnIndex, rowIndex);
    }

    public void calculateExchangeRate(ArrayOfExchangeRatesTable arrayOfExchangeRatesTable){
        List<Rate> rateList = arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList();

        for(int i = 0 ; i <= 5 ; i++){
            Label label1 = (Label) getNodeByIndex(i, 0);
            if(label1 != null) {
                for(int j = 0 ; j <= 10 ; j++){
                    Label label2 = (Label) getNodeByIndex(0, j);
                    if(label2 != null){
                        double currencyOne = 1;
                        double currencyTwo = 1;
                        String nameOfFirstCurrency = "";
                        String nameOfSecondCurrency = "";

                        for(Rate rate: rateList){
                            if(rate.getCode().equals(label1.getText())){
                                currencyOne = rate.getMid();
                                nameOfFirstCurrency = rate.getCode();
                            }
                            if(rate.getCode().equals(label2.getText())){
                                currencyTwo = rate.getMid();
                                nameOfSecondCurrency = rate.getCode();
                            }
                        }

                        if(nameOfFirstCurrency == ""){
                            nameOfFirstCurrency = "PLN";
                        }
                        if (nameOfSecondCurrency == "") {
                            nameOfSecondCurrency = "PLN";
                        }

                        double result = currencyOne/currencyTwo;
                        String resultString = String.valueOf(result);
                        resultString = resultString.substring(0, Math.min(resultString.length(), 6));
                        setLabelStyle(resultString, j , i);
                        setTooltip(nameOfFirstCurrency, nameOfSecondCurrency, resultString ,j ,i);
                    }
                }
            }
        }
    }

    private void setTooltip(String nameOfFirstCurrency, String nameOfSecondCurrency, String text, int j, int i) {
        Tooltip tooltip = new Tooltip("1 " + nameOfFirstCurrency + " = " + text + " " + nameOfSecondCurrency);
        Tooltip.install(getNodeByIndex(i,j),tooltip);
    }

    public Node getNodeByIndex(int row, int column){
        Node result = null;
        ObservableList<Node> children = exchangeGridPane.getChildren();
        for (Node node : children) {
            if(exchangeGridPane.getRowIndex(node) == row && exchangeGridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}
