package model;

import javax.annotation.PostConstruct;
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
    //private Client client;
    //private WebTarget target;

    @PostConstruct
    public void init(){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://api.nbp.pl/api/exchangerates/tables");
        WebTarget secondTarget = target.path("A");
        WebTarget thirdTarget = target.path("B/last");
        //WebTarget formatTarget = secondTarget.queryParam("format", "json");

        Invocation.Builder invocationBuilder = secondTarget.request(MediaType.APPLICATION_XML_TYPE);
        Response response = invocationBuilder.get();
        File elo = response.readEntity(File.class);
        unmarshallerJAXB(elo);
        //invocationBuilder.header()
    }

    public void unmarshallerJAXB(File xml){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ArrayOfExchangeRatesTable.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ArrayOfExchangeRatesTable arrayOfExchangeRatesTable = (ArrayOfExchangeRatesTable) unmarshaller.unmarshal(xml);
            System.out.println("elo");

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
