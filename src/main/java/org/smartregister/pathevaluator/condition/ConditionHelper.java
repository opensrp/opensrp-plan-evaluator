/**
 * 
 */
package org.smartregister.pathevaluator.condition;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.domain.Action;
import org.smartregister.domain.Action.SubjectConcept;
import org.smartregister.domain.Condition;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.action.ActionHelper;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Samuel Githengi created on 06/15/20
 */
@RequiredArgsConstructor
public class ConditionHelper {
	
	private PathEvaluatorLibrary pathEvaluatorLibrary = PathEvaluatorLibrary.getInstance();
	
	@NonNull
	private ActionHelper actionHelper;
	
	/**
	 * Evaluates an action conditions on whether task generation should be executed
	 * 
	 * @param resource the resource being evaluated against
	 * @param action the action being evaluated
	 * @param planIdentifier
	 * @param triggerEvent
	 * @return result of condition evaluation
	 */
	public boolean evaluateActionConditions(DomainResource resource, Action action, String planIdentifier,
	        TriggerType triggerEvent) {
		boolean isValid = true;
		for (Condition condition : action.getCondition()) {
			//this condition is not applicable for this triggering event, skip its evaluation
			if (StringUtils.isNotBlank(condition.getExpression().getReference())
			        && !triggerEvent.getValue().equalsIgnoreCase(condition.getExpression().getReference())) {
				continue;
			}
			SubjectConcept concept = condition.getExpression().getSubjectConcept();
			if (concept != null) {
				List<? extends DomainResource> resources = actionHelper.getConditionSubjectResources(condition, action, resource,
				    planIdentifier);
				if (resources != null) {
					isValid = resources.stream().anyMatch(
					    r -> pathEvaluatorLibrary.evaluateBooleanExpression(r, condition.getExpression().getExpression()));
				}
			} else {
				isValid = pathEvaluatorLibrary.evaluateBooleanExpression(resource,
				    condition.getExpression().getExpression());
			}
			if (!isValid) {
				return isValid;
			}
		}
		return isValid;
	}
	
}
