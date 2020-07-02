/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.smartregister.pathevaluator.TestData.createResponse;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.domain.Jurisdiction;
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
public class ClientProviderTest {
	
	@Mock
	private ClientDao clientDao;
	
	private ClientProvider clientProvider;
	
	private Patient patient = TestData.createPatient();
	
	private Location location = TestData.createLocation();
	
	private Task task = TestData.createTask();

	private QuestionnaireResponse questionnaireResponse = createResponse();

	private List<Patient> expected = Collections.singletonList(patient);
	
	@Before
	public void setUp() {
		clientProvider = new ClientProvider(clientDao);
	}
	
	@Test
	public void testgetFamiliesForJurisdiction() {
		when(clientDao.findFamilyByJurisdiction(location.getId())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilies(location, ResourceType.JURISDICTION));
		verify(clientDao).findFamilyByJurisdiction(location.getId());
	}
	
	@Test
	public void testgetFamiliesForLocation() {
		when(clientDao.findFamilyByResidence(location.getId())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilies(location, ResourceType.LOCATION));
		verify(clientDao).findFamilyByResidence(location.getId());
	}
	
	@Test
	public void testgetFamilyForFamilyMember() {
		String familyId = UUID.randomUUID().toString();
		patient = patient.toBuilder().identifier(Identifier.builder().id(ClientProvider.FAMILY).value(of(familyId)).build())
		        .build();
		when(clientDao.findClientById(familyId)).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilies(patient, ResourceType.PERSON));
		verify(clientDao).findClientById(familyId);
	}
	
	@Test
	public void testgetFamilyForFamilyMemberWithoutRelationship() {
		assertNull(clientProvider.getFamilies(patient, ResourceType.PERSON));
		verifyNoInteractions(clientDao);
	}
	
	@Test
	public void testGetFamiliesForTask() {
		when(clientDao.findClientById(task.getFor().getReference().getValue())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilies(task, ResourceType.TASK));
		verify(clientDao).findClientById(task.getFor().getReference().getValue());
	}

	@Test
	public void testGetFamiliesForQuestionnareResponse() {
		when(clientDao.findClientById(questionnaireResponse.getId())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilies(questionnaireResponse, ResourceType.QUESTIONAIRRE_RESPONSE));
		verify(clientDao).findClientById(questionnaireResponse.getId());
	}

	@Test
	public void testGetFamiliesForFamily() {
		assertEquals(expected, clientProvider.getFamilies(patient, ResourceType.FAMILY));
		verify(clientDao,never()).findClientById(task.getFor().getReference().getValue());
	}
	
	@Test
	public void testGetFamilyMembersForFamily() {
		when(clientDao.findClientByRelationship(ClientProvider.FAMILY, patient.getId())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilyMembers(patient, ResourceType.FAMILY));
		verify(clientDao).findClientByRelationship(ClientProvider.FAMILY, patient.getId());
	}
	
	@Test
	public void testGetFamilyMembersForLocation() {
		when(clientDao.findFamilyMemberByResidence(location.getId())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilyMembers(location, ResourceType.LOCATION));
		verify(clientDao).findFamilyMemberByResidence(location.getId());
	}
	
	@Test
	public void testGetFamilyMembersForTask() {
		when(clientDao.findClientById(task.getFor().getReference().getValue())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilyMembers(task, ResourceType.TASK));
		verify(clientDao).findClientById(task.getFor().getReference().getValue());
	}

	@Test
	public void testGetFamilyMembersForQuestionaarreResponse() {
		when(clientDao.findClientById(questionnaireResponse.getId())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilyMembers(questionnaireResponse, ResourceType.QUESTIONAIRRE_RESPONSE));
		verify(clientDao).findClientById(questionnaireResponse.getId());
	}

	@Test
	public void testGetFamilyMembersForJurisdiction() {
		when(clientDao.findFamilyMemberyByJurisdiction(task.getId())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilyMembers(task, ResourceType.JURISDICTION));
		verify(clientDao).findFamilyMemberyByJurisdiction(task.getId());
	}

	@Test
	public void testGetFamiliesForPerson() {
		assertEquals(expected, clientProvider.getFamilyMembers(patient, ResourceType.PERSON));
		verify(clientDao,never()).findClientById(patient.getId());
	}

}
