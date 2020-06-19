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
import org.smartregister.pathevaluator.TestData;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.QuestionnaireResponse;

/**
 * @author Samuel Githengi created on 06/19/20
 */
@RunWith(MockitoJUnitRunner.class)
public class EventProviderTest {
	
	private EventProvider eventProvider;
	
	@Mock
	private EventDao eventDao;
	
	@Before
	public void setUp() {
		eventProvider = new EventProvider(eventDao);
	}
	
	@Test
	public void testGetEvents() {
		Patient patient = TestData.createPatient();
		List<QuestionnaireResponse> expected = Collections.singletonList(TestData.createResponse());
		String planIdentifier = UUID.randomUUID().toString();
		when(eventDao.findEventsByEntityIdAndPlan(patient.getId(), planIdentifier)).thenReturn(expected);
		assertEquals(expected, eventProvider.getEvents(patient, planIdentifier));
		verify(eventDao).findEventsByEntityIdAndPlan(patient.getId(), planIdentifier);
	}
}
