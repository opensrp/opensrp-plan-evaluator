package org.smartregister.pathevaluator.dao;

import org.smartregister.pathevaluator.TriggerType;

public interface QueuingHelper {

	void addToQueue(String planId, TriggerType triggerType, String locationId);

}
