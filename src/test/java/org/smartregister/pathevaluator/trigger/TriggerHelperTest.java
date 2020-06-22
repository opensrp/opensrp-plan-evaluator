/**
 * 
 */
package org.smartregister.pathevaluator.trigger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.smartregister.domain.Action.SubjectConcept;
import org.smartregister.domain.Expression;
import org.smartregister.domain.Trigger;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.action.ActionHelper;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Task;

/**
 * @author Samuel Githengi created on 06/19/20
 */
@RunWith(MockitoJUnitRunner.class)
public class TriggerHelperTest {
	
	@Mock
	private ActionHelper actionHelper;
	
	@Captor
	private ArgumentCaptor<QuestionnaireResponse> questionnaireCaptor;
	
	private TriggerHelper triggerHelper;
	
	private String planIdentifier = UUID.randomUUID().toString();
	
	private Trigger trigger;
	
	private QuestionnaireResponse questionnaireResponse = TestData.createResponse();
	
	@Before
	public void setUp() {
		PathEvaluatorLibrary.init(null, null, null, null,null);
		triggerHelper = new TriggerHelper(actionHelper);
		trigger = Trigger.builder().name(TriggerType.PLAN_ACTIVATION.value()).build();
	}
	
	@Test
	public void testEvaluateTriggerForPlanActivation() {
		assertTrue(triggerHelper.evaluateTrigger(Collections.singleton(trigger), TriggerType.PLAN_ACTIVATION, planIdentifier,
		    null));
		verifyNoInteractions(actionHelper);
	}
	
	@Test
	public void testEvaluateTriggerForPlanJurisdictionModification() {
		assertTrue(triggerHelper.evaluateTrigger(Collections.singleton(trigger), TriggerType.PLAN_JURISDICTION_MODIFICATION,
		    planIdentifier, null));
		verifyNoInteractions(actionHelper);
	}
	
	@Test
	public void testEvaluateTriggerWithEmptyTriggers() {
		assertFalse(triggerHelper.evaluateTrigger(null, TriggerType.PLAN_JURISDICTION_MODIFICATION, planIdentifier, null));
		assertFalse(triggerHelper.evaluateTrigger(new HashSet<>(), TriggerType.PLAN_JURISDICTION_MODIFICATION,
		    planIdentifier, null));
		verifyNoInteractions(actionHelper);
	}
	
	@Test
	public void testEvaluateTriggerWithEvent() {
		Expression expression = Expression.builder()
		        .expression("QuestionnaireResponse.item.where(linkId='structureType').answer.value ='Residential Structure'")
		        .build();
		assertTrue(triggerHelper.evaluateTrigger(Collections.singleton(Trigger.builder().expression(expression).build()),
		    TriggerType.EVENT_SUBMISSION, planIdentifier, questionnaireResponse));
		verifyNoInteractions(actionHelper);
		
		expression = expression.toBuilder()
		        .expression("QuestionnaireResponse.item.where(linkId='structureType').answer.value ='School'").build();
		assertFalse(triggerHelper.evaluateTrigger(Collections.singleton(Trigger.builder().expression(expression).build()),
		    TriggerType.EVENT_SUBMISSION, planIdentifier, questionnaireResponse));
	}
	
	@Test
	public void testEvaluateTriggerWithDependentResources() {
		SubjectConcept subjectConcept = new SubjectConcept("task");
		Expression expression = Expression.builder()
		        .expression("Task.code.text='MDA_Round_1' and Task.businessStatus.text='Completed'")
		        .subjectConcept(subjectConcept).build();
		Trigger trigger = Trigger.builder().expression(expression).expression(expression).build();
		assertFalse(triggerHelper.evaluateTrigger(Collections.singleton(trigger), TriggerType.EVENT_SUBMISSION,
		    planIdentifier, questionnaireResponse));
		
		verify(actionHelper).getConditionSubjectResources(questionnaireCaptor.capture(), eq(planIdentifier),
		    eq(ResourceType.from(subjectConcept)), eq(ResourceType.QUESTIONAIRRE_RESPONSE));
		assertEquals(questionnaireResponse.getSubject().getReference().getValue(), questionnaireCaptor.getValue().getId());
		when(actionHelper.getConditionSubjectResources(any(), eq(planIdentifier), eq(ResourceType.from(subjectConcept)),
		    eq(ResourceType.QUESTIONAIRRE_RESPONSE))).then(new Answer<List<Task>>() {
			    
			    @Override
			    public List<Task> answer(InvocationOnMock invocation) throws Throwable {
				    return Collections.singletonList(TestData.createTask());
			    }
		    });
		
		assertTrue(triggerHelper.evaluateTrigger(Collections.singleton(trigger), TriggerType.EVENT_SUBMISSION,
		    planIdentifier, questionnaireResponse));
	}
}
