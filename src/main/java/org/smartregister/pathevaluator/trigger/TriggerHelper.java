/**
 * 
 */
package org.smartregister.pathevaluator.trigger;

import static org.smartregister.pathevaluator.TriggerEvent.EVENT_SUBMISSION;
import static org.smartregister.pathevaluator.TriggerEvent.PLAN_ACTIVATION;
import static org.smartregister.pathevaluator.TriggerEvent.PLAN_JURISDICTION_CHANGE;

import java.util.Set;

import org.smartregister.domain.Trigger;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TriggerEvent;
import org.smartregister.pathevaluator.action.ActionHelper;

import com.ibm.fhir.model.resource.QuestionnaireResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Samuel Githengi created on 06/19/20
 */
@RequiredArgsConstructor
public class TriggerHelper {
	
	@NonNull
	private ActionHelper actionHelper;
	
	private PathEvaluatorLibrary pathEvaluatorLibrary = PathEvaluatorLibrary.getInstance();
	
	/**
	 * Checks if trigger conditions for an action are met
	 * 
	 * @param triggers the set of triggers being evaluated
	 * @param triggerEvent triggering event
	 * @param quest
	 * @return true if trigger conditions are met
	 */
	public boolean evaluateTrigger(Set<Trigger> triggers, TriggerEvent triggerEvent, String planIdentifier,
	        QuestionnaireResponse questionnaireResponse) {
		if (triggers == null)
			return false;
		QuestionnaireResponse questionnaireWithEntityIdId = questionnaireResponse.toBuilder()
		        .id(questionnaireResponse.getIdentifier().getValue().getValue()).build();
		boolean valid = false;
		for (Trigger trigger : triggers) {
			if (PLAN_ACTIVATION.equals(triggerEvent) || PLAN_JURISDICTION_CHANGE.equals(triggerEvent)) {
				return false;
			} else if (EVENT_SUBMISSION.equals(triggerEvent) && trigger.getExpression() != null) {
				if (trigger.getExpression().getSubjectConcept() == null) {
					valid = pathEvaluatorLibrary.evaluateBooleanExpression(questionnaireResponse,
					    trigger.getExpression().getExpression());
				} else {
					valid = actionHelper
					        .getConditionSubjectResources(questionnaireWithEntityIdId, planIdentifier,
					            ResourceType.from(trigger.getExpression().getSubjectConcept().getText()),
					            ResourceType.QUESTIONAIRRE_RESPONSE)
					        .stream()
					        .anyMatch(resource -> pathEvaluatorLibrary.evaluateBooleanExpression(resource,
					            trigger.getExpression().getExpression()));
				}
				if (valid) {
					return true;
				}
			}
		}
		return valid;
	}
	
}
