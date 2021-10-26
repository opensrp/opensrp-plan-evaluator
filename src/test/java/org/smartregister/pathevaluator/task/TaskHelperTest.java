package org.smartregister.pathevaluator.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.converters.EventConverter;
import org.smartregister.converters.TaskConverter;
import org.smartregister.domain.Action;
import org.smartregister.domain.DynamicValue;
import org.smartregister.domain.Event;
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
import org.smartregister.utils.DateTypeConverter;
import org.smartregister.utils.TaskDateTimeTypeConverter;
import org.smartregister.pathevaluator.dao.StockDao;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.smartregister.pathevaluator.TestData.createPlanV1;

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
	private StockDao stockDao;

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
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao, stockDao);
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
		taskHelper.updateTask(taskResource, action, null);
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
		taskHelper.updateTask(taskResource, action, null);
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
		taskHelper.updateTask(taskResource, action, null);
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
		taskHelper.updateTask(taskResource, action, null);
		verify(taskDao, times(1)).updateTask(taskCaptor.capture());
		Task updatedTask = taskCaptor.getValue();
	
		assertEquals(1, updatedTask.getRestriction().getRepetitions());
		assertEquals(new DateTime().withTimeAtStartOfDay().plusDays(7).toString(), updatedTask.getRestriction().getPeriod().getStart().toString());
		assertEquals(new DateTime().withTimeAtStartOfDay().plusMonths(2).toString(), updatedTask.getRestriction().getPeriod().getEnd().toString());
	}

	@Test
	public void testUpdateTaskWhenQuestionResponseIsNotNullShouldUpdateTaskUsingParentQuestionnaire() {
		Action action = new Action();
		Set<DynamicValue> dynamicValues = new HashSet<>();
		action.setDynamicValue(dynamicValues);
		action.getDynamicValue().add(new DynamicValue("businessStatus", Expression.builder().expression("$this.item.where(linkId='task_business_status').answer[0].value.value").build()));

		Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
				.registerTypeAdapter(LocalDate.class, new DateTypeConverter()).create();

		Event event = gson.fromJson(TestData.MOSQUITTO_COLLECTION_EVENT, Event.class);
		QuestionnaireResponse eventQuestionnaire = EventConverter.convertEventToEncounterResource(event);

		Task task = gson.fromJson(TestData.MOSQUITTO_COLLECTION_TASK, Task.class);
		Mockito.doReturn(task).when(taskDao).getTaskByIdentifier(task.getIdentifier());

		assertEquals("Not Visited", task.getBusinessStatus());

		// Call the method under test
		taskHelper.updateTask(TaskConverter.convertTasktoFihrResource(task), action, eventQuestionnaire);

		// Perform verifications and assertions
		verify(taskDao, times(1)).updateTask(taskCaptor.capture());
		Task updatedTask = taskCaptor.getValue();
		assertEquals("Complete", updatedTask.getBusinessStatus());

	}

	@Test
	public void testGenerateTaskWhenResourceIsQuestionResponseShouldPopulateTaskReasonReference() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		String userName = "opensrp";
		Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
				.registerTypeAdapter(LocalDate.class, new DateTypeConverter()).create();

		Event event = gson.fromJson(TestData.MOSQUITTO_COLLECTION_EVENT, Event.class);
		QuestionnaireResponse eventQuestionnaire = EventConverter.convertEventToEncounterResource(event);

		// Call the method under test
		taskHelper.generateTask(eventQuestionnaire,action,planIdentifier,jurisdiction,userName,null);

		// Perform verifications and assertions
		verify(taskDao, times(1)).saveTask(taskCaptor.capture(),any());
		Task generatedTask = taskCaptor.getValue();
		assertEquals(event.getId(), generatedTask.getReasonReference());

	}
}
