package dst.ass2.ejb.session;

import java.security.NoSuchAlgorithmException;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass1.jpa.util.test.TestData;
import dst.ass2.ejb.session.interfaces.ITestingBean;

@Stateless
//pozvana direktno od clienta
@Remote
public class TestingBean implements ITestingBean {

    //em ce sa dipend. injec. automatitski injected. njega trebamo da testdaten ubacimo
	@PersistenceContext
	private EntityManager entityManager;

	private TestData testData;

	@Override
	public void insertTestData() {
		System.out.println("Started");

		testData = new TestData(entityManager);
		try {
			testData.insertTestData_withoutTransaction();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		System.out.println("Finished");
	}

}
