/**
 * 
 */
package org.smartregister.pathevaluator.utils;

import org.smartregister.pathevaluator.TriggerEvent;

import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.type.code.PublicationStatus;

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
		if (existingPlanDefinition == null && planDefinition.getStatus().equals(PublicationStatus.ACTIVE)) {
			return TriggerEvent.PLAN_ACTIVATION;
		}
		//TODO implement rest of logic
		return null;
	}
	
}
