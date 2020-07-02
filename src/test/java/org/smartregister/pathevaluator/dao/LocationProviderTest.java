/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TestData;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.type.Identifier;

/**
 * @author Samuel Githengi created on 06/18/20
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationProviderTest {
	
	@Mock
	private LocationDao locationDao;
	
	private LocationProvider locationProvider;
	
	private Patient patient = TestData.createPatient();
	
	private Location location = TestData.createLocation();
	
	private Task task = TestData.createTask();

	private List<Location> expectedLocation = Collections.singletonList(location);

	private QuestionnaireResponse questionnaireResponse = TestData.createResponse();
	
	private List<Location> expected = Collections.singletonList(location);
	
	@Before
	public void setUp() {
		locationProvider = new LocationProvider(locationDao);
	}
	
	@Test
	public void testGetLocationsForJurisdiction() {
		when(locationDao.findLocationByJurisdiction(location.getId())).thenReturn(expected);
		assertEquals(expected, locationProvider.getLocations(location, ResourceType.JURISDICTION));
		verify(locationDao).findLocationByJurisdiction(location.getId());
	}
	
	@Test
	public void testGetLocationsForFamily() {
		patient = patient.toBuilder()
		        .identifier(Identifier.builder().id(LocationProvider.RESIDENCE).value(of(location.getId())).build()).build();
		when(locationDao.findJurisdictionsById(location.getId())).thenReturn(expected);
		assertEquals(expected, locationProvider.getLocations(patient, ResourceType.FAMILY));
		verify(locationDao).findJurisdictionsById(location.getId());
	}
	
	@Test
	public void testGetLocationsForFamilyMember() {
		
		patient = patient.toBuilder()
		        .identifier(Identifier.builder().id(LocationProvider.RESIDENCE).value(of(location.getId())).build()).build();
		when(locationDao.findJurisdictionsById(location.getId())).thenReturn(expected);
		assertEquals(expected, locationProvider.getLocations(patient, ResourceType.PERSON));
		verify(locationDao).findJurisdictionsById(location.getId());
	}
	
	@Test
	public void testGetLocationsForFamilyMemberWithoutResidence() {
		assertNull(locationProvider.getLocations(patient, ResourceType.FAMILY));
		verifyNoInteractions(locationDao);
	}
	
	@Test
	public void testGetLocationsForTask() {
		when(locationDao.findLocationsById(task.getFor().getReference().getValue())).thenReturn(expected);
		assertEquals(expected, locationProvider.getLocations(task, ResourceType.TASK));
		verify(locationDao).findLocationsById(task.getFor().getReference().getValue());
	}

	@Test
	public void testGetLocationsForQuestionarreResponse() {
		when(locationDao.findLocationsById(questionnaireResponse.getId())).thenReturn(expected);
		assertEquals(expected, locationProvider.getLocations(questionnaireResponse, ResourceType.QUESTIONAIRRE_RESPONSE));
		verify(locationDao).findLocationsById(questionnaireResponse.getId());
	}

	@Test
	public void testGetLocationsForLocations() {
		assertEquals(expected, locationProvider.getLocations(location, ResourceType.LOCATION));
		verify(locationDao,never()).findLocationsById(location.getId());
	}
	
	@Test
	public void testGetJurisdictionsForFamily() {
		patient = patient.toBuilder()
		        .identifier(Identifier.builder().id(LocationProvider.LOCATION_ID).value(of(location.getId())).build())
		        .build();
		when(locationDao.findJurisdictionsById(location.getId())).thenReturn(expected);
		assertEquals(expected, locationProvider.getJurisdictions(patient, ResourceType.FAMILY));
		verify(locationDao).findJurisdictionsById(location.getId());
	}
	
	@Test
	public void testGetJurisdictionsForFamilyWithoutLocationIdentifier() {
		assertNull(locationProvider.getJurisdictions(patient, ResourceType.FAMILY));
		verifyNoInteractions(locationDao);
	}
	
	@Test
	public void testGetJurisdictionsForFamilyMember() {
		patient = patient.toBuilder()
		        .identifier(Identifier.builder().id(LocationProvider.LOCATION_ID).value(of(location.getId())).build())
		        .build();
		when(locationDao.findJurisdictionsById(location.getId())).thenReturn(expected);
		assertEquals(expected, locationProvider.getJurisdictions(patient, ResourceType.PERSON));
		verify(locationDao).findJurisdictionsById(location.getId());
	}
	
	@Test
	public void testGetJurisdictionsForTask() {
		when(locationDao.findJurisdictionsById(task.getGroupIdentifier().getValue().getValue())).thenReturn(expected);
		assertEquals(expected, locationProvider.getJurisdictions(task, ResourceType.TASK));
		verify(locationDao).findJurisdictionsById(task.getGroupIdentifier().getValue().getValue());
	}
}
