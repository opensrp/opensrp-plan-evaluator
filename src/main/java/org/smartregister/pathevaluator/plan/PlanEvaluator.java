package org.smartregister.pathevaluator.plan;

import java.util.Collection;

import org.smartregister.pathevaluator.TriggerEvent;
import org.smartregister.pathevaluator.utils.PlanHelper;

import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;

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
		TriggerEvent triggerEvent = PlanHelper.evaluatePlanModification(planDefinition, existingPlanDefinition);
		if (triggerEvent != null) {
			evaluatePlan(planDefinition);
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
	 * Evaluates a plan for task generation
	 * 
	 * @param planDefinition the plan being evaluated
	 */
	private void evaluatePlan(PlanDefinition planDefinition) {
		
	}
	
}
