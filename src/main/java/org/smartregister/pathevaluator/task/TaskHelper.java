/**
 * 
 */
package org.smartregister.pathevaluator.task;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.smartregister.domain.Action;
import org.smartregister.domain.DynamicValue;
import org.smartregister.domain.Task;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.dao.TaskDao;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.QuestionnaireResponse;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public class TaskHelper {
	
	private PathEvaluatorLibrary pathEvaluatorLibrary = PathEvaluatorLibrary.getInstance();
	
	private static Logger logger = Logger.getLogger(TaskHelper.class.getSimpleName());
	
	/**
	 * Generates task for an action and target resource
	 * 
	 * @param resource entity task is being generated for
	 * @param action entity used to create the task
	 * @param username
	 * @param questionnaireResponse
	 */
	public void generateTask(DomainResource resource, Action action, String planIdentifier, String jurisdiction,
	        String username, QuestionnaireResponse questionnaireResponse) {
		TaskDao taskDao = PathEvaluatorLibrary.getInstance().getTaskProvider().getTaskDao();
		if (taskDao.checkIfTaskExists(resource.getId(), jurisdiction, planIdentifier, action.getCode())) {
			logger.info("Task already exists");
		} else {
			Task task = new Task();
			task.setIdentifier(UUID.randomUUID().toString());
			task.setPlanIdentifier(planIdentifier);
			task.setGroupIdentifier(jurisdiction);
			task.setStatus(Task.TaskStatus.READY);
			task.setPriority(Task.TaskPriority.ROUTINE);
			task.setCode(action.getCode());
			task.setDescription(action.getDescription());
			task.setFocus(action.getIdentifier());
			task.setForEntity(resource.getId());
			task.setExecutionPeriod(action.getTimingPeriod());
			task.setAuthoredOn(DateTime.now());
			task.setLastModified(DateTime.now());
			if (action.getDynamicValue() != null) {
				for (DynamicValue dynamicValue : action.getDynamicValue()) {
					if (dynamicValue != null && dynamicValue.getExpression() != null
					        && dynamicValue.getExpression().getName().equals("defaultBusinessStatus")) {
						task.setBusinessStatus(dynamicValue.getExpression().getExpression());
					}
				}
			}
			if (task.getBusinessStatus() == null) {
				task.setBusinessStatus("Not Visited");
			}
			task.setRequester(username);
			task.setOwner(username);
			taskDao.saveTask(task, questionnaireResponse);
			logger.info("Created task " + task.toString());
		}
	}
	
	public void updateTask(DomainResource resource, Action action) {
		TaskDao taskDao = PathEvaluatorLibrary.getInstance().getTaskProvider().getTaskDao();
		Task task = taskDao.getTaskByIdentifier(resource.getId());
		try {
			for (DynamicValue dynamicValue : action.getDynamicValue()) {
				Field aField = task.getClass().getDeclaredField(dynamicValue.getPath());
				aField.setAccessible(true);
				if (aField.getType().isAssignableFrom(Task.TaskStatus.class)) {
					aField.set(task, Task.TaskStatus.get(pathEvaluatorLibrary
					        .evaluateStringExpression(resource, dynamicValue.getExpression().getExpression()).string()));
				} else if (aField.getType().isAssignableFrom(Task.TaskPriority.class)) {
					aField.set(task, Task.TaskPriority.get(pathEvaluatorLibrary
					        .evaluateStringExpression(resource, dynamicValue.getExpression().getExpression()).string()));
				} else if (aField.getType().isAssignableFrom(String.class)) {
					aField.set(task, pathEvaluatorLibrary
					        .evaluateStringExpression(resource, dynamicValue.getExpression().getExpression()).string());
				} else {
					throw new UnsupportedOperationException();
				}
			}
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Exception occurred while updating properties using Reflection" + e);
		}
		
		taskDao.updateTask(task);
	}
	
	
}
