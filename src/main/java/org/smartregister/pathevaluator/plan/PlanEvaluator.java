package org.smartregister.pathevaluator.plan;

import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import org.smartregister.domain.Action;
import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.pathevaluator.TriggerEvent;
import org.smartregister.pathevaluator.TriggerEventPayload;
import org.smartregister.pathevaluator.utils.ActionHelper;
import org.smartregister.pathevaluator.utils.ConditionHelper;
import org.smartregister.pathevaluator.utils.PlanHelper;
import org.smartregister.pathevaluator.utils.TaskHelper;
import org.smartregister.pathevaluator.utils.TriggerHelper;

import java.util.Collection;
import java.util.List;

/**
 * 
 */

/**
 * @author Samuel Githengi created on 06/09/20
 */
public class PlanEvaluator {
	
	private FHIRPathEvaluator fhirPathEvaluator = FHIRPathEvaluator.evaluator();
	
	public boolean evaluateBooleanExpression(Resource resource, String expression) {
		
		try {
			Collection<FHIRPathNode> nodes = fhirPathEvaluator.evaluate(resource, expression);
			return nodes != null ? nodes.iterator().next().as(FHIRPathBooleanValue.class)._boolean() : false;
		}
		catch (Exception e) {
			return false;
		}
		
	}
	
	/**
	 * Evaluates plan after plan is saved on updated
	 * 
	 * @param planDefinition the new Plan definition
	 * @param existingPlanDefinition the existing plan definition
	 */
	public void evaluatePlan(PlanDefinition planDefinition, PlanDefinition existingPlanDefinition) {
		TriggerEventPayload triggerEvent = PlanHelper.evaluatePlanModification(planDefinition, existingPlanDefinition);
		if (triggerEvent != null  && (
				triggerEvent.getTriggerEvent().equals(TriggerEvent.PLAN_ACTIVATION) ||
				triggerEvent.getTriggerEvent().equals(TriggerEvent.PLAN_JURISDICTION_CHANGE))
		) {
			evaluatePlan(planDefinition, triggerEvent.getTriggerEvent(), triggerEvent.getJurisdictions());
		}
		
	}
	
	/**
	 * Evaluates a plan if an encounter is submitted
	 * 
	 * @param planDefinition the plan being evaluated
	 * @param encounter the encounter that has just been submitted
	 */
	public void evaluatePlan(PlanDefinition planDefinition, Encounter encounter) {
	}

	/**
	 *  Evaluates a plan for task generation
	 *
	 * @param planDefinition the plan being evaluated
	 * @param triggerEvent
	 * @param jurisdictions
	 */
	private void evaluatePlan(PlanDefinition planDefinition, TriggerEvent triggerEvent, List<Jurisdiction> jurisdictions) {
		jurisdictions.forEach(jurisdiction-> evaluatePlan(planDefinition, triggerEvent, jurisdiction));
	}
	
	/**
	 * Evaluates a plan for task generation
	 * 
	 * @param planDefinition the plan being evaluated
	 */
	private void evaluatePlan(PlanDefinition planDefinition, TriggerEvent triggerEvent, Jurisdiction jurisdiction) {

		planDefinition.getActions().forEach(action -> {
			if (TriggerHelper.evaluateTrigger(action.getTriggers(), triggerEvent)) {
				// // if the jurisdiction contains a list of  resources generate a list of objects
				ActionHelper.getSubject(action, jurisdiction).ifPresent( resources -> resources.forEach(resource -> {
					if (ConditionHelper.evaluateActionConditions(resource, action.getConditions())) {
						TaskHelper.generateTask(resource, action);
					}
				}));
			}
		});
	}

}
