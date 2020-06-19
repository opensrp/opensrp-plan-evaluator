package org.smartregister.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Task;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TaskConverterTest {

	private String taskJson = "{\"identifier\":\"tsk11231jh22\",\"planIdentifier\":\"IRS_2018_S1\",\"groupIdentifier\":\"2018_IRS-3734{\",\"status\":\"Ready\",\"businessStatus\":\"Not Visited\",\"priority\":3,\"code\":\"IRS\",\"description\":\"Spray House\",\"focus\":\"IRS Visit\",\"for\":\"location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc\",\"executionStartDate\":\"2018-11-10T2200\",\"executionEndDate\":\"2019-11-10T2100\",\"authoredOn\":\"2018-10-31T0700\",\"lastModified\":\"2018-10-31T0700\",\"owner\":\"demouser\",\"note\":[{\"authorString\":\"demouser\",\"time\":\"2018-01-01T0800\",\"text\":\"This should be assigned to patrick.\"}],\"serverVersion\":0,\"reasonReference\":\"reasonrefuuid\",\"location\":\"catchment1\",\"requester\":\"chw1\"}";

	private static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.serializeNulls().create();

	@Test
	public void testConvertToFihrTask() {
		org.smartregister.domain.Task task = gson.fromJson(taskJson, org.smartregister.domain.Task.class);
		Task fihrTask = TaskConverter.convertTasktoFihrResource(task);
		assertNotNull(fihrTask);
		assertEquals(fihrTask.getStatus().getValueAsEnumConstant().value(),
				StringUtils.toRootLowerCase(task.getStatus().name()));
		assertEquals(fihrTask.getIdentifier().get(0).getSystem().getValue(), "identifier");
		assertEquals(fihrTask.getIdentifier().get(0).getValue().getValue(), task.getIdentifier());
		assertEquals(fihrTask.getGroupIdentifier().getValue().getValue(), task.getGroupIdentifier());
		assertEquals(fihrTask.getBusinessStatus().getText().getValue(), task.getBusinessStatus());
		assertEquals(fihrTask.getLocation().getReference().getValue(), task.getLocation());
		assertEquals(fihrTask.getFor().getReference().getValue(), task.getForEntity());
		assertEquals(fihrTask.getFocus().getReference().getValue(), task.getFocus());
		assertEquals(fihrTask.getDescription().getValue(), task.getDescription());
		assertEquals(fihrTask.getCode().getText().getValue(), task.getCode());
		assertEquals(fihrTask.getNote().size(), task.getNotes().size());
		assertEquals(fihrTask.getRequester().getReference().getValue(), task.getRequester());
		assertEquals(fihrTask.getOwner().getReference().getValue(), task.getOwner());
		assertEquals(fihrTask.getStatusReason().getText().getValue(), task.getReasonReference());
		assertEquals(fihrTask.getAuthoredOn().getValue().toString(), task.getAuthoredOn().toString("yyyy-MM-dd'T'HH:mmZZ"));
		assertEquals(fihrTask.getLastModified().getValue().toString(),
				task.getLastModified().toString("yyyy-MM-dd'T'HH:mmZZ"));
		assertEquals(fihrTask.getExecutionPeriod().getStart().getValue().toString(),
				task.getExecutionStartDate().toString("yyyy-MM-dd'T'HH:mmZZ"));
		assertEquals(fihrTask.getExecutionPeriod().getEnd().getValue().toString(),
				task.getExecutionEndDate().toString("yyyy-MM-dd'T'HH:mmZZ"));
		assertEquals(fihrTask.getBasedOn().get(0).getReference().getValue(), task.getPlanIdentifier());
		assertEquals(fihrTask.getMeta().getVersionId().getValue(), String.valueOf(task.getServerVersion()));
		assertEquals(fihrTask.getIntent().getValueAsEnumConstant().value(), "plan");
		//TODO: Do we need to add assertions for Notes individually?
		System.out.println(fihrTask);
	}

}
