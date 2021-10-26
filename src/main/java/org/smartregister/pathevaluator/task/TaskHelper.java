/**
 * 
 */
package org.smartregister.pathevaluator.task;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.domain.Action;
import org.smartregister.domain.DynamicValue;
import org.smartregister.domain.Period;
import org.smartregister.domain.Task;
import org.smartregister.domain.Task.Restriction;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.dao.TaskDao;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public class TaskHelper {
	
	private static final String RESTRICTION = "restriction";
	
	private static final String RESTRICTION_REPETITIONS = "restriction.repetitions";
	
	private static final String RESTRICTION_START = "restriction.period.start";
	
	private static final String RESTRICTION_END = "restriction.period.end";
	
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
	public void generateTask(Resource resource, Action action, String planIdentifier, String jurisdiction,
	        String username, QuestionnaireResponse questionnaireResponse) {
		String entityId = getEntityId(resource);
		TaskDao taskDao = PathEvaluatorLibrary.getInstance().getTaskProvider().getTaskDao();
		if (taskDao.checkIfTaskExists(entityId, jurisdiction, planIdentifier, action.getCode())) {
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
			task.setForEntity(entityId);
			task.setExecutionPeriod(action.getTimingPeriod());
			task.setAuthoredOn(DateTime.now());
			task.setLastModified(DateTime.now());
			evaluateDynamicValues(resource, action, task);
			if (task.getBusinessStatus() == null) {
				task.setBusinessStatus("Not Visited");
			}
			task.setRequester(username);
			task.setOwner(username);
			task.setReasonReference(getReasonReference(resource));
			taskDao.saveTask(task, questionnaireResponse);
			logger.info("Created task " + task.toString());
		}
	}
	
	private String getEntityId(Resource resource) {
		String entityId = resource.getId();
		if (resource instanceof QuestionnaireResponse) {
			entityId = ((QuestionnaireResponse) resource).getSubject().getReference().getValue();
		}
		return entityId;
	}
	
	public void updateTask(Resource resource, Action action, QuestionnaireResponse questionnaireResponse) {
		TaskDao taskDao = PathEvaluatorLibrary.getInstance().getTaskProvider().getTaskDao();
		Task task = taskDao.getTaskByIdentifier(resource.getId());

		if (task != null) {

			Resource taskResource = resource;

			if (questionnaireResponse != null) {
				taskResource = questionnaireResponse.toBuilder()
						.contained(resource)
						.build();
			}

			evaluateDynamicValues(taskResource, action, task);

			taskDao.updateTask(task);
			logger.info("updated task " + task.toString());
		}
	}
	
	private void evaluateDynamicValues(Resource resource, Action action, Task task) {
		if (action.getDynamicValue() == null) {
			return;
		}
		try {
			for (DynamicValue dynamicValue : action.getDynamicValue()) {
				if (dynamicValue != null && dynamicValue.getExpression() != null
				        && "defaultBusinessStatus".equals(dynamicValue.getExpression().getName())) {
					task.setBusinessStatus(dynamicValue.getExpression().getExpression());
				} else if (dynamicValue.getPath().startsWith(RESTRICTION)) {
					if (task.getRestriction() == null) {
						Restriction restriction = new Restriction();
						restriction.setPeriod(new Period());
						task.setRestriction(restriction);
					}
					if (dynamicValue.getPath().equals(RESTRICTION_REPETITIONS)) {
						task.getRestriction().setRepetitions(Integer.parseInt(pathEvaluatorLibrary
						        .evaluateStringExpression(resource, dynamicValue.getExpression().getExpression()).string()));
					} else if (dynamicValue.getPath().equals(RESTRICTION_START)) {
						DateTime date = new DateTime(pathEvaluatorLibrary
						        .evaluateDateExpression(resource, dynamicValue.getExpression().getExpression()).toString());
						task.getRestriction().getPeriod().setStart(date);
					} else if (dynamicValue.getPath().equals(RESTRICTION_END)) {
						DateTime date = new DateTime(pathEvaluatorLibrary
						        .evaluateDateExpression(resource, dynamicValue.getExpression().getExpression()).toString());
						task.getRestriction().getPeriod().setEnd(date);
					}
					continue;
				}
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
			logger.log(Level.SEVERE, "Exception occurred while updating properties" + e, e);
		}
	}

	private String getReasonReference(Resource resource) {
		String reasonReference = null;
		if (resource instanceof QuestionnaireResponse) {
			QuestionnaireResponse questionnaireResponse = (QuestionnaireResponse) resource;
			for (QuestionnaireResponse.Item item : questionnaireResponse.getItem()) {
				if (StringUtils.isNotBlank(item.getLinkId().getValue()) && item.getLinkId().getValue().equals("eventId")) {
					String eventId = item.getAnswer() != null && !item.getAnswer().isEmpty() ?
							item.getAnswer().get(0).getValue().as(com.ibm.fhir.model.type.String.class).getValue() : null;
					reasonReference = eventId;
					break;
				}
			}
		}
		return reasonReference;
	}
}
