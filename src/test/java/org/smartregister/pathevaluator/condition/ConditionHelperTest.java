/**
 * 
 */
package org.smartregister.pathevaluator.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.smartregister.domain.Action;
import org.smartregister.domain.Action.SubjectConcept;
import org.smartregister.domain.Condition;
import org.smartregister.domain.Expression;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.action.ActionHelper;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Task;

/**
 * @author Samuel Githengi created on 06/16/20
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionHelperTest {
	
	private ConditionHelper conditionHelper;
	
	@Mock
	private ActionHelper actionHelper;
	
	private Patient patient;
	
	private Action action;
	
	private Condition condition;
	
	private Expression expression;
	
	private String plan=UUID.randomUUID().toString();
	
	@BeforeClass
	public static void bootstrap() {
		PathEvaluatorLibrary.init(null,null,null, null);
	}
	
	@Before
	public void setUp() {
		conditionHelper = new ConditionHelper(actionHelper);
		patient = TestData.createPatient();
		action = new Action();
		action.setCondition(new HashSet<>());
		expression = Expression.builder().expression("Patient.name.family = 'John'").build();
		condition = Condition.builder().kind("applicability").expression(expression).build();
	}
	
	@Test
	public void testEvaluateActionWithEmptyConditions() {
		assertTrue(conditionHelper.evaluateActionConditions(patient, action,plan));
	}
	
	@Test
	public void testEvaluateActionConditionWithoutSubject() {
		action.getCondition().add(condition);
		assertTrue(conditionHelper.evaluateActionConditions(patient, action,plan));
		
		expression = Expression.builder().expression("Patient.name.given = 'Jil'").build();
		condition = Condition.builder().kind("applicability").expression(expression).build();
		action.getCondition().add(condition);
		assertFalse(conditionHelper.evaluateActionConditions(patient, action,null));
	}
	
	@Test
	public void testEvaluateActionConditionWithSubject() {
		condition = condition.toBuilder()
		        .expression(
		            expression.toBuilder().subjectConcept(new SubjectConcept("Task")).expression("Task.code.text='MDA_Round_1' and Task.businessStatus.text='Completed' ").build())
		        .build();
		action.getCondition().add(condition);
		assertFalse(conditionHelper.evaluateActionConditions(patient, action,plan));
		
		when(actionHelper.getConditionSubjectResources(condition, action, patient,plan)).thenAnswer(new Answer<List<Task>>() {
			
			@Override
			public List<Task> answer(InvocationOnMock invocation) throws Throwable {
				return Collections.singletonList(TestData.createTask());
			}
		});
		assertTrue(conditionHelper.evaluateActionConditions(patient, action,plan));
	}
}
