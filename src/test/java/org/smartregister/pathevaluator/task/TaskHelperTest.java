package org.smartregister.pathevaluator.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.smartregister.pathevaluator.TestData.createPlanV1;

import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.domain.Action;
import org.smartregister.domain.DynamicValue;
import org.smartregister.domain.Expression;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
import org.smartregister.domain.Task.TaskPriority;
import org.smartregister.domain.Task.TaskStatus;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.EventDao;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.TaskDao;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.QuestionnaireResponse;

@RunWith(MockitoJUnitRunner.class)
public class TaskHelperTest {
	
	private TaskHelper taskHelper;
	
	@Mock
	private LocationDao locationDao;
	
	@Mock
	private ClientDao clientDao;
	
	@Mock
	private TaskDao taskDao;
	
	@Mock
	private EventDao eventDao;
	
	@Mock
	private QuestionnaireResponse questionnaireResponse;

	@Mock
	private Action action;
	
	@Captor
	private ArgumentCaptor<Task> taskCaptor;

	private Patient patient;

	private com.ibm.fhir.model.resource.Task taskResource;
	
	@Before
	public void setUp() {
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao);
		taskHelper = new TaskHelper();
		patient = TestData.createPatient();
		taskResource = TestData.createTask();
	}
	
	@Test
	public void testGenerateTask() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		Mockito.doNothing().when(taskDao).saveTask(any(Task.class), ArgumentMatchers.nullable(QuestionnaireResponse.class));
		taskHelper.generateTask(patient, action, planIdentifier, jurisdiction, "testUser", questionnaireResponse);
		verify(taskDao).saveTask(taskCaptor.capture(), ArgumentMatchers.eq(questionnaireResponse));
		Task task = taskCaptor.getValue();
		assertEquals(patient.getId(), task.getForEntity());
		assertEquals(jurisdiction, task.getGroupIdentifier());
		assertEquals(planIdentifier, task.getPlanIdentifier());
		assertEquals("Not Visited", task.getBusinessStatus());
	}

	@Test
	public void testGenerateTaskWithDefaultBusinessStatus() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		PlanDefinition planDefinition = createPlanV1();
		Mockito.doNothing().when(taskDao).saveTask(any(Task.class), ArgumentMatchers.nullable(QuestionnaireResponse.class));
		taskHelper.generateTask(patient, planDefinition.getActions().get(0), planIdentifier, jurisdiction, "testUser", null);
		verify(taskDao).saveTask(taskCaptor.capture(), ArgumentMatchers.eq(null));
		Task task = taskCaptor.getValue();
		assertEquals(patient.getId(), task.getForEntity());
		assertEquals(jurisdiction, task.getGroupIdentifier());
		assertEquals(planIdentifier, task.getPlanIdentifier());
		assertEquals("expression", task.getBusinessStatus());
	}

	@Test
	public void testShouldNotGenerateTask() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		PlanDefinition planDefinition = createPlanV1();
		when(taskDao.checkIfTaskExists(anyString(),anyString(),anyString(),anyString())).thenReturn(true);
		taskHelper.generateTask(patient, planDefinition.getActions().get(0), planIdentifier, jurisdiction, "testUser", null);
		verify(taskDao, never()).saveTask(any(Task.class), any(QuestionnaireResponse.class));
	}

	@Test
	public void testUpdateTask() {
		Task task = TestData.createDomainTask();
		Action action = TestData.createAction();

		when(taskDao.getTaskByIdentifier(anyString())).thenReturn(task);
		Mockito.doReturn(task).when(taskDao).updateTask(any(Task.class));
		taskHelper.updateTask(taskResource, action);
		verify(taskDao, times(1)).updateTask(taskCaptor.capture());
		Task updatedTask = taskCaptor.getValue();
		assertEquals("location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc", updatedTask.getForEntity());
		assertEquals("Family Already Registered", updatedTask.getBusinessStatus());
		assertEquals("CANCELLED", updatedTask.getStatus().name());
		assertEquals(TaskPriority.ROUTINE, updatedTask.getPriority());
	}
	

	@Test
	public void testUpdateTaskWithNullDynamicValuesShouldNotUpdateTask() {
		Task task = TestData.createDomainTask();
		Action action = TestData.createAction();
		action.setDynamicValue(null);
		when(taskDao.getTaskByIdentifier(anyString())).thenReturn(task);
		Mockito.doReturn(task).when(taskDao).updateTask(any(Task.class));
		taskHelper.updateTask(taskResource, action);
		verify(taskDao, times(1)).updateTask(taskCaptor.capture());
		Task updatedTask = taskCaptor.getValue();
		assertEquals("location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc", updatedTask.getForEntity());
		assertEquals("Not Visited", updatedTask.getBusinessStatus());
		assertEquals(TaskStatus.READY, updatedTask.getStatus());
		assertEquals(TaskPriority.ROUTINE, updatedTask.getPriority());
	}
	
	@Test
	public void testUpdateTaskWithDynamicValuesForPriority() {
		Task task = TestData.createDomainTask();
		Action action = TestData.createAction();
		action.getDynamicValue().add(new DynamicValue("priority", Expression.builder().expression("'stat'").build()));

		when(taskDao.getTaskByIdentifier(anyString())).thenReturn(task);
		Mockito.doReturn(task).when(taskDao).updateTask(any(Task.class));
		taskHelper.updateTask(taskResource, action);
		verify(taskDao, times(1)).updateTask(taskCaptor.capture());
		Task updatedTask = taskCaptor.getValue();
		assertEquals("location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc", updatedTask.getForEntity());
		assertEquals("Family Already Registered", updatedTask.getBusinessStatus());
		assertEquals("CANCELLED", updatedTask.getStatus().name());
		assertEquals(TaskPriority.STAT, updatedTask.getPriority());
	}
	
	@Test
	public void testUpdateTaskWithDynamicValuesForRestriction() {
		Task task = TestData.createDomainTask();
		Action action = TestData.createAction();
		action.getDynamicValue().add(new DynamicValue("priority", Expression.builder().expression("'stat'").build()));
		action.getDynamicValue().add(new DynamicValue("restriction.repetitions", Expression.builder().expression("'1'").build()));
		action.getDynamicValue().add(new DynamicValue("restriction.period.start", Expression.builder().expression("today() + 7 'days'").build()));
		action.getDynamicValue().add(new DynamicValue("restriction.period.end", Expression.builder().expression("today() + 2 'months'").build()));

		when(taskDao.getTaskByIdentifier(anyString())).thenReturn(task);
		Mockito.doReturn(task).when(taskDao).updateTask(any(Task.class));
		taskHelper.updateTask(taskResource, action);
		verify(taskDao, times(1)).updateTask(taskCaptor.capture());
		Task updatedTask = taskCaptor.getValue();
	
		assertEquals(1, updatedTask.getRestriction().getRepetitions());
		assertEquals(new DateTime().withTimeAtStartOfDay().plusDays(7).toString(), updatedTask.getRestriction().getPeriod().getStart().toString());
		assertEquals(new DateTime().withTimeAtStartOfDay().plusMonths(2).toString(), updatedTask.getRestriction().getPeriod().getEnd().toString());
	}
}
