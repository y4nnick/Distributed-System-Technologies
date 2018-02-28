package dst.ass2.ws;

import dst.ass2.ws.util.WSUtils;

/**
 * This class defines constants for the Web service implementation.
 */
public class Constants {

	/**
	 * Target namespace of the Web service. (corresponds to /definitions/@targetNamespace in the WSDL)
	 */
	public static final String NAMESPACE = "http://dst.infosys.tuwien.ac.at/statistics";
	/**
	 * Service name of the Web service. (corresponds to //service/@name in the WSDL)
	 */
	public static final String SERVICE_NAME = "StatisticsService";
	/**
	 * Name of the Web service port. (corresponds to //service/port/@name in the WSDL)
	 */
	public static final String PORT_NAME = "servicePort";
	/**
	 * Name of the Web service binding. (corresponds to //binding/@name in the WSDL)
	 */
	public static final String BINDING_NAME = SERVICE_NAME + "SoapBinding";
	/**
	 * Name of the Web service.
	 */
	public static final String NAME = "service";
	/**
	 * URL of the Web service. (corresponds to //service/port/soap:address/@location in the WSDL)
	 */
	public static final String SERVICE_URL = "http://localhost:4204/" + WSUtils.MODULE_NAME + "/" + SERVICE_NAME + "/" + NAME;
	
	/**
	 * WSDL URL of the deployed Web service.
	 */
	public static final String SERVICE_WSDL_URL = SERVICE_URL + "?wsdl";

	
	/* OPERATION NAMES */

	public static final String OP_GET_STATS = "getStatisticsForPlatform";

}
