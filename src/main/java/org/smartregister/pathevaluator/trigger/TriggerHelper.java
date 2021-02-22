/**
 * 
 */
package org.smartregister.pathevaluator.trigger;

import static org.smartregister.pathevaluator.TriggerType.EVENT_SUBMISSION;
import static org.smartregister.pathevaluator.TriggerType.PLAN_ACTIVATION;
import static org.smartregister.pathevaluator.TriggerType.PLAN_JURISDICTION_MODIFICATION;

import java.util.Set;

import org.smartregister.domain.Trigger;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TriggerType;
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
	public boolean evaluateTrigger(Set<Trigger> triggers, TriggerType triggerEvent, String planIdentifier,
	        QuestionnaireResponse questionnaireResponse) {
		if (triggers == null)
			return false;
		QuestionnaireResponse questionnaireWithEntityIdId = null;
		if (questionnaireResponse != null) {
			questionnaireWithEntityIdId = questionnaireResponse.toBuilder()
			        .id(questionnaireResponse.getSubject().getReference().getValue()).build();
		}
		boolean valid = false;
		for (Trigger trigger : triggers) {
			if ((PLAN_ACTIVATION == triggerEvent || PLAN_JURISDICTION_MODIFICATION == triggerEvent)
			        && PLAN_ACTIVATION.getValue().equalsIgnoreCase(trigger.getName())) {
				return true;
			} else if (trigger.getExpression() != null && EVENT_SUBMISSION == triggerEvent  ) {
				if (trigger.getExpression().getSubjectCodableConcept() == null) {
					valid = pathEvaluatorLibrary.evaluateBooleanExpression(questionnaireResponse,
					    trigger.getExpression().getExpression());
				} else {
					valid = actionHelper
					        .getConditionSubjectResources(questionnaireWithEntityIdId, planIdentifier,
					            ResourceType.from(trigger.getExpression().getSubjectCodableConcept().getText()),
					            ResourceType.QUESTIONAIRRE_RESPONSE)
					        .stream().anyMatch(resource -> pathEvaluatorLibrary.evaluateBooleanExpression(resource,
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
