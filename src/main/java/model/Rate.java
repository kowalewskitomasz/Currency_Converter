package model;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Tomek on 2017-08-17.
 */
public class Rate {
    String currency;
    String code;
    double mid;

    @XmlElement(name="Currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @XmlElement(name="Code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(name="Mid")
    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

}
