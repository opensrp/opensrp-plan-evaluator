/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TestData;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Task;

/**
 * @author Samuel Githengi created on 06/18/20
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskProviderTest {
	
	@Mock
	private TaskDao taskDao;
	
	private TaskProvider taskProvider;
	
	@Before
	public void setUp() {
		taskProvider = new TaskProvider(taskDao);
	}
	
	@Test
	public void testGetTasks() {
		Patient patient = TestData.createPatient();
		List<Task> expected = Collections.singletonList(TestData.createTask());
		String planIdentifier = UUID.randomUUID().toString();
		when(taskDao.findTasksForEntity(patient.getId(), planIdentifier)).thenReturn(expected);
		assertEquals(expected, taskProvider.getTasks(patient, ResourceType.FAMILY_MEMBER, planIdentifier));
		verify(taskDao).findTasksForEntity(patient.getId(), planIdentifier);
	}
}
