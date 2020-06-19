/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import org.smartregister.pathevaluator.ResourceType;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;

/**
 * @author Samuel Githengi created on 06/19/20
 */
public class EventProvider {
	
	/**
	 * @param resource
	 * @param actionResourceType
	 * @param planIdentifier
	 * @return
	 */
	public List<QuestionnaireResponse> getEvents(Resource resource, ResourceType actionResourceType, String planIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
