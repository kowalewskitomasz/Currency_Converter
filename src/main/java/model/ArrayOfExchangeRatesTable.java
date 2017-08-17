package model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Tomek on 2017-08-17.
 */
@XmlRootElement(name="ArrayOfExchangeRatesTable")
public class ArrayOfExchangeRatesTable {
    ExchangeRatesTable exchangeRatesTable;

    public ExchangeRatesTable getExchangeRatesTable() {
        return exchangeRatesTable;
    }

    public void setExchangeRatesTable(ExchangeRatesTable exchangeRatesTable) {
        this.exchangeRatesTable = exchangeRatesTable;
    }


}
