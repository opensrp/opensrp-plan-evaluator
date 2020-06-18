package org.smartregister.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Task;
import org.joda.time.DateTime;
import org.junit.Test;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import static org.junit.Assert.assertNotNull;

public class TaskConverterTest {

	private String taskJson = "{\"identifier\":\"tsk11231jh22\",\"planIdentifier\":\"IRS_2018_S1\",\"groupIdentifier\":\"2018_IRS-3734{\",\"status\":\"Ready\",\"businessStatus\":\"Not Visited\",\"priority\":3,\"code\":\"IRS\",\"description\":\"Spray House\",\"focus\":\"IRS Visit\",\"for\":\"location.properties.uid:41587456-b7c8-4c4e-b433-23a786f742fc\",\"executionStartDate\":\"2018-11-10T2200\",\"executionEndDate\":null,\"authoredOn\":\"2018-10-31T0700\",\"lastModified\":\"2018-10-31T0700\",\"owner\":\"demouser\",\"note\":[{\"authorString\":\"demouser\",\"time\":\"2018-01-01T0800\",\"text\":\"This should be assigned to patrick.\"}],\"serverVersion\":0,\"reasonReference\":\"reasonrefuuid\",\"location\":\"catchment1\",\"requester\":\"chw1\"}";

	private static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.serializeNulls().create();

	@Test
	public void testConvertToFihrTask() {
		org.smartregister.domain.Task task = gson.fromJson(taskJson, org.smartregister.domain.Task.class);
	    Task fihrTask = TaskConverter.convertTasktoFihrResource(task);
	    assertNotNull(fihrTask);
	    System.out.println(fihrTask);
	}

}
