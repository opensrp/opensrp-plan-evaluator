/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import org.smartregister.pathevaluator.ResourceType;

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
	
	public List<Task> getTasks(Resource resource, ResourceType fromResourceType, String planIdentifier) {
		
		return taskDao.findTasksForEntity(resource.getId(), planIdentifier);
		
	}
	
}
