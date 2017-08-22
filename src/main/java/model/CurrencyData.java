package model;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Tomek on 2017-08-14.
 */
public class CurrencyData {

    public ArrayOfExchangeRatesTable arrayOfExchangeRatesTable;
    public ExchangeRatesSeries exchangeRatesSeries;

    public ArrayOfExchangeRatesTable getTableAForAllCurrencies(){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://api.nbp.pl/api/exchangerates/tables");
        WebTarget secondTarget = target.path("A");

        Invocation.Builder invocationBuilder = secondTarget.request(MediaType.APPLICATION_XML_TYPE);
        Response response = invocationBuilder.get();
        File xml = response.readEntity(File.class);
        return unmarshallerJAXBForTableA(xml);
    }

    public ExchangeRatesSeries getInfoForChart(String currencyAndDates){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://api.nbp.pl/api/exchangerates/rates/A");
        WebTarget secondTarget = target.path(currencyAndDates);

        Invocation.Builder invocationBuilder = secondTarget.request(MediaType.APPLICATION_XML_TYPE);
        Response response = invocationBuilder.get();
        File xml = response.readEntity(File.class);
        return unmarshallerJAXBForCharts(xml);

    }


    public ArrayOfExchangeRatesTable unmarshallerJAXBForTableA(File xml){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ArrayOfExchangeRatesTable.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            arrayOfExchangeRatesTable = (ArrayOfExchangeRatesTable) unmarshaller.unmarshal(xml);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        finally {
            Rate ratePLN = new Rate();
            ratePLN.setMid(1);
            ratePLN.setCode("PLN");
            ratePLN.setCurrency("polski złoty");

            arrayOfExchangeRatesTable.getExchangeRatesTable().getRates().getRateList().add(ratePLN);

            return arrayOfExchangeRatesTable;
        }
    }

    public ExchangeRatesSeries unmarshallerJAXBForCharts(File xml){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRatesSeries.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            exchangeRatesSeries = (ExchangeRatesSeries) unmarshaller.unmarshal(xml);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        finally {

            return exchangeRatesSeries;
        }
    }

}
