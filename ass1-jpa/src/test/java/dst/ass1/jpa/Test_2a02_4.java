package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.ExceptionUtils;
import dst.ass1.jpa.util.test.TestData;

public class Test_2a02_4 extends AbstractTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testQuery() {
		try {
			Query query = em.createNamedQuery(Constants.Q_MOSTACTIVEEVENTMASTER);

			List<IEventMaster> result = (List<IEventMaster>) query.getResultList();
			assertNotNull(result);
			assertEquals(0, result.size());
		} catch (Exception e) {
			fail(ExceptionUtils.getMessage(e));
		}

	}

	protected void setUpDatabase() {
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			IAddress address1 = modelFactory.createAddress();
			IAddress address2 = modelFactory.createAddress();
			IAddress address3 = modelFactory.createAddress();
			IAddress address4 = modelFactory.createAddress();

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

			IModerator ent7_1 = modelFactory.createModerator();
			IModerator ent7_2 = modelFactory.createModerator();

			ent7_1.setFirstName(TestData.N_ENT7_1);
			ent7_1.setLastName(TestData.N_ENT7_1);
			ent7_1.setAddress(address1);

			ent7_2.setFirstName(TestData.N_ENT7_2);
			ent7_2.setLastName(TestData.N_ENT7_2);
			ent7_2.setAddress(address2);

			MessageDigest md = MessageDigest.getInstance("MD5");

			IEventMaster EventMaster1 = modelFactory.createEventMaster();
			EventMaster1.setFirstName("first1");
			EventMaster1.setLastName("last1");
			EventMaster1.setAddress(address3);
			EventMaster1.setAccountNo("account1");
			EventMaster1.setEventMasterName(dst.ass1.jpa.util.test.TestData.N_ENT8_1);
			EventMaster1.setBankCode("bank1");
			EventMaster1.setPassword(md.digest("pw1".getBytes()));

			IEventMaster EventMaster2 = modelFactory.createEventMaster();
			EventMaster2.setFirstName("first2");
			EventMaster2.setLastName("last2");
			EventMaster2.setAddress(address4);
			EventMaster2.setAccountNo("account2");
			EventMaster2.setEventMasterName(dst.ass1.jpa.util.test.TestData.N_ENT8_2);
			EventMaster2.setBankCode("bank2");
			EventMaster2.setPassword(md.digest("pw2".getBytes()));

			em.persist(ent7_1);
			em.persist(ent7_2);
			em.persist(EventMaster1);
			em.persist(EventMaster2);

			IMOSPlatform ent1_1 = modelFactory.createPlatform();
			ent1_1.setName(TestData.N_ENT1_1);
			ent1_1.setUrl("vienna");
			ent1_1.setCostsPerStreamingMinute(new BigDecimal(20));

			em.persist(ent1_1);

			IMembership membership1 = modelFactory.createMembership();
			membership1.setDiscount(0.10);
			membership1.setRegistration(new Date());

			IMembershipKey key1 = modelFactory.createMembershipKey();
			key1.setEventMaster(EventMaster1);
			key1.setMOSPlatform(ent1_1);

			membership1.setId(key1);
			EventMaster1.addMembership(membership1);

			IMembership membership2 = modelFactory.createMembership();
			membership2.setDiscount(0.10);
			membership2.setRegistration(new Date());

			IMembershipKey key2 = modelFactory.createMembershipKey();
			key2.setEventMaster(EventMaster2);
			key2.setMOSPlatform(ent1_1);

			membership2.setId(key2);
			EventMaster2.addMembership(membership2);

			em.persist(ent7_1);
			em.persist(ent7_2);
			em.persist(EventMaster1);
			em.persist(EventMaster2);
			em.persist(ent1_1);
			em.persist(membership1);
			em.persist(membership2);

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			fail(ExceptionUtils.getMessage(e));
		}
	}
}
