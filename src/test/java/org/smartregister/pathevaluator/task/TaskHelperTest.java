package org.smartregister.pathevaluator.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.smartregister.pathevaluator.TestData.createPlanV1;

import java.util.UUID;

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
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
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
	
	@Before
	public void setUp() {
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao);
		taskHelper = new TaskHelper();
		patient = TestData.createPatient();
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
}
