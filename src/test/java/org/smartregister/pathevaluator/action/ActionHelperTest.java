/**
 * 
 */
package org.smartregister.pathevaluator.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.converters.EventConverter;
import org.smartregister.domain.Action;
import org.smartregister.domain.Action.SubjectConcept;
import org.smartregister.domain.Condition;
import org.smartregister.domain.Event;
import org.smartregister.domain.Expression;
import org.smartregister.domain.Jurisdiction;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TestData;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.ClientProvider;
import org.smartregister.pathevaluator.dao.EventDao;
import org.smartregister.pathevaluator.dao.EventProvider;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.LocationProvider;
import org.smartregister.pathevaluator.dao.StockDao;
import org.smartregister.pathevaluator.dao.StockProvider;
import org.smartregister.pathevaluator.dao.TaskDao;
import org.smartregister.pathevaluator.dao.TaskProvider;
import org.smartregister.utils.DateTypeConverter;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Identifier;

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
	
	@Mock
	private EventDao eventDao;
	
	@Mock
	private StockDao stockDao;

	@Mock
	private LocationProvider locationProvider;
	
	@Mock
	private ClientProvider clientProvider;
	
	@Mock
	private TaskProvider taskProvider;
	
	@Mock
	private EventProvider eventProvider;

	@Mock
	private StockProvider stockProvider;
	
	private SubjectConcept subjectConcept;
	
	private Jurisdiction jurisdiction;
	
	private Patient patient;

	private Bundle bundle;
	
	private Condition condition;
	
	private Expression expression;
	
	private QuestionnaireResponse questionnaireResponse;
	
	private String plan = UUID.randomUUID().toString();

	public static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.registerTypeAdapter(LocalDate.class, new DateTypeConverter()).create();
	
	@Before
	public void setUp() {
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao, stockDao);
		PathEvaluatorLibrary instance = PathEvaluatorLibrary.getInstance();
		Whitebox.setInternalState(instance, "locationProvider", locationProvider);
		Whitebox.setInternalState(instance, "clientProvider", clientProvider);
		Whitebox.setInternalState(instance, "taskProvider", taskProvider);
		Whitebox.setInternalState(instance, "eventProvider", eventProvider);
		Whitebox.setInternalState(instance, "stockProvider", stockProvider);
		when(locationProvider.getLocationDao()).thenReturn(locationDao);
		when(clientProvider.getClientDao()).thenReturn(clientDao);
		when(taskProvider.getTaskDao()).thenReturn(taskDao);
		when(stockProvider.getStockDao()).thenReturn(stockDao);
		when(eventProvider.getEventDao()).thenReturn(eventDao);

		actionHelper = new ActionHelper();
		subjectConcept = new SubjectConcept(ResourceType.JURISDICTION.value());
		jurisdiction = new Jurisdiction("12123");
		when(action.getSubjectCodableConcept()).thenReturn(subjectConcept);
		patient = TestData.createPatient();
		bundle = TestData.createBundle();
		questionnaireResponse = TestData.createResponse();
		expression = Expression.builder().expression("Patient.name.family = 'John'").subjectCodableConcept(subjectConcept)
		        .build();
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
		when(locationDao.findJurisdictionsById(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction,plan));
		verify(locationDao).findJurisdictionsById(jurisdiction.getCode());
	}
	
	@Test
	public void testGetLocationResources() {
		subjectConcept.setText(ResourceType.LOCATION.value());
		List<Location> expected = Collections.singletonList(TestData.createLocation());
		when(locationDao.findLocationByJurisdiction(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction,plan));
		verify(locationDao).findLocationByJurisdiction(jurisdiction.getCode());
	}
	
	@Test
	public void testGetFamilyResources() {
		subjectConcept.setText(ResourceType.FAMILY.value());
		List<Patient> expected = Collections.singletonList(TestData.createPatient());
		when(clientDao.findFamilyByJurisdiction(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction,plan));
		verify(clientDao).findFamilyByJurisdiction(jurisdiction.getCode());
	}
	
	@Test
	public void testGetFamilyMemberResources() {
		subjectConcept.setText(ResourceType.PERSON.value());
		List<Patient> expected = Collections.singletonList(TestData.createPatient());
		when(clientDao.findFamilyMemberyByJurisdiction(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction,plan));
		verify(clientDao).findFamilyMemberyByJurisdiction(jurisdiction.getCode());
	}
	
	@Test
	public void testGetQuestionnaireResources() {
		subjectConcept.setText(ResourceType.QUESTIONAIRRE_RESPONSE.value());
		List<QuestionnaireResponse> expected = Collections.singletonList(TestData.createResponse());
		when(eventDao.findEventsByJurisdictionIdAndPlan(jurisdiction.getCode(),plan)).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction,plan));
		verify(eventDao).findEventsByJurisdictionIdAndPlan(jurisdiction.getCode(),plan);
	}
	
	@Test
	public void testGetQuestionnaireResourcesForFamilyEvent() {
		subjectConcept.setText(ResourceType.LOCATION.value());
		Location location = TestData.createLocation();
	
		Coding coding = Coding.builder().code(Code.code("attribute")).build();
		CodeableConcept codeableConcept = CodeableConcept.builder().coding(coding).build();
		Patient person = TestData.createPatient().toBuilder()
		        .identifier(
		            Identifier.builder().type(codeableConcept).id("residence").value(com.ibm.fhir.model.type.String.of(location.getId())).build())
		        .build();
		System.out.print(person);
		List<Resource> persons = Collections.singletonList(person);
		List<Location> locations = Collections.singletonList(location);
		QuestionnaireResponse questionnaireResponse = TestData.createResponse().toBuilder().contained(persons).build();
		when(locationDao.findLocationsById(location.getId())).thenReturn(locations);
		assertEquals(locations, actionHelper.getSubjectResources(action, questionnaireResponse, null));
		verify(locationDao).findLocationsById(location.getId());
	}
	
	
	@Test
	public void testGetGlobalTasksForFamilyEvent() {
		subjectConcept.setText(ResourceType.GLOBAL_TASK.value());
		Task task=TestData.createTask();
		List<Task> expected = Collections.singletonList(task);
		
		QuestionnaireResponse questionnaireResponse = TestData.createResponse().toBuilder().subject(task.getFor()).build();
	
		when(taskProvider.getAllTasks(questionnaireResponse.getSubject().getReference().getValue())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources( action, questionnaireResponse, null));
		verify(taskProvider).getAllTasks(questionnaireResponse.getSubject().getReference().getValue());
	}

	@Test
	public void testGetBundleResources() {
		subjectConcept.setText(ResourceType.DEVICE.value());
		List<Bundle> expected = Collections.singletonList(TestData.createBundle());
		when(stockDao.findInventoryItemsInAJurisdiction(jurisdiction.getCode())).thenReturn(expected);
		assertEquals(expected, actionHelper.getSubjectResources(action, jurisdiction,plan));
		verify(stockDao).findInventoryItemsInAJurisdiction(jurisdiction.getCode());
	}
	
	/** Condition resources tests **/
	
	@Test
	public void testGetJurisdictionConditionResources() {
		List<Location> expected = Collections.singletonList(TestData.createLocation());
		when(locationProvider.getJurisdictions(patient, ResourceType.JURISDICTION)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient, plan));
		verify(locationProvider).getJurisdictions(patient, ResourceType.JURISDICTION);
	}
	
	@Test
	public void testGetLocationConditionResources() {
		subjectConcept.setText(ResourceType.LOCATION.value());
		List<Location> expected = Collections.singletonList(TestData.createLocation());
		when(locationProvider.getLocations(patient, ResourceType.LOCATION)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient, plan));
		verify(locationProvider).getLocations(patient, ResourceType.LOCATION);
	}
	
	@Test
	public void testGetFamilyConditionResources() {
		subjectConcept.setText(ResourceType.FAMILY.value());
		List<Patient> expected = Collections.singletonList(TestData.createPatient());
		when(clientProvider.getFamilies(patient, ResourceType.FAMILY)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient, plan));
		verify(clientProvider).getFamilies(patient, ResourceType.FAMILY);
	}
	
	@Test
	public void testGetFamilyMemberConditionResources() {
		subjectConcept.setText(ResourceType.PERSON.value());
		List<Patient> expected = Collections.singletonList(TestData.createPatient());
		when(clientProvider.getFamilyMembers(patient, ResourceType.PERSON)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient, plan));
		verify(clientProvider).getFamilyMembers(patient, ResourceType.PERSON);
	}
	
	@Test
	public void testGetTaskConditionResources() {
		subjectConcept.setText(ResourceType.TASK.value());
		List<Task> expected = Collections.singletonList(TestData.createTask());
		when(taskProvider.getTasks(patient, plan)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient, plan));
		verify(taskProvider).getTasks(patient, plan);
	}
	
	@Test
	public void testGetQuestionnaireConditionResources() {
		subjectConcept.setText(ResourceType.QUESTIONAIRRE_RESPONSE.value());
		List<QuestionnaireResponse> expected = Collections.singletonList(TestData.createResponse());
		when(eventProvider.getEvents(patient, plan)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient, plan));
		verify(eventProvider).getEvents(patient, plan);
	}
	
	@Test
	public void testGetQuestionnaireConditionResourcesV2() {
		subjectConcept.setText(ResourceType.QUESTIONAIRRE_RESPONSE.value());
		List<QuestionnaireResponse> expected = Collections.singletonList(TestData.createResponse());
		when(eventProvider.getEvents(questionnaireResponse, plan)).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, questionnaireResponse, plan));
		verify(eventProvider).getEvents(questionnaireResponse, plan);
	}
	
	@Test
	public void testGetGlobalTaskConditionResources() {
		subjectConcept.setText(ResourceType.GLOBAL_TASK.value());
		List<Task> expected = Collections.singletonList(TestData.createTask());
		when(taskProvider.getAllTasks(patient.getId())).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, patient, plan));
		verify(taskProvider).getAllTasks(patient.getId());
	}
	
	@Test
	public void testGetDeviceConditionResources() {
		subjectConcept.setText(ResourceType.DEVICE.value());
		List<Bundle> expected = Collections.singletonList(TestData.createBundle());
		when(stockProvider.getStocksAgainstServicePointId(anyString())).thenReturn(expected);
		assertEquals(expected, actionHelper.getConditionSubjectResources(condition, action, bundle, plan));
		verify(stockProvider).getStocksAgainstServicePointId(anyString());
	}

	@Test
	public void testGetSubjectResourcesWhenGivenTaskActionAndMosquittoCollectionQuestionnaire() {
		PlanDefinition planDefinition = gson.fromJson(TestData.MOSQUITTO_COLLECTION_CLOSE_PLAN, PlanDefinition.class);
		Event event = gson.fromJson(TestData.MOSQUITTO_COLLECTION_EVENT, Event.class);
		QuestionnaireResponse eventQuestionnaire = EventConverter.convertEventToEncounterResource(event);
		org.smartregister.domain.Task task = gson.fromJson(TestData.MOSQUITTO_COLLECTION_TASK, org.smartregister.domain.Task.class);

		when(taskDao.getTaskByIdentifier(task.getIdentifier())).thenReturn(task);

		// Call the actual method under test
		List<Task> tasks = (List<Task>) actionHelper.getSubjectResources(planDefinition.getActions().get(0), eventQuestionnaire, planDefinition.getIdentifier());

		// Perform verifications and assertions
		Assert.assertEquals(task.getIdentifier(), tasks.get(0).getId());
		verify(taskDao).getTaskByIdentifier(task.getIdentifier());
	}

	@Test
	public void testGetSubjectResourcesWithDeviceResourceType() {
		List<Bundle> expected = Collections.singletonList(TestData.createBundle());
		PlanDefinition planDefinition = gson.fromJson(TestData.EUSM_PLAN, PlanDefinition.class);
		Event event = gson.fromJson(TestData.FLAG_PROBLEM_EVENT, Event.class);
		QuestionnaireResponse eventQuestionnaire = EventConverter.convertEventToEncounterResource(event);
		when(stockDao.getStockById(anyString())).thenReturn(expected);
		List<Bundle> bundles = (List<Bundle>) actionHelper.getSubjectResources(planDefinition.getActions().get(1), eventQuestionnaire, planDefinition.getIdentifier());

		assertNotNull(bundles);
		verify(stockDao).getStockById(anyString());
	}

	@Test
	public void testGetSubjectResourcesWithJurisdictionalTaskShouldCallFindTasksByJurisdictionWithJurisdictionCodeOnly() {
		Jurisdiction jurisdiction = new Jurisdiction("jurisdiction-code");
		Action action = new Action();
		action.setSubjectCodableConcept(new SubjectConcept(ResourceType.JURISDICTIONAL_TASK.value()));

		actionHelper.getSubjectResources(action, jurisdiction, null);

		ArgumentCaptor<String> jurisdictionCodeCaptor = ArgumentCaptor.forClass(String.class);
		verify(taskDao).findTasksByJurisdiction(jurisdictionCodeCaptor.capture());

		Assert.assertEquals(jurisdiction.getCode(), jurisdictionCodeCaptor.getValue());
	}
	
}
