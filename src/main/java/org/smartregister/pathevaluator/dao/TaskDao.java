/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import org.smartregister.pathevaluator.ResourceType;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Task;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public interface TaskDao {
	
	/**
	 * gets the task associated with a resource
	 * 
	 * @param resource
	 * @param fromResourceType
	 * @return tasks associated with a resource
	 */
	List<Task> getTasks(Resource resource, ResourceType fromResourceType,String planIdentifier);
	
}
