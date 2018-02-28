package dst.ass1.jpa.util.test;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IStreamingServer;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.model.ModelFactory;

public class TestData {

	public static final String N_ENT1_1 = "platform1";
	public static final String N_ENT1_2 = "platform2";
	public static final String N_ENT2_1 = "streamingServer1";
	public static final String N_ENT2_2 = "streamingServer2";
	public static final String N_ENT2_3 = "streamingServer3";
	public static final String N_ENT3_1 = "uplink1";
	public static final String N_ENT3_2 = "uplink2";
	public static final String N_ENT3_3 = "uplink3";
	public static final String N_ENT3_4 = "uplink4";
	public static final String N_ENT3_5 = "uplink5";
	public static final String N_ENT6_1 = "game1";
	public static final String N_ENT6_2 = "game2";
	public static final String N_ENT6_3 = "game3";
	public static final String N_ENT6_4 = "game4";
	public static final String N_ENT6_1_S1 = "setting1";
	public static final String N_ENT6_1_S2 = "setting2";
	public static final String N_ENT6_1_S3 = "setting3";
	public static final String N_ENT6_2_S1 = "setting4";
	public static final String N_ENT6_3_S1 = "setting5";
	public static final String N_ENT6_4_S1 = "setting6";
	public static final String N_ENT7_1 = "Alex";
	public static final String N_ENT7_2 = "Alexandra";
	public static final String N_ENT7_3 = "Alexander";
	public static final String N_ENT8_1 = "EventMaster1";
	public static final String PW_ENT8_1 = "pw1";
	public static final String N_ENT8_2 = "EventMaster2";
	public static final String PW_ENT8_2 = "pw2";

	public Long entity8_1Id;
	public Long entity8_2Id;

	public Long entity2_1Id;
	public Long entity2_2Id;

	public Long entity3_1Id;
	public Long entity3_2Id;
	public Long entity3_3Id;

	public Long entity4_1Id;
	public Long entity4_2Id;
	public Long entity4_3Id;
	public Long entity4_4Id;

	public Long entity5_1Id;
	public Long entity5_2Id;
	public Long entity5_3Id;
	public Long entity5_4Id;

	public Long entity6_1Id;
	public Long entity6_2Id;
	public Long entity6_3Id;
	public Long entity6_4Id;

	public Long entity7_1Id;
	public Long entity7_2Id;
	public Long entity7_3Id;

	protected EntityManager em;
	private ModelFactory modelFactory;

	public TestData(EntityManager em) {
		this.em = em;
		this.modelFactory = new ModelFactory();
	}

	public void insertTestData() throws NoSuchAlgorithmException {
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		insertTestData_withoutTransaction();

		tx.commit();
	}

	public void insertTestData_withoutTransaction()
			throws NoSuchAlgorithmException {
		
		// Addresses
		IAddress address1 = modelFactory.createAddress();
		IAddress address2 = modelFactory.createAddress();
		IAddress address3 = modelFactory.createAddress();
		IAddress address4 = modelFactory.createAddress();
		IAddress address5 = modelFactory.createAddress();

		address1.setCity("city1");
		address1.setStreet("street1");
		address1.setZipCode("zip1");

		address2.setCity("city2");
		address2.setStreet("street2");
		address2.setZipCode("zip2");

		address3.setCity("city3");
		address3.setStreet("street3");
		address3.setZipCode("zip3");

		address4.setCity("city4");
		address4.setStreet("street4");
		address4.setZipCode("zip4");

		address5.setCity("city5");
		address5.setStreet("street5");
		address5.setZipCode("zip5");

		IModerator ent7_1 = modelFactory.createModerator();
		IModerator ent7_2 = modelFactory.createModerator();
		IModerator ent7_3 = modelFactory.createModerator();

		ent7_1.setFirstName(N_ENT7_1);
		ent7_1.setLastName(N_ENT7_1);
		ent7_1.setAddress(address1);

		ent7_2.setFirstName(N_ENT7_2);
		ent7_2.setLastName(N_ENT7_2);
		ent7_2.setAddress(address2);

		ent7_3.setFirstName(N_ENT7_3);
		ent7_3.setLastName(N_ENT7_3);
		ent7_3.setAddress(address5);

		// Entity #8
		MessageDigest md = MessageDigest.getInstance("MD5");

		IEventMaster ent8_1 = modelFactory.createEventMaster();
		ent8_1.setFirstName("first1");
		ent8_1.setLastName("last1");
		ent8_1.setAddress(address3);
		ent8_1.setAccountNo("account1");
		ent8_1.setEventMasterName(N_ENT8_1);
		ent8_1.setBankCode("bank1");
		ent8_1.setPassword(md.digest(PW_ENT8_1.getBytes()));

		IEventMaster ent8_2 = modelFactory.createEventMaster();
		ent8_2.setFirstName("first2");
		ent8_2.setLastName("last2");
		ent8_2.setAddress(address4);
		ent8_2.setAccountNo("account2");
		ent8_2.setEventMasterName(N_ENT8_2);
		ent8_2.setBankCode("bank2");
		ent8_2.setPassword(md.digest(PW_ENT8_2.getBytes()));

		em.persist(ent7_1);
		em.persist(ent7_2);
		em.persist(ent7_3);
		em.persist(ent8_1);
		em.persist(ent8_2);

		// Entity #3
		IUplink ent3_1 = modelFactory.createUplink();
		ent3_1.setName(N_ENT3_1);
		ent3_1.setViewerCapacity(40);
		ent3_1.setRegion("AUT-VIE@1010");
		ent3_1.setActivated(new Date(0));
		ent3_1.setLastUpdate(new Date(0));

		IUplink ent3_2 = modelFactory.createUplink();
		ent3_2.setName(N_ENT3_2);
		ent3_2.setViewerCapacity(60);
		ent3_2.setRegion("AUT-VIE@1020");
		ent3_2.setActivated(new Date(0));
		ent3_2.setLastUpdate(new Date(0));

		IUplink ent3_3 = modelFactory.createUplink();
		ent3_3.setName(N_ENT3_3);
		ent3_3.setViewerCapacity(80);
		ent3_3.setRegion("AUT-VIE@1030");
		ent3_3.setActivated(new Date(0));
		ent3_3.setLastUpdate(new Date(0));

		IUplink ent3_4 = modelFactory.createUplink();
		ent3_4.setName(N_ENT3_4);
		ent3_4.setViewerCapacity(40);
		ent3_4.setRegion("AUT-IBK@6020");
		ent3_4.setActivated(new Date(0));
		ent3_4.setLastUpdate(new Date(0));
		
		IUplink ent3_5 = modelFactory.createUplink();
		ent3_5.setName(N_ENT3_5);
		ent3_5.setViewerCapacity(80);
		ent3_5.setRegion("AUT-IBK@6060");
		ent3_5.setActivated(new Date(0));
		ent3_5.setLastUpdate(new Date(0));

		// Entity #2
		IStreamingServer ent2_1 = modelFactory.createStreamingServer();
		ent2_1.setModerator(ent7_1);
		ent2_1.setName(N_ENT2_1);
		ent2_1.setLastMaintenance(new Date(0));
		ent2_1.setNextMaintenance(new Date(0));

		IStreamingServer ent2_2 = modelFactory.createStreamingServer();
		ent2_2.setModerator(ent7_2);
		ent2_2.setName(N_ENT2_2);
		ent2_2.setLastMaintenance(new Date(1));
		ent2_2.setNextMaintenance(new Date(1));

		IStreamingServer ent2_3 = modelFactory.createStreamingServer();
		ent2_3.setModerator(ent7_3);
		ent2_3.setName(N_ENT2_3);
		ent2_3.setLastMaintenance(new Date(2));
		ent2_3.setNextMaintenance(new Date(2));

		ent2_1.addComposedOf(ent2_2);
		ent2_2.addPartOf(ent2_1);

		ent7_1.addAdvisedStreamingServers(ent2_1);
		ent7_2.addAdvisedStreamingServers(ent2_2);
		ent7_3.addAdvisedStreamingServers(ent2_3);

		ent2_1.addUplink(ent3_1);
		ent2_1.addUplink(ent3_2);
		ent3_1.setStreamingServer(ent2_1);
		ent3_2.setStreamingServer(ent2_1);

		ent2_2.addUplink(ent3_3);
		ent3_3.setStreamingServer(ent2_2);

		ent2_3.addUplink(ent3_4);
		ent2_3.addUplink(ent3_5);
		ent3_4.setStreamingServer(ent2_3);
		ent3_5.setStreamingServer(ent2_3);

		// Entity #1
		IMOSPlatform ent1_1 = modelFactory.createPlatform();
		ent1_1.setName(N_ENT1_1);
		ent1_1.setUrl("vienna");
		ent1_1.setCostsPerStreamingMinute(new BigDecimal(5));

		ent1_1.addStreamingServer(ent2_1);
		ent1_1.addStreamingServer(ent2_2);

		ent2_1.setMOSPlatform(ent1_1);
		ent2_2.setMOSPlatform(ent1_1);

		IMOSPlatform ent1_2 = modelFactory.createPlatform();
		ent1_2.setName(N_ENT1_2);
		ent1_2.setUrl("vienna");
		ent1_2.setCostsPerStreamingMinute(new BigDecimal(7));

		ent1_2.addStreamingServer(ent2_3);

		ent2_3.setMOSPlatform(ent1_2);

		em.persist(ent1_1);
		em.persist(ent1_2);

		// Memberships
		IMembership membership1 = modelFactory.createMembership();
		membership1.setDiscount(0.10);
		membership1.setRegistration(new Date());

		IMembershipKey key1 = modelFactory.createMembershipKey();
		key1.setEventMaster(ent8_1);
		key1.setMOSPlatform(ent1_1);

		membership1.setId(key1);
		ent8_1.addMembership(membership1);

		IMembership membership2 = modelFactory.createMembership();
		membership2.setDiscount(0.20);
		membership2.setRegistration(new Date());

		IMembershipKey key2 = modelFactory.createMembershipKey();
		key2.setEventMaster(ent8_2);
		key2.setMOSPlatform(ent1_1);

		membership2.setId(key2);
		ent8_2.addMembership(membership2);

		IMembership membership3 = modelFactory.createMembership();
		membership3.setDiscount(0.30);
		membership3.setRegistration(new Date());

		IMembershipKey key3 = modelFactory.createMembershipKey();
		key3.setEventMaster(ent8_1);
		key3.setMOSPlatform(ent1_2);

		membership3.setId(key3);
		ent8_1.addMembership(membership3);

		// Entity #6
		IMetadata ent6_1 = modelFactory.createMetadata();
		ent6_1.setGame(N_ENT6_1);
		ent6_1.addSetting(N_ENT6_1_S1);
		ent6_1.addSetting(N_ENT6_1_S2);
		ent6_1.addSetting(N_ENT6_1_S3);

		IMetadata ent6_2 = modelFactory.createMetadata();
		ent6_2.setGame(N_ENT6_2);
		ent6_2.addSetting(N_ENT6_2_S1);

		IMetadata ent6_3 = modelFactory.createMetadata();
		ent6_3.setGame(N_ENT6_3);
		ent6_3.addSetting(N_ENT6_3_S1);

		IMetadata ent6_4 = modelFactory.createMetadata();
		ent6_4.setGame(N_ENT6_4);
		ent6_4.addSetting(N_ENT6_4_S1);

		em.persist(ent6_1);
		em.persist(ent6_2);
		em.persist(ent6_3);
		em.persist(ent6_4);

		IEventStreaming ent4_1 = modelFactory.createEventStreaming();
		ent4_1.setStart(new Date(System.currentTimeMillis() - 1800000));
		ent4_1.setEnd(new Date());
		ent4_1.setStatus(EventStatus.FINISHED);

		IEventStreaming ent4_2 = modelFactory.createEventStreaming();
		ent4_2.setStart(new Date());
		ent4_2.setStatus(EventStatus.SCHEDULED);

		IEventStreaming ent4_3 = modelFactory.createEventStreaming();
		ent4_3.setStart(new Date());
		ent4_3.setStatus(EventStatus.SCHEDULED);

		IEventStreaming ent4_4 = modelFactory.createEventStreaming();
		ent4_4.setStart(new Date());
		ent4_4.setStatus(EventStatus.SCHEDULED);

		ent4_1.addUplinks(ent3_1);
		ent4_2.addUplinks(ent3_2);
		ent4_3.addUplinks(ent3_3);
		ent4_4.addUplinks(ent3_4);

		ent3_1.addEventStreaming(ent4_1);
		ent3_2.addEventStreaming(ent4_2);
		ent3_3.addEventStreaming(ent4_3);
		ent3_4.addEventStreaming(ent4_4);

		// Entity #5
		IEvent ent5_1 = modelFactory.createEvent();
		ent5_1.setAttendingViewers(3);
		ent5_1.setStreamingTime(0);
		ent5_1.setMetadata(ent6_1);
		ent5_1.setEventStreaming(ent4_1);
		ent5_1.setEventMaster(ent8_1);
		ent8_1.addEvent(ent5_1);

		IEvent ent5_2 = modelFactory.createEvent();
		ent5_2.setAttendingViewers(3);
		ent5_2.setStreamingTime(0);
		ent5_2.setMetadata(ent6_2);
		ent5_2.setEventStreaming(ent4_2);
		ent5_2.setEventMaster(ent8_2);
		ent8_2.addEvent(ent5_2);

		IEvent ent5_3 = modelFactory.createEvent();
		ent5_3.setAttendingViewers(4);
		ent5_3.setStreamingTime(0);
		ent5_3.setMetadata(ent6_3);
		ent5_3.setEventStreaming(ent4_3);
		ent5_3.setEventMaster(ent8_1);
		ent8_1.addEvent(ent5_3);

		IEvent ent5_4 = modelFactory.createEvent();
		ent5_4.setAttendingViewers(4);
		ent5_4.setStreamingTime(0);
		ent5_4.setMetadata(ent6_4);
		ent5_4.setEventStreaming(ent4_4);
		ent5_4.setEventMaster(ent8_1);
		ent8_1.addEvent(ent5_4);

		ent4_1.setEvent(ent5_1);
		ent4_2.setEvent(ent5_2);
		ent4_3.setEvent(ent5_3);
		ent4_4.setEvent(ent5_4);

		em.persist(ent7_1);
		em.persist(ent7_2);
		em.persist(ent7_3);
		em.persist(ent8_1);
		em.persist(ent8_2);
		em.persist(ent2_1);
		em.persist(ent2_2);
		em.persist(ent2_3);
		em.persist(ent3_1);
		em.persist(ent3_2);
		em.persist(ent3_3);
		em.persist(ent3_4);
		em.persist(ent3_5);
		em.persist(ent1_1);
		em.persist(ent1_2);
		em.persist(membership1);
		em.persist(membership2);
		em.persist(membership3);
		em.persist(ent5_1);
		em.persist(ent5_2);
		em.persist(ent5_3);
		em.persist(ent5_4);

		entity8_1Id = ent8_1.getId();
		entity8_2Id = ent8_2.getId();

		entity2_1Id = ent2_1.getId();
		entity2_2Id = ent2_2.getId();

		entity3_1Id = ent3_1.getId();
		entity3_2Id = ent3_2.getId();
		entity3_3Id = ent3_3.getId();

		entity4_1Id = ent4_1.getId();
		entity4_2Id = ent4_2.getId();
		entity4_3Id = ent4_3.getId();
		entity4_4Id = ent4_4.getId();

		entity5_1Id = ent5_1.getId();
		entity5_2Id = ent5_2.getId();
		entity5_3Id = ent5_3.getId();
		entity5_4Id = ent5_4.getId();

		entity6_1Id = ent6_1.getId();
		entity6_2Id = ent6_2.getId();
		entity6_3Id = ent6_3.getId();
		entity6_4Id = ent6_4.getId();

		entity7_1Id = ent7_1.getId();
		entity7_2Id = ent7_2.getId();
		entity7_3Id = ent7_3.getId();
	}

}
