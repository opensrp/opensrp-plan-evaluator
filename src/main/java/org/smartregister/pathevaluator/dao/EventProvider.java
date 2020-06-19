/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;

import lombok.AllArgsConstructor;

/**
 * @author Samuel Githengi created on 06/19/20
 */
@AllArgsConstructor
public class EventProvider {
	
	private EventDao eventDao;
	
	/**
	 * Gets events for to a resource in a particular plan
	 * 
	 * @param resourceId resource id
	 * @param planIdentifier plan identifier
	 * @return events for to a resource in a particular plan
	 */
	public List<QuestionnaireResponse> getEvents(Resource resource, String planIdentifier) {
		return eventDao.findEventsByEntityIdAndPlan(resource.getId(), planIdentifier);
	}
	
}
