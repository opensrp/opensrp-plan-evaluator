package org.smartregister.pathevaluator.task;

import com.ibm.fhir.model.resource.Patient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.domain.Action;
import org.smartregister.domain.Task;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.dao.*;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

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
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao, "testUser");
		PathEvaluatorLibrary instance = PathEvaluatorLibrary.getInstance();
		taskHelper = new TaskHelper();
		patient = TestData.createPatient();
	}

	@Test(expected = Test.None.class)
	public void testGenerateTask() {
		String planIdentifier = UUID.randomUUID().toString();
		String jurisdiction = "12123";
		Mockito.doNothing().when(taskDao).saveTask(any(Task.class));
		taskHelper.generateTask(patient,action, planIdentifier,jurisdiction);
	}
}
