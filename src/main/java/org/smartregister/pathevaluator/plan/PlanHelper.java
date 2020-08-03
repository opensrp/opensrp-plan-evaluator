/**
 *
 */
package org.smartregister.pathevaluator.plan;

import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.PlanDefinition.PlanStatus;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.TriggerEventPayload;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	public static TriggerEventPayload evaluatePlanModification(PlanDefinition planDefinition,
	        PlanDefinition existingPlanDefinition) {
		if ((existingPlanDefinition == null && planDefinition.getStatus().equals(PlanStatus.ACTIVE))
		        || (existingPlanDefinition != null && !PlanStatus.ACTIVE.equals(existingPlanDefinition.getStatus())
		                && planDefinition.getStatus().equals(PlanStatus.ACTIVE))) {
			
			return new TriggerEventPayload(TriggerType.PLAN_ACTIVATION, planDefinition.getJurisdiction());
			
		} else if (existingPlanDefinition != null && planDefinition.getStatus().equals(PlanStatus.ACTIVE)) {
			
			// check for jurisdiction change
			/**@formatter:off*/
			Set<String> existingJurisdictions = existingPlanDefinition.getJurisdiction()
					.stream()
					.map(Jurisdiction::getCode)
					.collect(Collectors.toSet());

			List<Jurisdiction> modifiedJurisdictions = planDefinition.getJurisdiction()
					.stream()
					.filter(j -> !existingJurisdictions.contains(j.getCode()))
					.collect(Collectors.toList());
			/**@formatter:on*/
			return new TriggerEventPayload(TriggerType.PLAN_JURISDICTION_MODIFICATION, modifiedJurisdictions);
		}
		return null;
	}
	
}
