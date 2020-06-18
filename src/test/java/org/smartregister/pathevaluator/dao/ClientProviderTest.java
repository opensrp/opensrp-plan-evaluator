/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
		assertEquals(expected, clientProvider.getFamilies(patient, ResourceType.FAMILY_MEMBER));
		verify(clientDao).findClientById(familyId);
	}
	
	@Test
	public void testgetFamilyForFamilyMemberWithoutRelationship() {
		assertNull(clientProvider.getFamilies(patient, ResourceType.FAMILY));
		verifyNoInteractions(clientDao);
	}
	
	@Test
	public void testGetFamiliesForTask() {
		when(clientDao.findClientById(task.getFor().getReference().getValue())).thenReturn(expected);
		assertEquals(expected, clientProvider.getFamilies(task, ResourceType.TASK));
		verify(clientDao).findClientById(task.getFor().getReference().getValue());
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
}
