package dst.ass2.ws.session;

import dst.ass1.jpa.dao.impl.MOSPlatformDAO;
import dst.ass1.jpa.model.*;
import dst.ass2.ws.Constants;
import dst.ass2.ws.IGetStatsRequest;
import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.dto.StatisticsDTO;
import dst.ass2.ws.dto.StreamingDTO;
import dst.ass2.ws.impl.GetStatsRequestAdapter;
import dst.ass2.ws.impl.GetStatsResponse;
import dst.ass2.ws.impl.GetStatsResponseAdapter;
import dst.ass2.ws.session.exception.WebServiceException;
import dst.ass2.ws.session.interfaces.IEventStatisticsBean;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;

@Remote
@Stateless
//https://jax-ws.java.net/jax-ws-21-ea3/docs/wsaddressing.html
//enables ws addressing
@Addressing
//http://java.boot.by/ocewsd6-guide/ch01s02.html
//samo imena da napravimo xml file
@WebService(
        targetNamespace = Constants.NAMESPACE,
        serviceName = Constants.SERVICE_NAME,
        portName = Constants.PORT_NAME,
        name = Constants.NAME)

public class EventStatisticsBean implements IEventStatisticsBean {

    @PersistenceContext
    private EntityManager entityManager;


    @Action(input  = "http://example.com/input",
            output = "http://example.com/output",
            fault  = { @FaultAction(className = WebServiceException.class,
                    value = "http://example.com/fault") })

    @WebResult
    @WebMethod(operationName="getStatisticsForPlatform")
    public @XmlJavaTypeAdapter(GetStatsResponseAdapter.class) IGetStatsResponse getStatisticsForPlatform(
            @XmlJavaTypeAdapter(GetStatsRequestAdapter.class) @WebParam IGetStatsRequest request,
            @WebParam(partName = "name", header = true) String name) throws WebServiceException {

        if (name == null || request == null) throw new WebServiceException("no name or no request given");


        //object gdje daten ubacivamo
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setName(name);
        statisticsDTO.setStreamings(new ArrayList<StreamingDTO>());

        //platform nije nadjena
        if (new MOSPlatformDAO(entityManager).findByName(name).size() == 0) {
            throw new WebServiceException("no platform found");
        }

        IMOSPlatform mosPlatform = new MOSPlatformDAO(entityManager).findByName(name).get(0);
        ArrayList<Long> streamingIDs = new ArrayList<>();

        for (IStreamingServer streamingServer  : mosPlatform.getStreamingServers()) {

            for (IUplink uplink : streamingServer.getUplinks()) {

                for (IEventStreaming eventStreaming : uplink.getEventStreamings()) {
                    //samo maximale broj streamova i prekini nakon odredenog broja streamova
                    Long id = eventStreaming.getId();

                    if (eventStreaming.getStatus() == EventStatus.FINISHED && !streamingIDs.contains(id)) {
                        statisticsDTO.addStreaming(eventStreaming);
                        streamingIDs.add(id);

                    }

                    if (request.getMaxStreamings() == streamingIDs.size()){
                        return new GetStatsResponse(statisticsDTO);
                    }
                }

            }

        }

		return new GetStatsResponse(statisticsDTO);
	}

}
