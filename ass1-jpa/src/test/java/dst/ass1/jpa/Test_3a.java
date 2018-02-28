package dst.ass1.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;

import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass1.jpa.util.test.TestData;

public class Test_3a {
	
	private ModelFactory modelFactory = new ModelFactory();

	@Test
	public void testBuiltInConstraintsInvalid() {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator val = vf.getValidator();

		IUplink ent3_1 = modelFactory.createUplink();
		ent3_1.setName("cl1");
		ent3_1.setViewerCapacity(50);
		ent3_1.setRegion("region");
		ent3_1.setActivated(createDate(2012, 1, 1));
		ent3_1.setLastUpdate(createDate(2012, 1, 1));

		Set<ConstraintViolation<IUplink>> violation = val.validate(ent3_1);
		assertNotNull(violation);
		assertEquals(2, violation.size());	
	}
	
	@Test
	public void testBuiltInConstaintsValid() {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator val = vf.getValidator();

		IUplink ent3_1 = modelFactory.createUplink();
		ent3_1.setName(TestData.N_ENT3_1);
		ent3_1.setViewerCapacity(50);
		ent3_1.setRegion("AUT-VIE@1040");
		ent3_1.setActivated(createDate(2011, 1, 1));
		ent3_1.setLastUpdate(createDate(2011, 1, 1));
		

		Set<ConstraintViolation<IUplink>> violation = val.validate(ent3_1);
		assertEquals(0, violation.size());

	}

	private Date createDate(int year, int month, int day) {

		String temp = year + "/" + month + "/" + day;
		Date date = null;

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			date = formatter.parse(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}	
}
