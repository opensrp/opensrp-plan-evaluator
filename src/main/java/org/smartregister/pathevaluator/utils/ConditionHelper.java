package org.smartregister.pathevaluator.utils;

import com.ibm.fhir.model.resource.Resource;
import org.smartregister.domain.Condition;

import java.util.Set;

public class ConditionHelper {

	/**
	 * Verify conditions are met for executing the
	 * @param resource
	 * @param condition
	 * @return
	 */
	//TODO
	public static boolean evaluateActionConditions(Resource resource, Set<Condition> condition) {
		return false;
	}

}
