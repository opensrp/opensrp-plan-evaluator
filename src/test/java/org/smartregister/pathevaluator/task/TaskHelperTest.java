package org.smartregister.pathevaluator.task;

import com.ibm.fhir.model.resource.Patient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.domain.Action;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.dao.*;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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
	private Action action;
	
	private Patient patient;
	
	@Before
	public void setUp() {
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao);
		PathEvaluatorLibrary instance = PathEvaluatorLibrary.getInstance();
		taskHelper = new TaskHelper();
		patient = TestData.createPatient();
	}
	
	@Test
	public void testGenerateTask() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		Mockito.doNothing().when(taskDao).saveTask(any(Task.class));
		taskHelper.generateTask(patient, action, planIdentifier, jurisdiction, "testUser");
	}

	@Test
	public void testGenerateTaskWithDefaultBusinessStatus() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		PlanDefinition planDefinition = createPlanV1();
		Mockito.doNothing().when(taskDao).saveTask(any(Task.class));
		taskHelper.generateTask(patient, planDefinition.getActions().get(0), planIdentifier, jurisdiction, "testUser");
	}

	@Test
	public void testShouldNotGenerateTask() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		PlanDefinition planDefinition = createPlanV1();
		when(taskDao.checkIfTaskExists(anyString(),anyString(),anyString())).thenReturn(true);
		taskHelper.generateTask(patient, planDefinition.getActions().get(0), planIdentifier, jurisdiction, "testUser");
		verify(taskDao, never()).saveTask(any(Task.class));
	}
}
