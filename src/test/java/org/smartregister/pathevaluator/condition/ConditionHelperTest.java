/**
 * 
 */
package org.smartregister.pathevaluator.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.domain.Action;
import org.smartregister.domain.Condition;
import org.smartregister.domain.Expression;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.action.ActionHelper;

import com.ibm.fhir.model.resource.Patient;

/**
 * @author Samuel Githengi created on 06/16/20
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ConditionHelperTest {
	
	private ConditionHelper conditionHelper;
	
	@Mock
	private ActionHelper actionHelper;
	
	private Patient patient;
	
	private Action action;
	
	private Condition condition;
	
	private Expression expression;
	
	@BeforeClass
	public static void bootstrap() {
		PathEvaluatorLibrary.init();
	}
	
	@Before
	public void setUp() {
		conditionHelper = new ConditionHelper(actionHelper);
		patient = TestData.createPatient();
		action = new Action();
		action.setConditions(new HashSet<>());
		expression = Expression.builder().expression("Patient.name.family = 'John'").build();
		condition = Condition.builder().kind("applicability").expression(expression).build();
	}
	
	@Test
	public void testEvaluateActionWithEmptyConditions() {
		assertTrue(conditionHelper.evaluateActionConditions(patient, action));
	}
	
	@Test
	public void testEvaluateActionConditionWithoutSubject() {
		action.getConditions().add(condition);
		assertTrue(conditionHelper.evaluateActionConditions(patient, action));
		
		expression = Expression.builder().expression("Patient.name.given = 'Jil'").build();
		condition = Condition.builder().kind("applicability").expression(expression).build();
		action.getConditions().add(condition);
		assertFalse(conditionHelper.evaluateActionConditions(patient, action));
	}
}
