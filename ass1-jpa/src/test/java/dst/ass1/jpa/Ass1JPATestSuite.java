package dst.ass1.jpa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dst.ass1.AbstractTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ 
	
	TestTablesSetup.class,

	Test_1a01.class, Test_1a02.class, Test_1a03.class, Test_1a04.class,
	Test_1a05.class, Test_1a06.class, Test_1a07.class, Test_1a08.class,
	Test_1a09.class, Test_1a10.class, 
	
	Test_1b01.class, Test_1b02.class,

	Test_2a01_1.class, Test_2a01_2.class,
	Test_2a02_1.class, Test_2a02_2.class, Test_2a02_3.class,
	Test_2a02_4.class, 
	Test_2a03.class,
	
	Test_2b01_1.class, Test_2b01_2.class,
	
	Test_2c01.class, Test_2c02.class,

	Test_3a.class, Test_3b.class,

	Test_4a.class, Test_4b.class, Test_4c.class, Test_4d.class
	
})
public class Ass1JPATestSuite extends AbstractTestSuite {

}
