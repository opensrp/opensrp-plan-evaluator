/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Samuel Githengi created on 06/18/20
 */
@AllArgsConstructor
@Getter
public class TaskProvider extends BaseProvider {
	
	private TaskDao taskDao;
	
	/**
	 * gets the task associated with a resource in a plan
	 * 
	 * @param id the if of the resource
	 * @param planIdentifier the identifier of the plan
	 * @return the tasks for a resource in a plan
	 */
	public List<Task> getTasks(Resource resource, String planIdentifier) {
		
		return taskDao.findTasksForEntity(resource.getId(), planIdentifier);
		
	}
	
}
