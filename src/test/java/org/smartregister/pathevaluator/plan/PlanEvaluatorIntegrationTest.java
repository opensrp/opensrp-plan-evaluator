package org.smartregister.pathevaluator.plan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.converters.EventConverter;
import org.smartregister.domain.Event;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.domain.Task;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.EventDao;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.TaskDao;
import org.smartregister.utils.DateTypeConverter;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class PlanEvaluatorIntegrationTest {

	private PlanEvaluator planEvaluator;
	private String plan = UUID.randomUUID().toString();

	private String username = UUID.randomUUID().toString();

	@Mock
	private LocationDao locationDao;

	@Mock
	private ClientDao clientDao;

	@Mock
	private TaskDao taskDao;

	@Mock
	private EventDao eventDao;

	private String planDefinitionJson = "{\"date\":\"2020-10-20\",\"effectivePeriod\":{\"end\":\"2021-10-19\",\"start\":\"2020-10-20\"},\"experimental\":false,\"goal\":[{\"description\":\"Complete the Day 2 Visit form for each child.\",\"id\":\"Day_2_Visit\",\"priority\":\"medium-priority\",\"target\":[{\"detail\":{\"detailQuantity\":{\"comparator\":\"&gt;=\",\"unit\":\"Percent\",\"value\":80}},\"due\":\"2021-10-19\",\"measure\":\"Percent of children with completed Day 2 Visit form completed.\"}]}],\"identifier\":\"d652d9ae-2ff9-41e4-9bb3-fd4cd2ea0e2e\",\"jurisdiction\":[],\"name\":\"Sample_PNC_Plan_Template2\",\"serverVersion\":7,\"status\":\"active\",\"title\":\"Sample PNC Plan Template\",\"useContext\":[{\"code\":\"interventionType\",\"valueCodableConcept\":\"Linked-PNC\"},{\"code\":\"taskGenerationStatus\",\"valueCodableConcept\":\"internal\"}],\"version\":\"1\",\"action\":[{\"identifier\":\"c83deeb4-d580-529e-a6f0-c0c649585014\",\"prefix\":5,\"title\":\"Mosquito Collection Close Task\",\"description\":\"Close Mosquito Task\",\"code\":\"Mosquito Collection\",\"timingPeriod\":{\"start\":\"2020-11-17\",\"end\":\"2020-11-24\"},\"reason\":\"Investigation\",\"goalId\":\"Mosquito_Collection\",\"subjectCodableConcept\":{\"text\":\"Task\"},\"trigger\":[{\"type\":\"named-event\",\"name\":\"event-submission\",\"expression\":{\"description\":\"Trigger when a Mosquito Collection event is submitted\",\"expression\":\"questionnaire = 'mosquito_collection'\"}}],\"condition\":[{\"kind\":\"applicability\",\"subjectCodableConcept\":{\"text\":\"Task\"},\"expression\":{\"description\":\"Mosquito Collection event\",\"expression\":\"$this.contained.where(Task.code.text.value ='Mosquito Collection').exists()\"}}],\"dynamicValue\":[{\"path\":\"businessStatus\",\"expression\":{\"expression\":\"$this.item.where(linkId='task_business_status').answer[0].value.value\"}},{\"path\":\"status\",\"expression\":{\"expression\":\"'Completed'\"}}],\"definitionUri\":\"mosquito_collection_form.json\",\"type\":\"update\"}]}";
	private String eventJson = "{\"_id\": \"6c27e088-b25c-4074-9b0b-cc6e6a24f864\", \"obs\": [{\"set\": [], \"values\": [\"1\"], \"fieldCode\": \"eligibleMos\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"eligibleMos\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"Complete\"], \"fieldCode\": \"task_business_status\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"task_business_status\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"04-11-2020\"], \"fieldCode\": \"trap_start\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"trap_start\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"Complete\"], \"fieldCode\": \"business_status\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"business_status\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"2020-11-04 14:59:15\"], \"fieldCode\": \"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"start\", \"saveObsAsArray\": false, \"formSubmissionField\": \"start\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"2020-11-04 14:59:26\"], \"fieldCode\": \"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"end\", \"saveObsAsArray\": false, \"formSubmissionField\": \"end\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"868190043418417\"], \"fieldCode\": \"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"deviceid\", \"saveObsAsArray\": false, \"formSubmissionField\": \"deviceid\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"520001918834913\"], \"fieldCode\": \"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"subscriberid\", \"saveObsAsArray\": false, \"formSubmissionField\": \"subscriberid\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"8966001504183929136F\"], \"fieldCode\": \"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"simserial\", \"saveObsAsArray\": false, \"formSubmissionField\": \"simserial\", \"humanReadableValues\": []}], \"_rev\": \"v1\", \"team\": \"raitest\", \"type\": \"Event\", \"teamId\": \"a3bf8a41-3753-4b1d-816b-3999a30250c9\", \"details\": {\"taskStatus\": \"READY\", \"location_id\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"form_version\": \"0.0.1\", \"locationUUID\": \"044235d4-8c50-433a-ae3a-f925726a79f1\", \"appVersionName\": \"5.3.2\", \"planIdentifier\": \"42a68877-0376-4379-9660-9be7cc70f736\", \"taskIdentifier\": \"783a5d98-1ba6-4315-936b-57432996bf80\", \"locationVersion\": \"0\", \"taskBusinessStatus\": \"Not Visited\"}, \"version\": 1604476766559, \"duration\": 0, \"eventDate\": \"2020-11-04T00:00:00.000+07:00\", \"eventType\": \"mosquito_collection\", \"entityType\": \"Structure\", \"locationId\": \"9dc012f6-83fa-44a3-bec1-bfb948f05f6d\", \"providerId\": \"raitest\", \"dateCreated\": \"2020-11-25T10:47:03.666+07:00\", \"identifiers\": {}, \"baseEntityId\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"serverVersion\": 1600352722388, \"formSubmissionId\": \"a0b832e2-d1dd-453c-bb83-d862affe1a41\", \"clientDatabaseVersion\": 10, \"clientApplicationVersion\": 22}";
	private String taskJson = "{\"for\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"code\": \"Mosquito Collection\", \"focus\": \"6b931eb4-b361-4597-a412-73e02629d97d\", \"owner\": \"raitest\", \"status\": \"Ready\", \"priority\": \"routine\", \"authoredOn\": \"2020-11-25T10:47:10.682+07:00\", \"identifier\": \"783a5d98-1ba6-4315-936b-57432996bf80\", \"syncStatus\": \"Created\", \"description\": \"กำหนดสถานที่เก็บตัวอย่างยุง และการบันทึกผล\", \"structureId\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"lastModified\": \"2020-11-25T10:47:10.682+07:00\", \"serverVersion\": 1600354240185, \"businessStatus\": \"Not Visited\", \"planIdentifier\": \"42a68877-0376-4379-9660-9be7cc70f736\", \"executionPeriod\": {\"start\": \"2020-11-04T14:59:00.000+07:00\"}, \"groupIdentifier\": \"9dc012f6-83fa-44a3-bec1-bfb948f05f6d\"}";

	public static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.registerTypeAdapter(LocalDate.class, new DateTypeConverter()).create();

	@Before
	public void setUp() {
		PathEvaluatorLibrary.init(locationDao, clientDao, taskDao, eventDao);
		planEvaluator = new PlanEvaluator(username);
	}

	@Test
	public void evaluatePlanShouldUpdateTaskBasedOnEventObsValues() {
		PlanDefinition planDefinition = gson.fromJson(planDefinitionJson, PlanDefinition.class);
		Event event = gson.fromJson(eventJson, Event.class);
		QuestionnaireResponse eventQuestionnaire = EventConverter.convertEventToEncounterResource(event);

		Task task = gson.fromJson(taskJson, Task.class);
		Mockito.doReturn(task).when(taskDao).getTaskByIdentifier(Mockito.anyString());

		Assert.assertEquals("READY", task.getStatus().name());
		Assert.assertEquals("Not Visited", task.getBusinessStatus());

		planEvaluator.evaluatePlan(planDefinition, eventQuestionnaire);

		ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
		Mockito.verify(taskDao).updateTask(taskArgumentCaptor.capture());

		Task finalTask = taskArgumentCaptor.getValue();

		Assert.assertEquals("COMPLETED", finalTask.getStatus().name());
		Assert.assertEquals("Complete", finalTask.getBusinessStatus());
	}
}
