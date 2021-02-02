/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;

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

	/**Gets events in a jurisdiction for a particular plan
	 * @param jurisdictionId the jurisdiction Identifier
	 * @param planIdentifier the plan Identifier
	 * @return events in a jurisdiction for a particular plan
	 */
	List<? extends Resource> findEventsByJurisdictionIdAndPlan(String jurisdictionId, String planIdentifier);
	
}
