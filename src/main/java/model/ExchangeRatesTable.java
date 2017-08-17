package model;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Tomek on 2017-08-15.
 */
public class ExchangeRatesTable {

    String table;
    String no;
    String effectiveDate;
    Rates rates;

    @XmlElement(name="Table")
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @XmlElement(name="No")
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @XmlElement(name="EffectiveDate")
    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @XmlElement(name="Rates")
    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }
}
