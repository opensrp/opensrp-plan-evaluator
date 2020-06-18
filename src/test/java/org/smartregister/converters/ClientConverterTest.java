package org.smartregister.converters;

import com.ibm.fhir.model.resource.Patient;
import org.joda.time.DateTime;
import org.junit.Test;
import org.smartregister.domain.Client;

public class ClientConverterTest {

	@Test
	public void testConvertToPatientResource() {
		Client client = new Client("base-entity-id");
		client.setBirthdate(new DateTime(0l));
		client.setDeathdate(new DateTime(0l));
		client.setFirstName("John");
		client.setMiddleName("Lewis");
		client.setLastName("Johny");
		client.setGender("male");
		Patient patient = ClientConverter.convertClientToPatientResource(client);
		System.out.println(patient);
	}
}
