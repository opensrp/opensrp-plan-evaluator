package org.smartregister.pathevaluator.plan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.converters.EventConverter;
import org.smartregister.domain.Event;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.dao.*;
import org.smartregister.utils.DateTypeConverter;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import java.util.UUID;

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
		Mockito.doReturn(task).when(taskDao).getTaskByIdentifier(task.getIdentifier());

		Assert.assertEquals("READY", task.getStatus().name());
		Assert.assertEquals("Not Visited", task.getBusinessStatus());

		planEvaluator.evaluatePlan(planDefinition, eventQuestionnaire);

		ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
		Mockito.verify(taskDao).updateTask(taskArgumentCaptor.capture());

		Task finalTask = taskArgumentCaptor.getValue();

		Assert.assertEquals("COMPLETED", finalTask.getStatus().name());
		Assert.assertEquals("Complete", finalTask.getBusinessStatus());
	}
}
