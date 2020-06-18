/**
 * 
 */
package org.smartregister.pathevaluator.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.domain.Action;
import org.smartregister.domain.Action.SubjectConcept;
import org.smartregister.domain.Condition;
import org.smartregister.domain.Expression;
import org.smartregister.domain.Jurisdiction;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.TaskDao;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.model.resource.Task;

/**
 * @author Samuel Githengi created on 06/16/20
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionHelperTest {
	
	private ActionHelper actionHelper;
	
	@Mock
	private Action action;
	
	@Mock
	private LocationDao locationDao;
	
	@Mock
	private ClientDao clientDao;
	
	@Mock
	private TaskDao taskDao;
	
	private SubjectConcept subjectConcept;
	
	private Jurisdiction jurisdiction;
	
	private Patient patient;
	
	private Condition condition;
	
	private Expression expression;
	
	@BeforeClass
	public static void bootstrap() {
		PathEvaluatorLibrary.init();
	}
	
	@Before
	public void setUp() {
		actionHelper = new ActionHelper();
		PathEvaluatorLibrary.getInstance().setLocationDao(locationDao);
		PathEvaluatorLibrary.getInstance().setClientDao(clientDao);
		PathEvaluatorLibrary.getInstance().setTaskDao(taskDao);
		subjectConcept = new SubjectConcept(ResourceType.JURISDICTION.value());
		jurisdiction = new Jurisdiction("12123");
		when(action.getSubjectCodableConcept()).thenReturn(subjectConcept);
		patient = TestData.createPatient();
		expression = Expression.builder().expression("Patient.name.family = 'John'").subjectConcept(subjectConcept).build();
		condition = Condition.builder().kind("applicability").expression(expression).build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetResourceTypeWithoutSubjectConcept() {
		when(action.getSubjectCodableConcept()).thenReturn(null);
		assertEquals(ResourceType.FAMILY, ActionHelper.getResourceType(action));
	}
	
	@Test
	public void testGetResourceType() {
		when(action.getSubjectCodableConcept()).thenReturn(subjectConcept);
		assertEquals(ResourceType.JURISDICTION, ActionHelper.getResourceType(action));
	}
	
	@Test
	public void testGetJurisdictionResources() {
		List<Location> expected = Collections.singletonList(TestData.createLocation());
		when(locationDao.getJurisdictions(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction));
		verify(locationDao).getJurisdictions(jurisdiction.getCode());
	}
	
	@Test
	public void testGetLocationResources() {
		subjectConcept.setText(ResourceType.LOCATION.value());
		List<Location> expected = Collections.singletonList(TestData.createLocation());
		when(locationDao.getLocations(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction));
		verify(locationDao).getLocations(jurisdiction.getCode());
	}
	
	@Test
	public void testGetFamilyResources() {
		subjectConcept.setText(ResourceType.FAMILY.value());
		List<RelatedPerson> expected = Collections.singletonList(TestData.createFamily());
		when(clientDao.findFamilyByJurisdiction(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction));
		verify(clientDao).findFamilyByJurisdiction(jurisdiction.getCode());
	}
	
	@Test
	public void testGetFamilyMemberResources() {
		subjectConcept.setText(ResourceType.FAMILY_MEMBER.value());
		List<Patient> expected = Collections.singletonList(TestData.createPatient());
		when(clientDao.findFamilyMemberyByJurisdiction(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction));
		verify(clientDao).findFamilyMemberyByJurisdiction(jurisdiction.getCode());
	}
	
	/** Condition resources tests **/
	
	@Test
	public void testGetJurisdictionConditionResources() {
		List<Location> expected = Collections.singletonList(TestData.createLocation());
		when(locationDao.getJurisdictions(patient, ResourceType.JURISDICTION)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient));
		verify(locationDao).getJurisdictions(patient, ResourceType.JURISDICTION);
	}
	
	@Test
	public void testGetLocationConditionResources() {
		subjectConcept.setText(ResourceType.LOCATION.value());
		List<Location> expected = Collections.singletonList(TestData.createLocation());
		when(locationDao.getLocations(patient, ResourceType.LOCATION)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient));
		verify(locationDao).getLocations(patient, ResourceType.LOCATION);
	}
	
	@Test
	public void testGetFamilyConditionResources() {
		subjectConcept.setText(ResourceType.FAMILY.value());
		List<RelatedPerson> expected = Collections.singletonList(TestData.createFamily());
		when(clientDao.getFamilies(patient, ResourceType.FAMILY)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient));
		verify(clientDao).getFamilies(patient, ResourceType.FAMILY);
	}
	
	@Test
	public void testGetFamilyMemberConditionResources() {
		subjectConcept.setText(ResourceType.FAMILY_MEMBER.value());
		List<Patient> expected = Collections.singletonList(TestData.createPatient());
		when(clientDao.getFamilyMembers(patient, ResourceType.FAMILY_MEMBER)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient));
		verify(clientDao).getFamilyMembers(patient, ResourceType.FAMILY_MEMBER);
	}
	
	@Test
	public void testGetTaskConditionResources() {
		subjectConcept.setText(ResourceType.TASK.value());
		List<Task> expected = Collections.singletonList(TestData.createTask());
		when(taskDao.getTasks(patient, ResourceType.TASK)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient));
		verify(taskDao).getTasks(patient, ResourceType.TASK);
	}
	
}
