package org.smartregister.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.smartregister.domain.Task.Restriction;
import org.smartregister.domain.Task.TaskPriority;
import org.smartregister.domain.Task.TaskStatus;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TaskTest {

	private static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.create();

	protected static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HHmm");

	private String taskJson = "{\"identifier\":\"tsk11231jh22\",\"planIdentifier\":\"IRS_2018_S1\",\"groupIdentifier\":\"2018_IRS-3734{\",\"status\":\"Ready\",\"businessStatus\":\"Not Visited\",\"priority\":\"asap\",\"code\":\"IRS\",\"description\":\"Spray House\",\"focus\":\"IRS Visit\",\"for\":\"location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc\",\"executionPeriod\":{\"start\":\"2018-11-10T2200\"},\"authoredOn\":\"2018-10-31T0700\",\"lastModified\":\"2018-10-31T0700\",\"owner\":\"demouser\",\"note\":[{\"authorString\":\"demouser\",\"time\":\"2018-01-01T0800\",\"text\":\"This should be assigned to patrick.\"}],\"serverVersion\":0,\"reasonReference\":\"reasonrefuuid\",\"location\":\"catchment1\",\"requester\":\"chw1\"}";
	
	private String task2Json = "{\"identifier\":\"tsk11231jh22\",\"planIdentifier\":\"IRS_2018_S1\",\"groupIdentifier\":\"2018_IRS-3734{\",\"status\":\"Ready\",\"businessStatus\":\"Not Visited\",\"priority\":\"routine\",\"code\":\"IRS\",\"description\":\"Spray House\",\"focus\":\"IRS Visit\",\"for\":\"location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc\",\"executionPeriod\":{\"start\":\"2018-11-10T2200\"},\"authoredOn\":\"2018-10-31T0700\",\"lastModified\":\"2018-10-31T07:00:00\",\"owner\":\"demouser\",\"note\":[{\"authorString\":\"demouser\",\"time\":\"2018-01-01T0800\",\"text\":\"This should be assigned to patrick.\"}],\"serverVersion\":0}";
	
	@Test
	public void testDeserialize() {
		Task task = gson.fromJson(taskJson, Task.class);
		assertEquals("tsk11231jh22", task.getIdentifier());
		assertEquals("2018_IRS-3734{", task.getGroupIdentifier());
		assertEquals(TaskStatus.READY, task.getStatus());
		assertEquals("Not Visited", task.getBusinessStatus());
		assertEquals(TaskPriority.ASAP, task.getPriority());
		assertEquals("IRS", task.getCode());
		assertEquals("Spray House", task.getDescription());
		assertEquals("IRS Visit", task.getFocus());
		assertEquals("location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc", task.getForEntity());
		assertEquals("2018-11-10T2200", task.getExecutionPeriod().getStart().toString(formatter));
		assertNull(task.getExecutionPeriod().getEnd());
		assertEquals("2018-10-31T0700", task.getAuthoredOn().toString(formatter));
		assertEquals("2018-10-31T0700", task.getLastModified().toString(formatter));
		assertEquals("demouser", task.getOwner());
		assertEquals(1, task.getNotes().size());
		assertEquals("demouser", task.getNotes().get(0).getAuthorString());
		assertEquals("2018-01-01T0800", task.getNotes().get(0).getTime().toString(formatter));
		assertEquals("This should be assigned to patrick.", task.getNotes().get(0).getText());
		assertEquals("This should be assigned to patrick.", task.getNotes().get(0).getText());
		assertEquals("chw1", task.getRequester());
		assertEquals("catchment1", task.getLocation());
	}
	
	@Test
	public void testDeserializeWithTimeFormatArgument() {
		Task task = gson.fromJson(task2Json, Task.class);
		assertEquals("2018-10-31T0700", task.getLastModified().toString(formatter));
	}

	@Test
	public void testSerialize() {
		Task task =  gson.fromJson(taskJson, Task.class);
		assertEquals(taskJson, gson.toJson(task));
	}

	@Test
	public void testGettingReasonReference() {
		String reasonReference = "reasonreferenceuuid";
		Task task = new Task();
		assertNull(task.getReasonReference());

		task.setReasonReference(reasonReference);
		assertEquals(reasonReference, task.getReasonReference());

	}
	
	@Test
	public void testLocationAndRequesterMethods() {
		String location = "chaliwa";
		String user = "chw11";
		Task task = new Task();
		task.setLocation(location);
		task.setRequester(user);
		assertEquals(location, task.getLocation());
		assertEquals(user, task.getRequester());

	}
	
	@Test
	public void testGetTaskPriorityShouldReturnCorrectVariable() {
		assertEquals(TaskPriority.ROUTINE, Task.TaskPriority.get("routine"));
		assertEquals(TaskPriority.URGENT, Task.TaskPriority.get("urgent"));
		assertEquals(TaskPriority.ASAP, Task.TaskPriority.get("asap"));
		assertEquals(TaskPriority.STAT, Task.TaskPriority.get("stat"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTaskPriorityShouldReturnErrorWhenNull() {
		assertEquals(TaskPriority.ROUTINE, Task.TaskPriority.get(null));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTaskPriorityShouldReturnErrorForInvalidEnums() {
		assertEquals(TaskPriority.ROUTINE, Task.TaskPriority.get("normal"));
	}
	
	@Test
	public void testRestriction() {
		Task task = new Task();
		assertNull(task.getRestriction());
		Restriction restriction = new Restriction();
		restriction.setRepetitions(1);
		restriction.setPeriod(new Period(new DateTime(),null));
		
		task.setRestriction(restriction);
		assertEquals(restriction, task.getRestriction());

	}

}
