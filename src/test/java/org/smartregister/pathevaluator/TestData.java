/**
 * 
 */
package org.smartregister.pathevaluator;

import static com.ibm.fhir.model.type.String.of;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ibm.fhir.model.resource.*;
import com.ibm.fhir.model.type.code.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.smartregister.domain.Action;
import org.smartregister.domain.DynamicValue;
import org.smartregister.domain.Expression;
import org.smartregister.domain.PlanDefinition;
import org.smartregister.utils.DateTypeConverter;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.QuestionnaireResponse.Item;
import com.ibm.fhir.model.resource.QuestionnaireResponse.Item.Answer;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public class TestData {
	
	public static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
	        .registerTypeAdapter(LocalDate.class, new DateTypeConverter()).create();
	
	public static String plan = "{\"identifier\":\"d18f15ec-afaf-42f3-ba96-d207c456645b\",\"version\":\"2\",\"name\":\"A1-59ad4fa0-1945-4b50-a6e3-a056a7cdceb2-2019-09-09\",\"title\":\"A1 - Ban Khane Chu OA - 2019-09-09(deprecated)\",\"status\":\"retired\",\"date\":\"2019-09-09\",\"effectivePeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-29\"},\"useContext\":[{\"code\":\"interventionType\",\"valueCodableConcept\":\"FI\"},{\"code\":\"fiStatus\",\"valueCodableConcept\":\"A1\"},{\"code\":\"fiReason\",\"valueCodableConcept\":\"Routine\"},{\"code\":\"caseNum\",\"valueCodableConcept\":\"3336\"},{\"code\":\"opensrpEventId\",\"valueCodableConcept\":\"75049cd6-d77b-4239-9092-7bd1aa0d438c\"},{\"code\":\"taskGenerationStatus\",\"valueCodableConcept\":\"True\"}],\"jurisdiction\":[{\"code\":\"59ad4fa0-1945-4b50-a6e3-a056a7cdceb2\"}],\"serverVersion\":1568110931837,\"goal\":[{\"id\":\"Case_Confirmation\",\"description\":\"Confirm the index case\",\"priority\":\"medium-priority\",\"target\":[{\"measure\":\"Number of cases confirmed\",\"detail\":{\"detailQuantity\":{\"value\":1,\"comparator\":\">=\",\"unit\":\"case(s)\"}},\"due\":\"2019-09-19\"}]},{\"id\":\"RACD_register_families\",\"description\":\"Register all families and family members in all residential structures enumerated or added (100%) within operational area\",\"priority\":\"medium-priority\",\"target\":[{\"measure\":\"Percent of residential structures with full family registration\",\"detail\":{\"detailQuantity\":{\"value\":100,\"comparator\":\">=\",\"unit\":\"Percent\"}},\"due\":\"2019-09-29\"}]},{\"id\":\"RACD_Blood_Screening\",\"description\":\"Visit all residential structures (100%) within a 1 km radius of a confirmed index case and test each registered person\",\"priority\":\"medium-priority\",\"target\":[{\"measure\":\"Number of registered people tested\",\"detail\":{\"detailQuantity\":{\"value\":50,\"comparator\":\">=\",\"unit\":\"Person(s)\"}},\"due\":\"2019-09-29\"}]},{\"id\":\"RACD_bednet_distribution\",\"description\":\"Visit 100% of residential structures in the operational area and provide nets\",\"priority\":\"medium-priority\",\"target\":[{\"measure\":\"Percent of residential structures received nets\",\"detail\":{\"detailQuantity\":{\"value\":90,\"comparator\":\">=\",\"unit\":\"Percent\"}},\"due\":\"2019-09-29\"}]},{\"id\":\"Larval_Dipping\",\"description\":\"Perform a minimum of three larval dipping activities in the operational area\",\"priority\":\"medium-priority\",\"target\":[{\"measure\":\"Number of larval dipping activities completed\",\"detail\":{\"detailQuantity\":{\"value\":3,\"comparator\":\">=\",\"unit\":\"activit(y|ies)\"}},\"due\":\"2019-09-29\"}]},{\"id\":\"Mosquito_Collection\",\"description\":\"Set a minimum of three mosquito collection traps and complete the mosquito collection process\",\"priority\":\"medium-priority\",\"target\":[{\"measure\":\"Number of mosquito collection activities completed\",\"detail\":{\"detailQuantity\":{\"value\":3,\"comparator\":\">=\",\"unit\":\"activit(y|ies)\"}},\"due\":\"2019-09-29\"}]},{\"id\":\"BCC_Focus\",\"description\":\"Complete at least 1 BCC activity for the operational area\",\"priority\":\"medium-priority\",\"target\":[{\"measure\":\"Number of BCC Activities Completed\",\"detail\":{\"detailQuantity\":{\"value\":1,\"comparator\":\">=\",\"unit\":\"activit(y|ies)\"}},\"due\":\"2019-09-29\"}]}],\"action\":[{\"identifier\":\"baebfd98-2f1f-4a5e-9478-680f13b45697\",\"prefix\":1,\"title\":\"Case Confirmation\",\"description\":\"Confirm the index case\",\"code\":\"Case Confirmation\",\"timingPeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-19\"},\"reason\":\"Investigation\",\"goalId\":\"Case_Confirmation\",\"subjectCodableConcept\":{\"text\":\"Case_Confirmation\"},\"taskTemplate\":\"Case_Confirmation\"},{\"identifier\":\"9f554b5a-0d00-4f18-996f-30dce50e57d8\",\"prefix\":2,\"title\":\"Family Registration\",\"description\":\"Register all families & family members in all residential structures enumerated (100%) within the operational area\",\"code\":\"RACD Register Family\",\"timingPeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-29\"},\"reason\":\"Investigation\",\"goalId\":\"RACD_register_families\",\"subjectCodableConcept\":{\"text\":\"Residential_Structure\"},\"taskTemplate\":\"RACD_register_families\"},{\"identifier\":\"28164940-e2cb-4622-8153-b6ac8ff6e940\",\"prefix\":3,\"title\":\"Blood screening\",\"description\":\"Visit all residential structures (100%) within a 1 km radius of a confirmed index case and test each registered person\",\"code\":\"Blood Screening\",\"timingPeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-29\"},\"reason\":\"Investigation\",\"goalId\":\"RACD_Blood_Screening\",\"subjectCodableConcept\":{\"text\":\"Person\"},\"taskTemplate\":\"RACD_Blood_Screening\"},{\"identifier\":\"b9f68512-376f-49c9-bb53-80d9e81a73d2\",\"prefix\":4,\"title\":\"Bednet Distribution\",\"description\":\"Visit 100% of residential structures in the operational area and provide nets\",\"code\":\"Bednet Distribution\",\"timingPeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-29\"},\"reason\":\"Routine\",\"goalId\":\"RACD_bednet_distribution\",\"subjectCodableConcept\":{\"text\":\"Residential_Structure\"},\"taskTemplate\":\"Bednet_Distribution\"},{\"identifier\":\"3b3f265d-05fe-4c16-a5a1-141bac82f128\",\"prefix\":5,\"title\":\"Larval Dipping\",\"description\":\"Perform a minimum of three larval dipping activities in the operational area\",\"code\":\"Larval Dipping\",\"timingPeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-29\"},\"reason\":\"Investigation\",\"goalId\":\"Larval_Dipping\",\"subjectCodableConcept\":{\"text\":\"Breeding_Site\"},\"taskTemplate\":\"Larval_Dipping\"},{\"identifier\":\"c295655a-8630-4bc1-a5fd-79b22a9b1125\",\"prefix\":6,\"title\":\"Mosquito Collection\",\"description\":\"Set a minimum of three mosquito collection traps and complete the mosquito collection process\",\"code\":\"Mosquito Collection\",\"timingPeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-29\"},\"reason\":\"Investigation\",\"goalId\":\"Mosquito_Collection\",\"subjectCodableConcept\":{\"text\":\"Mosquito_Collection_Point\"},\"taskTemplate\":\"Mosquito_Collection_Point\"},{\"identifier\":\"8b899c73-56c8-40a2-a738-6a62b459e8f8\",\"prefix\":7,\"title\":\"Behaviour Change Communication\",\"description\":\"Conduct BCC activity\",\"code\":\"BCC\",\"timingPeriod\":{\"start\":\"2019-09-09\",\"end\":\"2019-09-29\"},\"reason\":\"Investigation\",\"goalId\":\"BCC_Focus\",\"subjectCodableConcept\":{\"text\":\"Operational_Area\"},\"taskTemplate\":\"BCC_Focus\"}]}";

	public static String plan_1 = "{\n"
			+ "\"identifier\": \"3204259e-1109-4baf-934b-05662d51bbd0\",\n"
			+ "\"version\": \"1\",\n"
			+ "\"name\": \"MDA-2020-06-23-Dynamic-Task-Test-Plan\",\n"
			+ "\"title\": \"MDA 2020-06-23 Dynamic Task Test Plan\",\n"
			+ "\"status\": \"active\",\n"
			+ "\"date\": \"2020-06-23\",\n"
			+ "\"effectivePeriod\": {\n"
			+ "\"start\": \"2020-06-23\",\n"
			+ "\"end\": \"2020-12-31\"\n"
			+ "},\n"
			+ "\"useContext\": [],\n"
			+ "\"jurisdiction\": [{\n"
			+ "\"code\": \"45017166-cc14-4f2f-b83f-4a72ce17bf91\"\n"
			+ "}],\n"
			+ "\"action\": [{\n"
			+ "\"identifier\": \"3802af3a-f40c-4705-849e-1727b603bd4e\",\n"
			+ "\"prefix\": 1,\n"
			+ "\"title\": \"Family Registration\",\n"
			+ "\"description\": \"Register all families and family members in all residential structures enumerated (100%) within the operational area\",\n"
			+ "\"code\": \"RACD Register Family\",\n"
			+ "\"trigger\": [{\n"
			+ "\"type\": \"named-event\",\n"
			+ "\"name\": \"plan_activation\"\n"
			+ "},\n"
			+ "{\n"
			+ "\"type\": \"named-event\",\n"
			+ "\"name\": \"event-submission\",\n"
			+ "\"condition\": {\n"
			+ "\"expression\": \"questionnaire = 'Register_Structure'\"\n"
			+ "}\n"
			+ "}\n"
			+ "],\n"
			+ "\"condition\": [{\n"
			+ "\"kind\": \"applicability\",\n"
			+ "\"expression\": {\n"
			+ "\"expression\": \"Location.type.where(id='locationType').text = 'Residential Structure'\"\n"
			+ "}\n"
			+ "}],\n"
			+ "\"timingPeriod\": {\n"
			+ "\"start\": \"2020-06-04\",\n"
			+ "\"end\": \"2020-10-01\"\n"
			+ "},\n"
			+ "\"reason\": \"Routine\",\n"
			+ "\"goalId\": \"RACD_register_all_families\",\n"
			+ "\"subjectCodableConcept\": {\n"
			+ "\"text\": \"Location\"\n"
			+ "},\n"
			+ "\"definitionUri\": \"zambia_family_register.json\",\n"
			+ "\"dynamicValue\": [{\n"
			+ "\"expression\": {\n"
			+ "\"name\": \"defaultBusinessStatus\",\n"
			+ "\"expression\": \"expression\"\n"
			+ "}\n"
			+ "}]\n"
			+ "}]\n"
			+ "}";

	public static String TASK_JSON  = "{\"identifier\":\"tsk11231jh22\",\"planIdentifier\":\"IRS_2018_S1\",\"groupIdentifier\":\"2018_IRS-3734{\",\"status\":\"Ready\",\"businessStatus\":\"Not Visited\",\"priority\":\"routine\",\"code\":\"IRS\",\"description\":\"Spray House\",\"focus\":\"IRS Visit\",\"for\":\"location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc\",\"executionStartDate\":\"2018-11-10T2200\",\"executionEndDate\":null,\"authoredOn\":\"2018-10-31T0700\",\"lastModified\":\"2018-10-31T0700\",\"owner\":\"demouser\",\"note\":[{\"authorString\":\"demouser\",\"time\":\"2018-01-01T0800\",\"text\":\"This should be assigned to patrick.\"}],\"serverVersion\":0,\"reasonReference\":\"reasonrefuuid\",\"location\":\"catchment1\",\"requester\":\"chw1\",\"syncStatus\":null,\"structureId\":null,\"rowid\":null}";

	public static String MOSQUITTO_COLLECTION_EVENT = "{\"_id\": \"6c27e088-b25c-4074-9b0b-cc6e6a24f864\", \"obs\": [{\"set\": [], \"values\": [\"1\"], \"fieldCode\": \"eligibleMos\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"eligibleMos\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"Complete\"], \"fieldCode\": \"task_business_status\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"task_business_status\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"04-11-2020\"], \"fieldCode\": \"trap_start\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"trap_start\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"Complete\"], \"fieldCode\": \"business_status\", \"fieldType\": \"formsubmissionField\", \"parentCode\": \"\", \"fieldDataType\": \"text\", \"saveObsAsArray\": false, \"formSubmissionField\": \"business_status\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"2020-11-04 14:59:15\"], \"fieldCode\": \"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"start\", \"saveObsAsArray\": false, \"formSubmissionField\": \"start\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"2020-11-04 14:59:26\"], \"fieldCode\": \"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"end\", \"saveObsAsArray\": false, \"formSubmissionField\": \"end\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"868190043418417\"], \"fieldCode\": \"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"deviceid\", \"saveObsAsArray\": false, \"formSubmissionField\": \"deviceid\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"520001918834913\"], \"fieldCode\": \"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"subscriberid\", \"saveObsAsArray\": false, \"formSubmissionField\": \"subscriberid\", \"humanReadableValues\": []}, {\"set\": [], \"values\": [\"8966001504183929136F\"], \"fieldCode\": \"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"fieldType\": \"concept\", \"parentCode\": \"\", \"fieldDataType\": \"simserial\", \"saveObsAsArray\": false, \"formSubmissionField\": \"simserial\", \"humanReadableValues\": []}], \"_rev\": \"v1\", \"team\": \"raitest\", \"type\": \"Event\", \"teamId\": \"a3bf8a41-3753-4b1d-816b-3999a30250c9\", \"details\": {\"taskStatus\": \"READY\", \"location_id\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"form_version\": \"0.0.1\", \"locationUUID\": \"044235d4-8c50-433a-ae3a-f925726a79f1\", \"appVersionName\": \"5.3.2\", \"planIdentifier\": \"42a68877-0376-4379-9660-9be7cc70f736\", \"taskIdentifier\": \"783a5d98-1ba6-4315-936b-57432996bf80\", \"locationVersion\": \"0\", \"taskBusinessStatus\": \"Not Visited\"}, \"version\": 1604476766559, \"duration\": 0, \"eventDate\": \"2020-11-04T00:00:00.000+07:00\", \"eventType\": \"mosquito_collection\", \"entityType\": \"Structure\", \"locationId\": \"9dc012f6-83fa-44a3-bec1-bfb948f05f6d\", \"providerId\": \"raitest\", \"dateCreated\": \"2020-11-25T10:47:03.666+07:00\", \"identifiers\": {}, \"baseEntityId\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"serverVersion\": 1600352722388, \"formSubmissionId\": \"a0b832e2-d1dd-453c-bb83-d862affe1a41\", \"clientDatabaseVersion\": 10, \"clientApplicationVersion\": 22}";
	public static String MOSQUITTO_COLLECTION_TASK = "{\"for\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"code\": \"Mosquito Collection\", \"focus\": \"6b931eb4-b361-4597-a412-73e02629d97d\", \"owner\": \"raitest\", \"status\": \"Ready\", \"priority\": \"routine\", \"authoredOn\": \"2020-11-25T10:47:10.682+07:00\", \"identifier\": \"783a5d98-1ba6-4315-936b-57432996bf80\", \"syncStatus\": \"Created\", \"description\": \"กำหนดสถานที่เก็บตัวอย่างยุง และการบันทึกผล\", \"structureId\": \"1008059c-b0b7-498d-9a37-c9603e1f6fed\", \"lastModified\": \"2020-11-25T10:47:10.682+07:00\", \"serverVersion\": 1600354240185, \"businessStatus\": \"Not Visited\", \"planIdentifier\": \"42a68877-0376-4379-9660-9be7cc70f736\", \"executionPeriod\": {\"start\": \"2020-11-04T14:59:00.000+07:00\"}, \"groupIdentifier\": \"9dc012f6-83fa-44a3-bec1-bfb948f05f6d\"}";

	public static String MOSQUITTO_COLLECTION_CLOSE_PLAN = "{\"date\":\"2020-10-20\",\"effectivePeriod\":{\"end\":\"2021-10-19\",\"start\":\"2020-10-20\"},\"experimental\":false,\"goal\":[{\"description\":\"Complete the Day 2 Visit form for each child.\",\"id\":\"Day_2_Visit\",\"priority\":\"medium-priority\",\"target\":[{\"detail\":{\"detailQuantity\":{\"comparator\":\"&gt;=\",\"unit\":\"Percent\",\"value\":80}},\"due\":\"2021-10-19\",\"measure\":\"Percent of children with completed Day 2 Visit form completed.\"}]}],\"identifier\":\"d652d9ae-2ff9-41e4-9bb3-fd4cd2ea0e2e\",\"jurisdiction\":[],\"name\":\"Sample_PNC_Plan_Template2\",\"serverVersion\":7,\"status\":\"active\",\"title\":\"Sample PNC Plan Template\",\"useContext\":[{\"code\":\"interventionType\",\"valueCodableConcept\":\"Linked-PNC\"},{\"code\":\"taskGenerationStatus\",\"valueCodableConcept\":\"internal\"}],\"version\":\"1\",\"action\":[{\"identifier\":\"c83deeb4-d580-529e-a6f0-c0c649585014\",\"prefix\":5,\"title\":\"Mosquito Collection Close Task\",\"description\":\"Close Mosquito Task\",\"code\":\"Mosquito Collection\",\"timingPeriod\":{\"start\":\"2020-11-17\",\"end\":\"2020-11-24\"},\"reason\":\"Investigation\",\"goalId\":\"Mosquito_Collection\",\"subjectCodableConcept\":{\"text\":\"Task\"},\"trigger\":[{\"type\":\"named-event\",\"name\":\"event-submission\",\"expression\":{\"description\":\"Trigger when a Mosquito Collection event is submitted\",\"expression\":\"questionnaire = 'mosquito_collection'\"}}],\"condition\":[{\"kind\":\"applicability\",\"subjectCodableConcept\":{\"text\":\"Task\"},\"expression\":{\"description\":\"Mosquito Collection event\",\"expression\":\"$this.contained.where(Task.code.text.value ='Mosquito Collection').exists()\"}}],\"dynamicValue\":[{\"path\":\"businessStatus\",\"expression\":{\"expression\":\"$this.item.where(linkId='task_business_status').answer[0].value.value\"}},{\"path\":\"status\",\"expression\":{\"expression\":\"'Completed'\"}}],\"definitionUri\":\"mosquito_collection_form.json\",\"type\":\"update\"}]}";

	public static PlanDefinition createPlan() {
		return gson.fromJson(plan, PlanDefinition.class);
	}
	
	public static Patient createPatient() {
		return Patient.builder().id(UUID.randomUUID().toString()).birthDate(Date.of("1990-12-19"))
		        .identifier(Identifier.builder().id("1234").value(of("1212313")).build())
		        .name(HumanName.builder().family(of("John")).given(of("Doe")).build()).build();
	}
	
	public static Task createTask() {
		return Task.builder().id(UUID.randomUUID().toString())
		        .businessStatus(CodeableConcept.builder().text(of("Completed")).build()).status(TaskStatus.COMPLETED)
		        .intent(TaskIntent.PLAN).code(CodeableConcept.builder().text(of("MDA_Round_1")).build())
		        ._for(Reference.builder().reference(of("1234mlmn-sdsd")).build())
		        .groupIdentifier(Identifier.builder().value(of("189897")).build()).build();
	}
	
	public static Location createLocation() {
		return Location.builder().id(UUID.randomUUID().toString()).name(of("Nairobi")).status(LocationStatus.ACTIVE).build();
	}

	public static Bundle createBundle() {
		return Bundle.builder().id(UUID.randomUUID().toString()).type(BundleType.COLLECTION).build();
	}
	
	public static QuestionnaireResponse createResponse() {
		Item structureType = Item.builder().linkId(of("structureType"))
		        .answer(Answer.builder().value(of("Residential Structure")).build()).build();
		Item businessStatus = Item.builder().linkId(of("business_status"))
		        .answer(Answer.builder().value(of("Not Visited")).build()).build();
		Item location = Item.builder().linkId(of("locationId"))
		        .answer(Answer.builder().value(of("123343430mmmj")).build()).build();
		return QuestionnaireResponse.builder().id(UUID.randomUUID().toString()).status(QuestionnaireResponseStatus.COMPLETED)
		        .subject(Reference.builder().reference(of("098787kml-jsks09")).build()).item(structureType, businessStatus,location)
		        .build();
	}

	public static PlanDefinition createPlanV1() {
		//TODO : Define expression
		return gson.fromJson(plan_1, PlanDefinition.class);
	}

	public static org.smartregister.domain.Task createDomainTask() {
       return gson.fromJson(TASK_JSON, org.smartregister.domain.Task.class);
	}

	public static Action createAction(){
		Action action = new Action();
		Set<DynamicValue> dynamicValues = new HashSet<>();
		DynamicValue dynamicValue = new DynamicValue();
		Expression expression = new Expression();
		expression.setExpression("'Cancelled'");
		dynamicValue.setPath("status");
		dynamicValue.setExpression(expression);
		dynamicValues.add(dynamicValue);

		dynamicValue = new DynamicValue();
		expression = new Expression();
		expression.setExpression("'Family Already Registered'");
		dynamicValue.setPath("businessStatus");
		dynamicValue.setExpression(expression);
		dynamicValues.add(dynamicValue);

		action.setType(Action.ActionType.UPDATE);
		action.setDynamicValue(dynamicValues);
		return action;
	}

}
