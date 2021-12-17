package org.smartregister.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Task;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.smartregister.domain.Period;
import org.smartregister.domain.Task.Restriction;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TaskConverterTest {
	
	private String taskJson = "{\"identifier\":\"tsk11231jh22\",\"planIdentifier\":\"IRS_2018_S1\",\"groupIdentifier\":\"2018_IRS-3734{\",\"status\":\"Ready\",\"businessStatus\":\"Not Visited\",\"priority\":\"routine\",\"code\":\"IRS\",\"description\":\"Spray House\",\"focus\":\"IRS Visit\",\"for\":\"location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc\",\"executionPeriod\": {\"start\":\"2018-11-10T2200\",\"end\":\"2019-11-10T2100\"},\"authoredOn\":\"2018-10-31T0700\",\"lastModified\":\"2018-10-31T0700\",\"owner\":\"demouser\",\"note\":[{\"authorString\":\"demouser\",\"time\":\"2018-01-01T0800\",\"text\":\"This should be assigned to patrick.\"}],\"serverVersion\":0,\"reasonReference\":\"reasonrefuuid\",\"location\":\"catchment1\",\"requester\":\"chw1\"}";
	private String dateFormat = "yyyy-MM-dd'T'HH:mm'Z'";
	
	private static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
	        .serializeNulls().create();
	
	@Test
	public void testConvertToFhirTask() {
		org.smartregister.domain.Task task = gson.fromJson(taskJson, org.smartregister.domain.Task.class);
		DateTime testNotetime = task.getNotes().get(0).getTime();
		DateTimeZone zoneUTC = DateTimeZone.UTC;
		DateTime utcTime = testNotetime.withZone(zoneUTC);
		task.getNotes().get(0).setTime(utcTime);
		Task fihrTask = TaskConverter.convertTasktoFihrResource(task);
		assertNotNull(fihrTask);
		assertEquals(fihrTask.getStatus().getValueAsEnumConstant().value(),
		    StringUtils.toRootLowerCase(task.getStatus().name()));
		assertEquals("identifier", fihrTask.getIdentifier().get(0).getSystem().getValue());
		assertEquals(task.getIdentifier(), fihrTask.getIdentifier().get(0).getValue().getValue());
		assertEquals(task.getGroupIdentifier(), fihrTask.getGroupIdentifier().getValue().getValue());
		assertEquals(task.getBusinessStatus(), fihrTask.getBusinessStatus().getText().getValue());
		assertEquals(task.getLocation(), fihrTask.getLocation().getReference().getValue());
		assertEquals(task.getForEntity(), fihrTask.getFor().getReference().getValue());
		assertEquals(task.getFocus(), fihrTask.getFocus().getReference().getValue());
		assertEquals(task.getDescription(), fihrTask.getDescription().getValue());
		assertEquals(task.getCode(), fihrTask.getCode().getText().getValue());
		assertEquals(task.getNotes().size(), fihrTask.getNote().size());
		assertEquals(task.getNotes().get(0).getAuthorString(),
		    fihrTask.getNote().get(0).getAuthor().as(com.ibm.fhir.model.type.String.class).getValue());
		assertEquals(task.getNotes().get(0).getText(), fihrTask.getNote().get(0).getText().getValue());
		assertEquals(task.getNotes().get(0).getTime().toString(dateFormat),
		    fihrTask.getNote().get(0).getTime().getValue().toString());
		assertEquals(task.getRequester(), fihrTask.getRequester().getReference().getValue());
		assertEquals(task.getOwner(), fihrTask.getOwner().getReference().getValue());
		assertEquals(task.getReasonReference(), fihrTask.getStatusReason().getText().getValue());
		assertEquals(task.getAuthoredOn().toString(dateFormat), fihrTask.getAuthoredOn().getValue().toString());
		assertEquals(task.getLastModified().toString(dateFormat), fihrTask.getLastModified().getValue().toString());
		assertEquals(task.getExecutionPeriod().getStart().toString(dateFormat),
		    fihrTask.getExecutionPeriod().getStart().getValue().toString());
		assertEquals(task.getExecutionPeriod().getEnd().toString(dateFormat),
		    fihrTask.getExecutionPeriod().getEnd().getValue().toString());
		assertEquals(task.getPlanIdentifier(), fihrTask.getBasedOn().get(0).getReference().getValue());
		assertEquals(String.valueOf(task.getServerVersion()), fihrTask.getMeta().getVersionId().getValue());
		assertEquals("plan", fihrTask.getIntent().getValueAsEnumConstant().value());
		//TODO: Do we need to add assertions for Notes individually?
		System.out.println(fihrTask);
	}
	
	@Test
	public void testConvertToFhirTaskWithRestriction() {
		org.smartregister.domain.Task task = gson.fromJson(taskJson, org.smartregister.domain.Task.class);
		task.setRestriction(new Restriction(3, new Period(DateTime.now(), DateTime.now().plusYears(1))));
		Task fhirTask = TaskConverter.convertTasktoFihrResource(task);
		assertNotNull(fhirTask.getRestriction());
		assertEquals(3, fhirTask.getRestriction().getRepetitions().getValue().intValue());
		assertEquals(task.getRestriction().getPeriod().getStart().toString(),
		    fhirTask.getRestriction().getPeriod().getStart().getValue().toString());
		assertEquals(task.getRestriction().getPeriod().getEnd().toString(),
		    fhirTask.getRestriction().getPeriod().getEnd().getValue().toString());
	}
	
}
