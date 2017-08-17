package model;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Tomek on 2017-08-17.
 */
public class Rates {
    List <Rate> rateList;

    @XmlElement(name="Rate")
    public List<Rate> getRateList() {
        return rateList;
    }

    public void setRateList(List<Rate> rateList) {
        this.rateList = rateList;
    }
}
