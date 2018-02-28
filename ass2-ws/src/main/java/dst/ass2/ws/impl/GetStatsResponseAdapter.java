package dst.ass2.ws.impl;

import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.dto.StatisticsDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * Created by amra.
 */
public class GetStatsResponseAdapter extends XmlAdapter<String, IGetStatsResponse> {


    // ovdje kao xml, xml izcitati i promijenuti u j.objekat
    @Override
    public IGetStatsResponse unmarshal(String v) throws Exception {

        JAXBContext jaxbContext = JAXBContext.newInstance(StatisticsDTO.class);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        return new GetStatsResponse((StatisticsDTO) um.unmarshal(new ByteArrayInputStream(v.getBytes())));
    }

    //j.objekat u string i opet nazad da mozemo kroz mrezu slati
    @Override
    public String marshal(IGetStatsResponse v) throws Exception {

        JAXBContext context = JAXBContext.newInstance(StatisticsDTO.class);
        Marshaller m = context.createMarshaller();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        m.marshal(v.getStatistics(), baos);

        return baos.toString();
    }
}
