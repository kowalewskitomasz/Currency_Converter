package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.ArrayOfExchangeRatesTable;
import model.CurrencyData;
import model.Rate;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    @FXML
    private Label updatedAt;

    ArrayOfExchangeRatesTable arrayOfExchangeRatesTable;


    public void initExchanger(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/exchange.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            scene.getStylesheets().add("styles");

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void initialize(){
        CurrencyData currencyData = new CurrencyData();
        arrayOfExchangeRatesTable = currencyData.getTableAForAllCurrencies();
        System.out.println("stop");

        populateTableView();
        calculateExchangeRate();
    }


    public void backButtonAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/main.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void customizeButtonAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        CustomizeController customizeController = new CustomizeController();
        customizeController.initCustomize(stage);
    }

    public void populateTableView(){
        //List<Rate> rateList = arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList();
        ObservableList<String> list = parseRatelistToStringList(arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList());
        updatedAt.setText("Updated on: " + arrayOfExchangeRatesTable.getExchangeRatesTable().getEffectiveDate() + " via api.nbp.pl");
        exchangeGridPane.setPadding(new Insets(10, 10, 10, 10));
        int verticalColumn = 2;
        int horizontalColumn = 2;

        setBoundaryFrame(list, "PLN", 0, 1);
        setBoundaryFrame(list, "PLN", 1, 0);

        for(String element: list){
            if(element.equals("GBP") || element.equals("USD") || element.equals("EUR") || element.equals("CHF") || element.equals("JPY") || element.equals("DKK") || element.equals("NOK") || element.equals("SEK") || element.equals("RUB")) {

                setBoundaryFrame(list, element, 0, verticalColumn);
                verticalColumn++;

                setBoundaryFrame(list, element, horizontalColumn, 0);
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

    public void setBoundaryFrame(ObservableList<String> list, String selection, int columnIndex, int rowIndex){
        ComboBox comboBox = new ComboBox();
        comboBox.getStyleClass().add("combo-box-base");
        comboBox.setPadding(new Insets(10,5,10,5));
        comboBox.setItems(list);
        comboBox.setOnAction(this::comboBoxAction);
        int i = 0;
        for(String element : list){
            if(element.equals(selection)){
                comboBox.getSelectionModel().select(i);
            }
            i++;
        }
        exchangeGridPane.add(comboBox,columnIndex,rowIndex);
    }

    public void comboBoxAction(Event event){
        calculateExchangeRate();
    }

    public ObservableList<String> parseRatelistToStringList(List<Rate> rateList) {
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add("PLN");
        for (Rate element : rateList) {
            stringList.add(element.getCode());
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        return observableList;
    }


    public void calculateExchangeRate(){
        List<Rate> rateList = arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList();

        double currencyOne = 1;
        double currencyTwo = 1;
        for(int i = 0 ; i <= 10 ; i++){
            ComboBox combo1 = (ComboBox) getNodeByIndex(i, 0);
            if(combo1 != null) {
                for(int j = 0 ; j <= 10 ; j++){
                    ComboBox combo2 = (ComboBox) getNodeByIndex(0, j);
                    if(combo2 != null){
                        String nameOfFirstCurrency = "";
                        String nameOfSecondCurrency = "";

                        for(Rate rate: rateList){
                            if(rate.getCode().equals(combo1.getValue())){
                                currencyOne = rate.getMid();
                                nameOfFirstCurrency = rate.getCode();
                            }
                            if(rate.getCode().equals(combo2.getValue())){
                                currencyTwo = rate.getMid();
                                nameOfSecondCurrency = rate.getCode();
                            }
                        }

                        double result = currencyOne/currencyTwo;
                        DecimalFormat decimalFormat = new DecimalFormat("#.####");
                        decimalFormat.setRoundingMode(RoundingMode.CEILING);
                        String resultString = decimalFormat.format(result);

                        setLabelStyle(resultString, j , i);
                        //unsetTooltips(j,i);
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

//    private void unsetTooltips(int j, int i){
//        Tooltip tooltip = new Tooltip();
//        Tooltip.uninstall(getNodeByIndex(i,j),tooltip);
//    }

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
