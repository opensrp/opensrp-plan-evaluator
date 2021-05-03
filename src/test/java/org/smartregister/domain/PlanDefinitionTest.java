package org.smartregister.domain;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Assert;
import org.junit.Test;

public class PlanDefinitionTest {

	@Test
	public void testGetterAndSetter() {
		Validator validator = ValidatorBuilder.create().with(new SetterTester()).with(new GetterTester()).build();

		validator.validate(PojoClassFactory.getPojoClass(PlanDefinition.class));
	}

	@Test
	public void testCompareTo() {
		PlanDefinition planDefinition1 = new PlanDefinition();
		planDefinition1.setIdentifier("plan-id");
		planDefinition1.setName("the-plan-name");

		PlanDefinition planDefinition2 = new PlanDefinition();
		planDefinition2.setIdentifier("plan-id");
		planDefinition2.setName("the-plan-name");

		Assert.assertEquals(4, planDefinition1.compareTo(planDefinition2));
	}
}
