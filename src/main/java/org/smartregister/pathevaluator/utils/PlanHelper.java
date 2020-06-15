/**
 * 
 */
package org.smartregister.pathevaluator.utils;

import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.PlanDefinition.PlanStatus;
import org.smartregister.pathevaluator.TriggerEvent;


/**
 * @author Samuel Githengi created on 06/11/20
 */
public class PlanHelper {
	
	/**
	 * Get the plan Trigger event
	 * 
	 * @param planDefinition the new Plan definition
	 * @param existingPlanDefinition the existing plan definition
	 * @return the Trigger event
	 */
	public static TriggerEvent evaluatePlanModification(PlanDefinition planDefinition, PlanDefinition existingPlanDefinition) {
		if (existingPlanDefinition == null && planDefinition.getStatus().equals(PlanStatus.ACTIVE)) {
			return TriggerEvent.PLAN_ACTIVATION;
		}
		//TODO implement rest of logic  @Ronald to add this
		return null;
	}
	
}
