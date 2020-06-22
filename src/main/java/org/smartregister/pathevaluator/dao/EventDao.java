/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.QuestionnaireResponse;

/**
 * @author Samuel Githengi created on 06/19/20
 */
public interface EventDao {

	/** Gets events for to a resource in a particular plan
	 * @param resourceId resource id
	 * @param planIdentifier plan identifier
	 * @return events for to a resource in a particular plan
	 */
	List<QuestionnaireResponse> findEventsByEntityIdAndPlan(String resourceId, String planIdentifier);
	
}
