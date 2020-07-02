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

	boolean checkIfTaskExists(String baseEntityId, String planIdentifier, String code);
	
}
