package org.smartregister.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.joda.time.DateTime;
import org.junit.Test;
import org.smartregister.domain.Event;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventConverterTest {

	private String EVENT_JSON = "{\"identifiers\":{},\"baseEntityId\":\"26b57c03-2885-445b-9ff9-f4ce018da755\",\"locationId\":\"3951\",\"eventDate\":\"2019-11-14T10:00:00.000+02:00\",\"eventType\":\"bednet_distribution\",\"formSubmissionId\":\"96027258-d659-4a05-ad92-69c5ccfc0439\",\"providerId\":\"onatest\",\"duration\":0,\"obs\":[{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"totPopulation\",\"parentCode\":\"\",\"values\":[\"2\"],\"set\":[],\"formSubmissionField\":\"totPopulation\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"existingLLINs\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"existingLLINs\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"existingLLIHNs\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"existingLLIHNs\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"existingITNs\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"existingITNs\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"existingITNDipped\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"existingITNDipped\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"calcExistingNets\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"calcExistingNets\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"calcNumNetsNeeded\",\"parentCode\":\"\",\"values\":[\"1.0\"],\"set\":[],\"formSubmissionField\":\"calcNumNetsNeeded\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"calcNumNetsToDistribute\",\"parentCode\":\"\",\"values\":[\"1\"],\"set\":[],\"formSubmissionField\":\"calcNumNetsToDistribute\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"distributedLLINs\",\"parentCode\":\"\",\"values\":[\"1\"],\"set\":[],\"formSubmissionField\":\"distributedLLINs\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"distributedLLIHNs\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"distributedLLIHNs\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"distributedITNs\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"distributedITNs\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"calcPopulationMinusExistingNets\",\"parentCode\":\"\",\"values\":[\"2\"],\"set\":[],\"formSubmissionField\":\"calcPopulationMinusExistingNets\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"calcTotalNetsDistributed\",\"parentCode\":\"\",\"values\":[\"1\"],\"set\":[],\"formSubmissionField\":\"calcTotalNetsDistributed\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"calcDistributedRecommendation\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"calcDistributedRecommendation\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"redippedITNs\",\"parentCode\":\"\",\"values\":[\"0\"],\"set\":[],\"formSubmissionField\":\"redippedITNs\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"comment\",\"parentCode\":\"\",\"values\":[\"cmt\"],\"set\":[],\"formSubmissionField\":\"comment\",\"humanReadableValues\":[]},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"business_status\",\"parentCode\":\"\",\"values\":[\"Complete\"],\"set\":[],\"formSubmissionField\":\"business_status\",\"humanReadableValues\":[]},{\"fieldType\":\"concept\",\"fieldDataType\":\"start\",\"fieldCode\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"parentCode\":\"\",\"values\":[\"2019-11-14 15:32:40\"],\"set\":[],\"formSubmissionField\":\"start\",\"humanReadableValues\":[]},{\"fieldType\":\"concept\",\"fieldDataType\":\"end\",\"fieldCode\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"parentCode\":\"\",\"values\":[\"2019-11-14 15:33:03\"],\"set\":[],\"formSubmissionField\":\"end\",\"humanReadableValues\":[]},{\"fieldType\":\"concept\",\"fieldDataType\":\"deviceid\",\"fieldCode\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"parentCode\":\"\",\"values\":[\"02cc25617eb803d4\"],\"set\":[],\"formSubmissionField\":\"deviceid\",\"humanReadableValues\":[]}],\"entityType\":\"Structure\",\"details\":{\"taskStatus\":\"READY\",\"location_id\":\"26b57c03-2885-445b-9ff9-f4ce018da755\",\"form_version\":\"\",\"locationUUID\":\"a423f422-0a34-410a-ab82-47e8e109fad1\",\"appVersionName\":\"2.6.0\",\"taskIdentifier\":\"dad7ae45-be06-417b-8faa-0b1ff1bf8b56\",\"locationVersion\":\"0\",\"taskBusinessStatus\":\"Not Visited\"},\"version\":1573774383636,\"teamId\":\"4dbacde8-623d-5c5e-a3f9-0d7911175607\",\"team\":\"Ona Test\",\"dateCreated\":\"2019-11-15T01:34:12.403+02:00\",\"dateEdited\":\"2019-11-28T15:15:12.813+02:00\",\"serverVersion\":1574946962848,\"clientApplicationVersion\":6,\"clientDatabaseVersion\":3,\"type\":\"Event\",\"id\":\"84255c16-83f1-4d55-ae3d-92ecb4739d8e\",\"revision\":\"v3\"}";

	private static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.serializeNulls().create();

	@Test
	public void testConvertEventToQuestionnaireResponse() {
		Event event ; //TODO : Add identifier
		event = gson.fromJson(EVENT_JSON, Event.class);
		event.setChildLocationId("child-location-id");
		event.setEventType("event-type");
		event.setEntityType("entity-type");
		event.setLocationId("location-id");
		event.setProviderId("provider-id");
		event.setBaseEntityId("base-entity-id");
		event.setTeam("team");
		event.setTeamId("team-id");
		QuestionnaireResponse questionnaireResponse = EventConverter.convertEventToEncounterResource(event);
		assertNotNull(questionnaireResponse);
		assertEquals(questionnaireResponse.getSubject().getReference().getValue(),event.getBaseEntityId());
		//TODO : Add assertion on remaining properties
		System.out.println(questionnaireResponse);
	}
}
