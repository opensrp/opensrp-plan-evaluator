/**
 *
 */
package org.smartregister.pathevaluator.plan;

import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.PlanDefinition.PlanStatus;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.TriggerEventPayload;
import org.smartregister.pathevaluator.dao.LocationDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Samuel Githengi created on 06/11/20
 */
public class PlanHelper {

	private static LocationDao locationDao = PathEvaluatorLibrary.getInstance().getLocationProvider().getLocationDao();

	/**
	 * Get the plan Trigger event
	 *
	 * @param planDefinition the new Plan definition
	 * @param existingPlanDefinition the existing plan definition
	 * @return the Trigger event
	 */
	public static TriggerEventPayload evaluatePlanModification(PlanDefinition planDefinition,
			PlanDefinition existingPlanDefinition) {

		List<Jurisdiction> jurisdictions = new ArrayList<>();
		Jurisdiction jurisdictionObject;
		for (Jurisdiction jurisdiction : planDefinition.getJurisdiction()) {
			List<String> locationIds = locationDao.findChildLocationByJurisdiction(jurisdiction.getCode());
			for (String locationId : locationIds) {
				jurisdictionObject = new Jurisdiction(locationId);
				jurisdictions.add(jurisdictionObject);
			}
		}

		if (existingPlanDefinition == null && planDefinition.getStatus().equals(PlanStatus.ACTIVE)) {

			return new TriggerEventPayload(TriggerType.PLAN_ACTIVATION, jurisdictions);

		} else if (existingPlanDefinition != null && planDefinition.getStatus().equals(PlanStatus.ACTIVE)) {

			return new TriggerEventPayload(TriggerType.PLAN_JURISDICTION_MODIFICATION, jurisdictions);

		}
		return null;
	}

}
