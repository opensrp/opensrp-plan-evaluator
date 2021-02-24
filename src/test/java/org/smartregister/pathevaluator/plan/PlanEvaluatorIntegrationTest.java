package org.smartregister.pathevaluator.plan;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.smartregister.pathevaluator.TriggerType.PLAN_ACTIVATION;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.converters.EventConverter;
import org.smartregister.converters.TaskConverter;
import org.smartregister.domain.Event;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
import org.smartregister.domain.Task.TaskStatus;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.EventDao;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.PlanDao;
import org.smartregister.pathevaluator.dao.QueuingHelper;
import org.smartregister.pathevaluator.dao.StockDao;
import org.smartregister.pathevaluator.dao.TaskDao;
import org.smartregister.utils.DateTypeConverter;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.QuestionnaireResponse;

@RunWith(MockitoJUnitRunner.class)
public class PlanEvaluatorIntegrationTest {
	
	private PlanEvaluator planEvaluator;
	
	private String username = UUID.randomUUID().toString();
	
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
	private PlanDao planDao;
	
	@Mock
	private QueuingHelper queuingHelper;
	
	public static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
	        .registerTypeAdapter(LocalDate.class, new DateTypeConverter()).create();
	
	@Before
	public void setUp() {
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao, stockDao);
		PathEvaluatorLibrary.getInstance().setPlanDao(planDao);
		planEvaluator = new PlanEvaluator(username);
	}
	
	@Test
	public void evaluatePlanShouldUpdateTaskBasedOnEventObsValues() {
		PlanDefinition planDefinition = gson.fromJson(TestData.MOSQUITTO_COLLECTION_CLOSE_PLAN, PlanDefinition.class);
		Event event = gson.fromJson(TestData.MOSQUITTO_COLLECTION_EVENT, Event.class);
		QuestionnaireResponse eventQuestionnaire = EventConverter.convertEventToEncounterResource(event);
		
		Task task = gson.fromJson(TestData.MOSQUITTO_COLLECTION_TASK, Task.class);
		doReturn(task).when(taskDao).getTaskByIdentifier(task.getIdentifier());
		
		Assert.assertEquals("READY", task.getStatus().name());
		Assert.assertEquals("Not Visited", task.getBusinessStatus());
		
		planEvaluator.evaluatePlan(planDefinition, eventQuestionnaire);
		
		ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
		verify(taskDao).updateTask(taskArgumentCaptor.capture());
		
		Task finalTask = taskArgumentCaptor.getValue();
		
		Assert.assertEquals("COMPLETED", finalTask.getStatus().name());
		Assert.assertEquals("Complete", finalTask.getBusinessStatus());
	}
	
	@Test
	public void evaluatePlanWithGlobalTasksShouldUpdateAllTasksAndEvaluateOtherPlans() {
		PlanDefinition planDefinition = gson.fromJson(TestData.GLOBAL_TASK_PLAN, PlanDefinition.class);
		PlanDefinition planDefinition2 = gson.fromJson(TestData.MOSQUITTO_COLLECTION_CLOSE_PLAN, PlanDefinition.class);
		Event event = gson.fromJson(TestData.REGISTER_FAMILY_EVENT, Event.class);
		String entityId = UUID.randomUUID().toString();
		event.addDetails("plan_evaluation_entity_id", entityId);
		event.addDetails("planIdentifier", planDefinition.getIdentifier());
		QuestionnaireResponse eventQuestionnaire = EventConverter.convertEventToEncounterResource(event);
		
		Task task = gson.fromJson(TestData.MOSQUITTO_COLLECTION_TASK, Task.class);
		task.setPlanIdentifier(planDefinition2.getIdentifier());
		task.setStatus(TaskStatus.READY);
		task.setCode("RACD Register Family");
		Task task2 = gson.fromJson(TestData.TASK_JSON, Task.class);
		task2.setStatus(TaskStatus.READY);
		task2.setCode("RACD Register Family");
		task2.setPlanIdentifier(planDefinition.getIdentifier());
		
		List<com.ibm.fhir.model.resource.Task> tasks = Arrays.asList(task, task2).stream()
		        .map(TaskConverter::convertTasktoFihrResource).collect(Collectors.toList());
		doReturn(tasks).when(taskDao).findAllTasksForEntity(entityId);
		doReturn(task2).when(taskDao).getTaskByIdentifier(task2.getIdentifier());
		doReturn(task).when(taskDao).getTaskByIdentifier(task.getIdentifier());
		doReturn(planDefinition2).when(planDao).findPlanByIdentifier(planDefinition2.getIdentifier());
		
		Assert.assertEquals(TaskStatus.READY, task2.getStatus());
		Assert.assertEquals("Not Visited", task2.getBusinessStatus());
		Assert.assertEquals("RACD Register Family", task2.getCode());
		
		Whitebox.setInternalState(planEvaluator, "queuingHelper", queuingHelper);
		planEvaluator = spy(planEvaluator);
		planEvaluator.evaluatePlan(planDefinition, eventQuestionnaire);
		
		ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
		verify(taskDao).findAllTasksForEntity(entityId);
		verify(planDao).findPlanByIdentifier(planDefinition2.getIdentifier());
		verify(taskDao).getTaskByIdentifier(task2.getIdentifier());
		
		verify(taskDao, times(2)).updateTask(taskArgumentCaptor.capture());
		assertEquals(2, taskArgumentCaptor.getAllValues().size());
		
		Iterator<Task> taskIterator = taskArgumentCaptor.getAllValues().iterator();
		while (taskIterator.hasNext()) {
			Task finalTask = taskIterator.next();
			Assert.assertEquals(TaskStatus.CANCELLED, finalTask.getStatus());
			Assert.assertEquals("Completed in another plan", finalTask.getBusinessStatus());
		}
		verify(planEvaluator).evaluatePlan(eq(planDefinition), eq(TriggerType.EVENT_SUBMISSION), any(), eq(eventQuestionnaire));
		verify(planEvaluator).evaluatePlan(eq(planDefinition2), eq(TriggerType.PLAN_ACTIVATION), any(), eq(eventQuestionnaire));
		
	}
}
