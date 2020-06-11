/**
 * 
 */
package org.smartregister.pathevaluator.utils;

import org.smartregister.pathevaluator.TriggerEvent;

import com.ibm.fhir.model.resource.PlanDefinition;

/**
 * @author Samuel Githengi created on 06/11/20
 */
public class PlanHelper {
	
	/**
	 * Get the plan Trigger event
	 * @param planDefinition the new Plan definition
	 * @param existingPlanDefinition the existing plan definition
	 * @return the Trigger event 
	 */
	public TriggerEvent evaluatePlanModification(PlanDefinition planDefinition, PlanDefinition existingPlanDefinition) {
		if (existingPlanDefinition == null) {
			return TriggerEvent.PLAN_ACTIVATION;
		}
		return null;
	}
	
	
	
	
}
