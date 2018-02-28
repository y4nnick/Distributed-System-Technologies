package dst.ass1.jpa.util;

public class Constants {

	/* TYPES (CLASSES) */
	public static final String T_UPLINK = "Uplink";
	public static final String T_STREAMINGSERVER = "StreamingServer";
	public static final String T_MOSPLATFORM = "MOSPlatform";
	public static final String T_EVENTMASTER = "EventMaster";
	public static final String T_MEMBERSHIP = "Membership";
	public static final String T_EVENT = "Event";
	public static final String T_MODERATOR = "Moderator";
	public static final String T_EVENTSTREAMING = "EventStreaming";
	public static final String T_METADATA = "Metadata";
	public static final String T_PERSON = "Person";

	/* IDs (FOREIGN KEYS) */
	public static final String I_STREAMINGSERVER = T_STREAMINGSERVER.toLowerCase() + "_id";
	public static final String I_MOSPLATFORM = T_MOSPLATFORM.toLowerCase() + "_id";
	public static final String I_MODERATOR = T_MODERATOR.toLowerCase() + "_id";
	public static final String I_EVENTMASTER = T_EVENTMASTER.toLowerCase() + "_id";
	public static final String I_MEMBERSHIP = T_MEMBERSHIP.toLowerCase() + "_id";
	public static final String I_UPLINK = T_UPLINK.toLowerCase() + "_id";
	public static final String I_UPLINKS = T_UPLINK.toLowerCase() + "s_id";
	public static final String I_EVENTSTREAMING = T_EVENTSTREAMING.toLowerCase() + "_id";
	public static final String I_EVENTSTREAMINGS = T_EVENTSTREAMING.toLowerCase() + "s_id";
	public static final String I_EVENT = T_EVENT.toLowerCase() + "_id";
	public static final String I_METADATA = T_METADATA + "_id";

	/* MEMBER ATTRIBUTES */
	public static final String M_VIEWERCAPACITY = "viewerCapacity";
	public static final String M_COSTSPERSTREAMINGMINUTE = "costsPerStreamingMinute";
	public static final String M_LASTUPDATE = "lastUpdate";
	public static final String M_LASTMAINTENANCE = "lastMaintenance";
	public static final String M_NEXTMMAINTENANCE = "nextMaintenance";
	public static final String M_ACTIVATED = "activated";
	public static final String M_ISPAID = "isPaid";
	public static final String M_MODERATOR = "moderator";
	public static final String M_SETTINGS_ORDER = "settings_ORDER";
	public static final String M_REGION = "region";
	public static final String M_GAME = "game";
	public static final String M_EVENTMASTERNAME = "eventMasterName";
	public static final String M_PASSWORD = "password";
	public static final String M_COMPOSEDOF = "composedOf";
	public static final String M_PARTOF = "partOf";
	public static final String M_ACCOUNT = "accountNo";
	public static final String M_BANKCODE="bankCode";
	
	/* ASSOCIATION NAMES (FOR QUERIES) */
	public static final String A_MOSPLATFORM = "mosPlatform";
	public static final String A_STREAMING = "eventStreaming";
	public static final String A_STREAMINGS = "eventStreamings";
	public static final String A_UPLINKS = "uplinks";
	public static final String A_STREAMINGSERVER = "streamingServer";
	public static final String A_STREAMINGSERVERS = "streamingServers";
	public static final String A_MEMBERSHIPS = "memberships";
	public static final String A_EVENT = "event";
	public static final String A_EVENTMASTER = "eventmaster";
	public static final String A_EVENTS = "events";
	public static final String A_METADATA = "metadata";
	public static final String A_COMPOSEDOF = "composedOf";



	/* NAMED QUERIES */
	public static final String Q_ALLFINISHEDEVENTS = "allFinishedEvents";
	public static final String Q_STREAMINGSERVERSOFMODERATOR = "streamingserversOfModerator";
	public static final String Q_EVENTMASTERSSWITHACTIVEMEMBERSHIP = "eventmastersWithActiveMembership";
	public static final String Q_MOSTACTIVEEVENTMASTER = "mostActiveEventmaster";
	
	/* JOIN TABLES */
	public static final String J_STREAMING_UPLINK = "streaming_uplink";
	public static final String J_METADATA_SETTINGS = "Metadata_settings";
	public static final String J_STREAMINGSERVER_COMPOSEDOF = "composed_of";

	/* NoSQL part */
	public static final String MONGO_DB_NAME = "dst";
	public static final String COLL_EVENTDATA = "EventData";
	public static final String PROP_EVENTFINISHED = "event_finished";

}
