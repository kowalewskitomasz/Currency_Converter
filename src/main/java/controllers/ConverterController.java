package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ArrayOfExchangeRatesTable;
import model.CurrencyData;
import model.ExchangeRatesSeries;
import model.Rate;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Created by Tomek on 2017-08-13.
 */
public class ConverterController {

    @FXML
    private Button backButton;
    @FXML
    private ComboBox<String> firstCurrency;
    @FXML
    private ComboBox<String> secondCurrency;
    @FXML
    private TextField firstCurrencyTextField;
    @FXML
    private TextField secondCurrencyTextField;
    @FXML
    private Label updatedAt;

    ArrayOfExchangeRatesTable arrayOfExchangeRatesTable;

    public void initialize(){
        CurrencyData currencyData = new CurrencyData();
        arrayOfExchangeRatesTable = currencyData.getTableAForAllCurrencies();

        setupComboBoxes();

        firstCurrencyTextField.setTextFormatter(new TextFormatter<Object>(filter));
        firstCurrencyTextField.textProperty().addListener(
                (observable, oldValue, newValue) -> getTextFieldValueAndCalculate(newValue)
        );

        collectDataForChart();
        //TODO zbieranie danych działa, wymyśl jak zrobić z nich sensowne wykresy
    }

    public void initConverter(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/converter.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);

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

    public ObservableList<String> parseRatelistToStringList(List <Rate> rateList){
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add("PLN" + " - " + "polski złoty");
        for (Rate element: rateList){
            stringList.add(element.getCode() + " - " + element.getCurrency());
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        return observableList;
    }

    public void setupComboBoxes(){
        ObservableList<String> list = parseRatelistToStringList(arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList());
        firstCurrency.setItems(list);
        firstCurrency.getSelectionModel().select(0);
        secondCurrency.setItems(list);
        secondCurrency.getSelectionModel().select(8);
        updatedAt.setText("Updated on: " + arrayOfExchangeRatesTable.getExchangeRatesTable().getEffectiveDate() + " via api.nbp.pl");
    }


    public void getTextFieldValueAndCalculate(String firstCurrencyAmountString) {
        if(!firstCurrencyAmountString.equals("")) {
            String firstCurrencyValue = firstCurrency.getValue().substring(0, 3);
            String secondCurrencyValue = secondCurrency.getValue().substring(0, 3);
            double firstCurrencyRate = 1;
            double secondCurrencyRate = 1;
            double firstCurrencyAmount = Double.valueOf(firstCurrencyAmountString);
            List<Rate> rateList = arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList();

            for (Rate element : rateList) {
                if (element.getCode().equals(firstCurrencyValue)) {
                    firstCurrencyRate = element.getMid();
                }
                if (element.getCode().equals(secondCurrencyValue)) {
                    secondCurrencyRate = element.getMid();
                }
            }

            Double result = (firstCurrencyRate / secondCurrencyRate) * firstCurrencyAmount;
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);
            String resultString = decimalFormat.format(result);
            secondCurrencyTextField.setText(resultString);

            System.out.println(firstCurrencyAmount);
        }
    }


    public void swapCurrencies(ActionEvent actionEvent) {
        int first = firstCurrency.getSelectionModel().getSelectedIndex();
        int second = secondCurrency.getSelectionModel().getSelectedIndex();

        secondCurrency.getSelectionModel().select(first);
        firstCurrency.getSelectionModel().select(second);

    }

    UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {

        @Override
        public TextFormatter.Change apply(TextFormatter.Change t) {

            if (t.isReplaced())
                if(t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


            if (t.isAdded()) {
                if (t.getControlText().contains(".")) {
                    if (t.getText().matches("[^0-9]")) {
                        t.setText("");
                    }
                } else if (t.getText().matches("[^0-9.]")) {
                    t.setText("");
                }
            }

            return t;
        }
    };

    public void comboBoxAction(ActionEvent actionEvent) {
        getTextFieldValueAndCalculate(firstCurrencyTextField.getText());
    }

    public void collectDataForChart(){
        String firstCurrencyValue = firstCurrency.getValue().substring(0,3);
        String secondCurrencyValue = secondCurrency.getValue().substring(0,3);

        String todaysDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateSixMonthsAgo = LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            CurrencyData currencyData = new CurrencyData();
        if(!firstCurrencyValue.equals("PLN")) {
            ExchangeRatesSeries exchangeRatesSeries1 = currencyData.getInfoForChart(firstCurrencyValue + "/" + dateSixMonthsAgo + "/" + todaysDate);
            System.out.println("zebrane dane do czarta dla waluty 1");
        }
        if(!secondCurrencyValue.equals("PLN")) {
            ExchangeRatesSeries exchangeRatesSeries2 = currencyData.getInfoForChart(secondCurrencyValue + "/" + dateSixMonthsAgo + "/" + todaysDate);
            System.out.println("zebrane dane do czarta dla waluty 2");
        }
    }


}
