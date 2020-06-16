package org.smartregister.converters;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import org.smartregister.domain.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ClientConverter {

	public static Patient convertClientToPatientResource(Client client) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		java.lang.String strDate = dateFormat.format(client.getBirthdate());
		Date birthDate = Date.builder().value(strDate).build();
		AdministrativeGender administrativeGender = AdministrativeGender.of(client.getGender());
		HumanName firstName = HumanName.builder().given(String.builder().value(client.getFirstName()).build()).build();
		HumanName middleName = HumanName.builder().given(String.builder().value(client.getMiddleName()).build()).build();
		HumanName lastName = HumanName.builder().family(String.builder().value(client.getLastName()).build()).build();
		HumanName fullName = HumanName.builder().given(String.builder().value(client.fullName()).build()).build();

		Patient patient = Patient.builder().name(fullName).birthDate(birthDate).gender(administrativeGender).
				name(firstName, middleName, lastName, fullName).build();
		return patient;
	}
}
