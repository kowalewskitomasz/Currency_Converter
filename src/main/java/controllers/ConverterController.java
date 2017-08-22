package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    @FXML
    private LineChart lineChart;
    @FXML
    private NumberAxis numberAxis;

    ArrayOfExchangeRatesTable arrayOfExchangeRatesTable;
    int howManyDaysOnChart = 90;

    public void initialize() {
        CurrencyData currencyData = new CurrencyData();
        arrayOfExchangeRatesTable = currencyData.getTableAForAllCurrencies();

        setupComboBoxes();

        firstCurrencyTextField.setTextFormatter(new TextFormatter<Object>(filter));
        firstCurrencyTextField.textProperty().addListener(
                (observable, oldValue, newValue) -> getTextFieldValueAndCalculate(newValue)
        );

        collectDataForChart(howManyDaysOnChart);
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

    public ObservableList<String> parseRatelistToStringList(List<Rate> rateList) {
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add("PLN" + " - " + "polski złoty");
        for (Rate element : rateList) {
            stringList.add(element.getCode() + " - " + element.getCurrency());
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        return observableList;
    }

    public void setupComboBoxes() {
        ObservableList<String> list = parseRatelistToStringList(arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList());
        firstCurrency.setItems(list);
        firstCurrency.getSelectionModel().select(0);
        secondCurrency.setItems(list);
        secondCurrency.getSelectionModel().select(8);
        updatedAt.setText("Updated on: " + arrayOfExchangeRatesTable.getExchangeRatesTable().getEffectiveDate() + " via api.nbp.pl");
    }


    public void getTextFieldValueAndCalculate(String firstCurrencyAmountString) {
        if (!firstCurrencyAmountString.equals("")) {
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
                if (t.getText().matches("[^0-9]"))
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
        collectDataForChart(howManyDaysOnChart);
    }

    public void collectDataForChart(int howManyDaysAgo) {
        String firstCurrencyValue = firstCurrency.getValue().substring(0, 3);
        String secondCurrencyValue = secondCurrency.getValue().substring(0, 3);

        CurrencyData currencyData = new CurrencyData();

        ExchangeRatesSeries exchangeRatesSeries1 = new ExchangeRatesSeries();
        ExchangeRatesSeries exchangeRatesSeries2 = new ExchangeRatesSeries();

        String todaysDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String thisManyDaysAgo = LocalDate.now().minusDays(howManyDaysAgo).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (!firstCurrencyValue.equals("PLN")) {
            exchangeRatesSeries1 = currencyData.getInfoForChart(firstCurrencyValue + "/" + thisManyDaysAgo + "/" + todaysDate);
        }
        if (!secondCurrencyValue.equals("PLN")) {
            exchangeRatesSeries2 = currencyData.getInfoForChart(secondCurrencyValue + "/" + thisManyDaysAgo + "/" + todaysDate);
        }

        if (exchangeRatesSeries1.getRates() == null && exchangeRatesSeries2.getRates() != null) {
            exchangeRatesSeries1 = currencyData.getInfoForChart(secondCurrencyValue + "/" + thisManyDaysAgo + "/" + todaysDate);
            exchangeRatesSeries1.setCode("PLN");
            for (Rate element : exchangeRatesSeries1.getRates().getRateList()) {
                element.setMid(1.0);
            }
        }
        if (exchangeRatesSeries2.getRates() == null && exchangeRatesSeries1.getRates() != null) {
            exchangeRatesSeries2 = currencyData.getInfoForChart(firstCurrencyValue + "/" + thisManyDaysAgo + "/" + todaysDate);
            exchangeRatesSeries2.setCode("PLN");
            for (Rate element : exchangeRatesSeries2.getRates().getRateList()) {
                element.setMid(1.0);
            }
        }

        if(exchangeRatesSeries2.getRates() == null && exchangeRatesSeries2.getRates() == null){
            exchangeRatesSeries1 = currencyData.getInfoForChart("EUR" + "/" + thisManyDaysAgo + "/" + todaysDate);
            exchangeRatesSeries2 = currencyData.getInfoForChart("EUR" + "/" + thisManyDaysAgo + "/" + todaysDate);
        }

        drawChart(exchangeRatesSeries1, exchangeRatesSeries2);
    }

    private void drawChart(ExchangeRatesSeries exchangeRatesSeries1, ExchangeRatesSeries exchangeRatesSeries2) {
        double max = 0;
        double min = 100;
        XYChart.Series<String, Double> series = new XYChart.Series();

        List<Rate> rateList1 = exchangeRatesSeries1.getRates().getRateList();
        List<Rate> rateList2 = exchangeRatesSeries2.getRates().getRateList();

        series.setName(exchangeRatesSeries1.getCode() + " to " + exchangeRatesSeries2.getCode() + " Chart");
        for (int i = 0; i < rateList1.size(); i++) {
            rateList1.get(i).setMid(rateList1.get(i).getMid() / rateList2.get(i).getMid());

            if (rateList1.get(i).getMid() > max) {
                max = rateList1.get(i).getMid();
            }
            if (rateList1.get(i).getMid() < min) {
                min = rateList1.get(i).getMid();
            }
            series.getData().add(new XYChart.Data(rateList1.get(i).getEffectiveDate(), rateList1.get(i).getMid()));
        }

        numberAxis.setAutoRanging(false);
        numberAxis.setLowerBound(min);
        numberAxis.setUpperBound(max);

        lineChart.getData().retainAll();
        lineChart.getData().add(series);

        for (XYChart.Data<String, Double> o : series.getData()) {
            //setTooltip(o.getNode(), "Day: " + o.getXValue() + " Value: " + o.getYValue().toString().substring(0, Math.min(o.getYValue().toString().length(), 7)));
            setTooltip(o.getNode(), "Day: " + o.getXValue() + " Value: 1 " + exchangeRatesSeries1.getCode() + " = " + o.getYValue().toString().substring(0, Math.min(o.getYValue().toString().length(), 7)) + " " + exchangeRatesSeries2.getCode());
        }
    }

    private void setTooltip(Node node, String tooltip) {
        Tooltip tooltip1 = new Tooltip(tooltip);
        Tooltip.install(node, tooltip1);
    }

    @FXML
    private void chartWeek() {
        howManyDaysOnChart = 7;
        collectDataForChart(7);
    }

    @FXML
    private void chartMonth() {
        howManyDaysOnChart = 30;
        collectDataForChart(30);
    }

    @FXML
    private void chart3Month() {
        howManyDaysOnChart = 90;
        collectDataForChart(90);
    }

    @FXML
    private void chart6Month() {
        howManyDaysOnChart = 180;
        collectDataForChart(180);
    }

    @FXML
    private void chartYear() {
        howManyDaysOnChart = 360;
        collectDataForChart(360);
    }

}
