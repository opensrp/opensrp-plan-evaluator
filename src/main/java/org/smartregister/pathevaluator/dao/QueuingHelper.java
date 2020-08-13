package org.smartregister.pathevaluator.dao;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.smartregister.domain.Action;
import org.smartregister.pathevaluator.TriggerType;

public interface QueuingHelper {

	void addToQueue(String planId, TriggerType triggerType, String locationId);

	void addToQueue(String resource, QuestionnaireResponse questionnaireResponse,
					Action action, String planIdentifier, String jurisdictionCode, TriggerType triggerEvent);

}
