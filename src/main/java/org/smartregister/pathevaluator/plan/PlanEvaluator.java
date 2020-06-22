package org.smartregister.pathevaluator.plan;

import java.util.Collection;
import java.util.List;

import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.TriggerEventPayload;
import org.smartregister.pathevaluator.action.ActionHelper;
import org.smartregister.pathevaluator.condition.ConditionHelper;
import org.smartregister.pathevaluator.task.TaskHelper;
import org.smartregister.pathevaluator.trigger.TriggerHelper;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;

/**
 * @author Samuel Githengi created on 06/09/20
 */
public class PlanEvaluator {
	
	private ActionHelper actionHelper;
	
	private ConditionHelper conditionHelper;
	
	private TaskHelper taskHelper;
	
	private TriggerHelper triggerHelper;

	public PlanEvaluator() {
		actionHelper = new ActionHelper();
		conditionHelper = new ConditionHelper(actionHelper);
		taskHelper = new TaskHelper();
		triggerHelper = new TriggerHelper(actionHelper);
	}

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
		if (triggerEvent != null && (triggerEvent.getTriggerEvent().equals(TriggerType.PLAN_ACTIVATION)
		        || triggerEvent.getTriggerEvent().equals(TriggerType.PLAN_JURISDICTION_MODIFICATION))) {
			evaluatePlan(planDefinition, triggerEvent.getTriggerEvent(), triggerEvent.getJurisdictions());
		}

	}

	/**
	 * Evaluates a plan if an encounter is submitted
	 *
	 * @param planDefinition the plan being evaluated
	 * @param questionnaireResponse the questionnaireResponse that has just been submitted
	 */
	public void evaluatePlan(PlanDefinition planDefinition, QuestionnaireResponse questionnaireResponse) {
	}

	/**
	 * Evaluates a plan for task generation
	 *
	 * @param planDefinition the plan being evaluated
	 * @param triggerEvent
	 * @param jurisdictions
	 */
	private void evaluatePlan(PlanDefinition planDefinition, TriggerType triggerEvent, List<Jurisdiction> jurisdictions) {
		jurisdictions.parallelStream()
		        .forEach(jurisdiction -> evaluatePlan(planDefinition, triggerEvent, jurisdiction, null));
	}

	/**
	 * Evaluates a plan for task generation
	 *
	 * @param planDefinition the plan being evaluated
	 * @param questionnaireResponse {@link QuestionnaireResponse} just submitted
	 */
	private void evaluatePlan(PlanDefinition planDefinition, TriggerType triggerEvent, Jurisdiction jurisdiction,
	        QuestionnaireResponse questionnaireResponse) {
		
		planDefinition.getActions().forEach(action -> {
			if (triggerHelper.evaluateTrigger(action.getTriggers(), triggerEvent, planDefinition.getIdentifier(),
			    questionnaireResponse)) {
				actionHelper.getSubjectResources(action, jurisdiction).forEach(resource -> {
					if (conditionHelper.evaluateActionConditions(resource, action, planDefinition.getIdentifier())) {
						taskHelper.generateTask(resource, action,planDefinition.getIdentifier(),jurisdiction.getCode());
					}
				});
			}
		});
	}

}
