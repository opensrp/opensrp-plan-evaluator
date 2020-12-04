/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Task;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public interface TaskDao {
	
	/**
	 * gets the task associated with a resource in a plan
	 * 
	 * @param id the if of the resource
	 * @param planIdentifier the identifier of the plan
	 * @return the tasks for a resource in a plan
	 */
	List<Task> findTasksForEntity(String id, String planIdentifier);

	/**
	 * Saves a task
	 *
	 * @param task
	 * @param resource
	 */
	void saveTask(org.smartregister.domain.Task task, QuestionnaireResponse questionnaireResponse);

	boolean checkIfTaskExists(String baseEntityId,String juridiction, String planIdentifier, String code);

	List<Task> findAllTasksForEntity(String id);

	org.smartregister.domain.Task getTaskByIdentifier(String id);

	/**
	 * Updates the task in the repository and returns the updated task if the 
	 * update is successful. 
	 * 
	 * @param task
	 * @return org.smartregister.domain.Task if the operation was successful. Returns null 
	 * if the operation failed.
	 */
	org.smartregister.domain.Task updateTask(org.smartregister.domain.Task task);

	List<Task> findTasksByJurisdiction(String jurisdiction);
}
