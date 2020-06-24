/**
 * 
 */
package org.smartregister.pathevaluator.task;

import java.util.UUID;
import java.util.logging.Logger;

import com.ibm.fhir.path.FHIRPathElementNode;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.smartregister.domain.Action;
import org.smartregister.domain.ExecutionPeriod;
import org.smartregister.domain.Task;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.dao.TaskDao;

import com.ibm.fhir.model.resource.Resource;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public class TaskHelper {
	
	private static Logger logger = Logger.getLogger(TaskHelper.class.getSimpleName());
	
	/**
	 * Generates task for an action and target resource
	 * 
	 * @param resource entity task is being generated for
	 * @param action entity used to create the task
	 * @param username 
	 */
	public void generateTask(Resource resource, Action action, String planIdentifier, String jurisdiction, String username) {
		Task task = new Task();
		task.setIdentifier(UUID.randomUUID().toString());
		task.setPlanIdentifier(planIdentifier);
		task.setGroupIdentifier(jurisdiction);
		task.setStatus(Task.TaskStatus.READY);
		task.setPriority(3);
		task.setCode(action.getCode());
		task.setDescription(action.getDescription());
		task.setFocus(action.getIdentifier());
		task.setForEntity(resource.getId());
		task.setExecutionStartDate(getDateTime(action.getTimingPeriod(), true));
		task.setExecutionEndDate(getDateTime(action.getTimingPeriod(), false));
		task.setAuthoredOn(DateTime.now());
		task.setLastModified(DateTime.now());
		if (action.getDynamicValue() != null && action.getDynamicValue().getExpression() != null &&
				action.getDynamicValue().getExpression().getName().equals("defaultBusinessStatus")) {
			FHIRPathElementNode node = PathEvaluatorLibrary.getInstance()
					.evaluateElementExpression(resource, action.getDynamicValue().getExpression().getExpression());
			com.ibm.fhir.model.type.String businessStatus = node.element().as(com.ibm.fhir.model.type.String.class);
			task.setBusinessStatus(businessStatus.getValue());
		} else {
			task.setBusinessStatus("Not Visited");
		}
		task.setRequester(username);
		task.setOwner(username);
		TaskDao taskDao =  PathEvaluatorLibrary.getInstance().getTaskProvider().getTaskDao();
		taskDao.saveTask(task);
		logger.info("Created task " + task.toString());
		
	}
	
	private DateTime getDateTime(ExecutionPeriod executionPeriod, boolean start) {
		if (executionPeriod != null) {
			LocalDate localDate = start ? executionPeriod.getStart() : executionPeriod.getEnd();
			if (localDate != null) {
				return localDate.toDateTimeAtStartOfDay();
			}
		}
		return null;
	}
	
}
