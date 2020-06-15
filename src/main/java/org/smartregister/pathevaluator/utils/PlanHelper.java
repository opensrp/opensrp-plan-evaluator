/**
 *
 */
package org.smartregister.pathevaluator.utils;

import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.PlanDefinition.PlanStatus;
import org.smartregister.pathevaluator.TriggerEvent;
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
		if (existingPlanDefinition == null && planDefinition.getStatus().equals(PlanStatus.ACTIVE)) {

			return new TriggerEventPayload(TriggerEvent.PLAN_ACTIVATION, planDefinition.getJurisdiction());

		} else if (existingPlanDefinition != null && planDefinition.getStatus().equals(PlanStatus.ACTIVE)) {

			// check for jurisdiction change
			Set<String> existingJurisdictions = existingPlanDefinition.getJurisdiction()
					.stream()
					.map(Jurisdiction::getCode)
					.collect(Collectors.toSet());

			List<Jurisdiction> modifiedJurisdictions = planDefinition.getJurisdiction()
					.stream()
					.filter(j -> !existingJurisdictions.contains(j.getCode()))
					.collect(Collectors.toList());

			return new TriggerEventPayload(TriggerEvent.PLAN_JURISDICTION_CHANGE, modifiedJurisdictions);
		}
		return null;
	}

}
