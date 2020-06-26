/**
 * 
 */
package org.smartregister.pathevaluator.action;

import java.util.List;

import org.smartregister.domain.Action;
import org.smartregister.domain.Condition;
import org.smartregister.domain.Jurisdiction;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.LocationDao;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public class ActionHelper {
	
	private LocationDao locationDao = PathEvaluatorLibrary.getInstance().getLocationProvider().getLocationDao();
	
	private ClientDao clientDao = PathEvaluatorLibrary.getInstance().getClientProvider().getClientDao();
	
	/**
	 * Gets the resource type for the action
	 * 
	 * @param action the action
	 * @return the ResourceType for the action
	 */
	public static ResourceType getResourceType(Action action) {
		
		if (action.getSubjectCodableConcept() == null) {
			throw new IllegalArgumentException("getSubjectCodableConcept is null ");
		}
		return ResourceType.from(action.getSubjectCodableConcept().getText());
	}
	
	/**
	 * Gets the subject resources in the jurisdiction that tasks should be generated against
	 * 
	 * @param action
	 * @param jurisdiction
	 * @return resources that tasks should be generated against
	 */
	public List<? extends DomainResource> getSubjectResources(Action action, Jurisdiction jurisdiction) {
		ResourceType resourceType = getResourceType(action);
		switch (resourceType) {
			case JURISDICTION:
				
				return locationDao.findJurisdictionsById(jurisdiction.getCode());
			
			case LOCATION:
				return locationDao.findLocationByJurisdiction(jurisdiction.getCode());
			
			case FAMILY:
				return clientDao.findFamilyByJurisdiction(jurisdiction.getCode());
			
			case PERSON:
				return clientDao.findFamilyMemberyByJurisdiction(jurisdiction.getCode());
			
			default:
				return null;
		}
	}
	
	/**
	 * Gets the subject resources for the target entity that tasks should be generated against
	 * 
	 * @param action the action to evaluate
	 * @param entity id for entity
	 * @return resources that tasks should be generated against
	 */
	public List<? extends DomainResource> getSubjectResources(Action action, String entity) {
		ResourceType resourceType = getResourceType(action);
		switch (resourceType) {
			case JURISDICTION:
				return locationDao.findJurisdictionsById(entity);
			case LOCATION:
				return locationDao.findLocationsById(entity);
			case FAMILY:
			case PERSON:
				return clientDao.findClientById(entity);
			default:
				return null;
		}
	}
	
	/**
	 * Gets the subject resources of the resource id that tasks should be generated against
	 *
	 * @param condition
	 * @param action
	 * @param id the resource id
	 * @return resources that tasks should be generated against
	 */
	public List<? extends Resource> getConditionSubjectResources(Condition condition, Action action, Resource resource,
	        String planIdentifier) {
		ResourceType conditionResourceType = ResourceType.from(condition.getExpression().getSubjectCodableConcept());
		ResourceType actionResourceType = ResourceType.from(action.getSubjectCodableConcept());
		return getConditionSubjectResources(resource, planIdentifier, conditionResourceType, actionResourceType);
	}
	
	/**
	 * Gets the subject resources for the resource
	 * 
	 * @param resource the resource id
	 * @param planIdentifier the plan Identifier
	 * @param conditionResourceType the condition/expression subject concept
	 * @param actionResourceType the action subject concept
	 * @return resources that tasks should be generated against
	 */
	public List<? extends Resource> getConditionSubjectResources(Resource resource, String planIdentifier,
	        ResourceType conditionResourceType, ResourceType actionResourceType) {
		
		switch (conditionResourceType) {
			case JURISDICTION:
				return PathEvaluatorLibrary.getInstance().getLocationProvider().getJurisdictions(resource,
				    actionResourceType);
			
			case LOCATION:
				return PathEvaluatorLibrary.getInstance().getLocationProvider().getLocations(resource, actionResourceType);
			
			case FAMILY:
				return PathEvaluatorLibrary.getInstance().getClientProvider().getFamilies(resource, actionResourceType);
			
			case PERSON:
				return PathEvaluatorLibrary.getInstance().getClientProvider().getFamilyMembers(resource, actionResourceType);
			
			case TASK:
				return PathEvaluatorLibrary.getInstance().getTaskProvider().getTasks(resource, planIdentifier);
			
			case QUESTIONAIRRE_RESPONSE:
				return PathEvaluatorLibrary.getInstance().getEventProvider().getEvents(resource, planIdentifier);
			
			default:
				return null;
		}
	}
	
}
