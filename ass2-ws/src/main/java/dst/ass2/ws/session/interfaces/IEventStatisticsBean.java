package dst.ass2.ws.session.interfaces;

import dst.ass2.ws.Constants;
import dst.ass2.ws.IGetStatsRequest;
import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.impl.GetStatsRequestAdapter;
import dst.ass2.ws.impl.GetStatsResponseAdapter;
import dst.ass2.ws.session.exception.WebServiceException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This is the interface of the statistics Web Service.
 */
@WebService(
		targetNamespace = Constants.NAMESPACE,
		serviceName = Constants.SERVICE_NAME,
		portName = Constants.PORT_NAME,
		name = Constants.NAME)


public interface IEventStatisticsBean {

	/**
	 * Get statistics for a given platform.
	 * @param request      The request object with parameters
	 * @param platformName The name of the platform
	 * @return statistics for the platform with the specified name.
	 */
    @WebMethod(operationName="getStatisticsForPlatform")
    @XmlJavaTypeAdapter(GetStatsResponseAdapter.class) IGetStatsResponse getStatisticsForPlatform(
            @XmlJavaTypeAdapter(GetStatsRequestAdapter.class) @WebParam IGetStatsRequest request,
            @WebParam(partName = "name", header = true) String platformName) throws WebServiceException;
}
