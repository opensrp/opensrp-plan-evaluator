/**
 * 
 */
package org.smartregister.pathevaluator.plan;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;
import org.smartregister.domain.Action;
import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.PlanDefinition.PlanStatus;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.action.ActionHelper;
import org.smartregister.pathevaluator.condition.ConditionHelper;
import org.smartregister.pathevaluator.task.TaskHelper;

import com.ibm.fhir.model.resource.Patient;

/**
 * @author Samuel Githengi created on 06/15/20
 */
@RunWith(MockitoJUnitRunner.class)
public class PlanEvaluatorTest {
	
	private PlanEvaluator planEvaluator;
	
	@Mock
	private ActionHelper actionHelper;
	
	@Mock
	private ConditionHelper conditionHelper;
	
	@Mock
	private TaskHelper taskHelper;
	
	private String plan=UUID.randomUUID().toString();
	
	@Before
	public void setUp() {
		planEvaluator = new PlanEvaluator();
		Whitebox.setInternalState(planEvaluator, "actionHelper", actionHelper);
		Whitebox.setInternalState(planEvaluator, "conditionHelper", conditionHelper);
		Whitebox.setInternalState(planEvaluator, "taskHelper", taskHelper);
	}
	
	@Test
	public void testEvaluatePlanForInactivePlan() {
		PlanDefinition planDefinition = TestData.createPlan();
		PlanDefinition planDefinition2 = null;
		planEvaluator.evaluatePlan(planDefinition, planDefinition2);
		verify(actionHelper, never()).getSubjectResources(any(), any());
	}
	
	@Test
	public void testEvaluatePlanEvaluatesCondtions() {
		PlanDefinition planDefinition = TestData.createPlan();
		planDefinition.setIdentifier(plan);
		planDefinition.setStatus(PlanStatus.ACTIVE);
		PlanDefinition planDefinition2 = null;
		List<Patient> patients = Collections.singletonList(TestData.createPatient());
		Action action = planDefinition.getActions().get(0);
		Jurisdiction jurisdiction = planDefinition.getJurisdiction().get(0);
		when(actionHelper.getSubjectResources(action, jurisdiction)).thenAnswer(new Answer<List<Patient>>() {
			
			@Override
			public List<Patient> answer(InvocationOnMock invocation) throws Throwable {
				return patients;
			}
		});
		when(conditionHelper.evaluateActionConditions(patients.get(0), action,plan)).thenReturn(true);
		
		planEvaluator.evaluatePlan(planDefinition, planDefinition2);
		
		verify(actionHelper, times(planDefinition.getActions().size() * planDefinition.getJurisdiction().size()))
		        .getSubjectResources(any(), any());
		
		verify(conditionHelper).evaluateActionConditions(patients.get(0), action,plan);
		
		verify(taskHelper).generateTask(patients.get(0), action);
	}
	
}
