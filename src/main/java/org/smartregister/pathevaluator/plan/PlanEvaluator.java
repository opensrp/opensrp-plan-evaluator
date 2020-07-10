package org.smartregister.pathevaluator.plan;

import java.util.Collections;
import java.util.List;

import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TriggerEventPayload;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.action.ActionHelper;
import org.smartregister.pathevaluator.condition.ConditionHelper;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.task.TaskHelper;
import org.smartregister.pathevaluator.trigger.TriggerHelper;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.QuestionnaireResponse;

/**
 * @author Samuel Githengi created on 06/09/20
 */
public class PlanEvaluator {
	
	private ActionHelper actionHelper;
	
	private ConditionHelper conditionHelper;
	
	private TaskHelper taskHelper;
	
	private TriggerHelper triggerHelper;

	private LocationDao locationDao;
	
	private String username;
	
	public PlanEvaluator(String username) {
		actionHelper = new ActionHelper();
		conditionHelper = new ConditionHelper(actionHelper);
		taskHelper = new TaskHelper();
		triggerHelper = new TriggerHelper(actionHelper);
		this.username = username;
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
		QuestionnaireResponse.Item.Answer location = PathEvaluatorLibrary.getInstance()
		        .evaluateElementExpression(questionnaireResponse,
		            "QuestionnaireResponse.item.where(linkId='locationId').answer")
		        .element().as(QuestionnaireResponse.Item.Answer.class);
		
		evaluatePlan(planDefinition, TriggerType.EVENT_SUBMISSION,
		    new Jurisdiction(location.getValue().as(com.ibm.fhir.model.type.String.class).getValue()),
		    questionnaireResponse);
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
				.forEach(jurisdiction -> {
					evaluatePlan(planDefinition, triggerEvent, jurisdiction, null);
					List<String> locationIds = locationDao.findChildLocationByJurisdiction(jurisdiction.getCode());
					for (String locationId : locationIds) {
						evaluatePlan(planDefinition, triggerEvent, new Jurisdiction(locationId), null);
					}
				});
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
			if (triggerHelper.evaluateTrigger(action.getTrigger(), triggerEvent, planDefinition.getIdentifier(),
			    questionnaireResponse)) {
				List<? extends DomainResource> resources;
				if (questionnaireResponse != null) {
					resources = actionHelper.getSubjectResources(action,
					    questionnaireResponse.getSubject().getReference().getValue());
				} else {
					resources = actionHelper.getSubjectResources(action, jurisdiction);
					
				}
				resources.forEach(resource -> {
					if (conditionHelper.evaluateActionConditions(
					    questionnaireResponse == null ? resource
					            : questionnaireResponse.toBuilder().contained(Collections.singleton(resource)).build(),
					    action, planDefinition.getIdentifier(), triggerEvent)) {
						taskHelper.generateTask(resource, action, planDefinition.getIdentifier(), jurisdiction.getCode(),
						    username,questionnaireResponse);
					}
				});
			}
		});
	}
	
}
