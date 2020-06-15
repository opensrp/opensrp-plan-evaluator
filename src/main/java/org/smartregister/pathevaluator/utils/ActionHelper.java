package org.smartregister.pathevaluator.utils;

import com.ibm.fhir.model.resource.Resource;
import org.smartregister.domain.Action;
import org.smartregister.domain.Jurisdiction;

import java.util.List;
import java.util.Optional;

public class ActionHelper {

	/***
	 * Returns an optional list of resources attributed with an action and filtered by the jurisdiction
	 * @param action the action type that affects list of objects
	 * @param jurisdiction the location to which this resources are associated with
	 * @return
	 */
	//TODO
	public static Optional<List<Resource>> getSubject(Action action, Jurisdiction jurisdiction) {
		return Optional.empty();
	}
}
