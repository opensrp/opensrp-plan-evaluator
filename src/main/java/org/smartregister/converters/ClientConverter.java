package org.smartregister.converters;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import org.smartregister.domain.Client;

import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ClientConverter {

	public static Patient convertClientToPatientResource(Client client) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String strDate = dateFormat.format(client.getBirthdate());
		Date birthDate = Date.builder().value(strDate).build();
		AdministrativeGender administrativeGender = AdministrativeGender.of(client.getGender());
		Identifier firstName = Identifier.builder()
				.system(Uri.builder().id("firstName").value(client.getFirstName()).build())
				.build();
		Identifier lastName = Identifier.builder().system(Uri.builder().id("lastName").value(client.getLastName()).build())
				.build();
		Identifier middleName = Identifier.builder()
				.system(Uri.builder().id("middleName").value(client.getMiddleName()).build())
				.build();
		HumanName fullName = HumanName.builder().id(client.fullName()).build();

		Patient patient = Patient.builder().name(fullName).birthDate(birthDate).gender(administrativeGender)
				.identifier(firstName, middleName, lastName).build();
		return patient;
	}
}
