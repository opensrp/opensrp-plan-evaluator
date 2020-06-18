package org.smartregister.converters;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import org.joda.time.format.ISODateTimeFormat;
import org.smartregister.domain.Client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClientConverter {

	public static Patient convertClientToPatientResource(Client client) {
		java.lang.String strDate = ISODateTimeFormat.date().print(client.getBirthdate());
		Date birthDate = Date.builder().value(strDate).build();
		AdministrativeGender administrativeGender = AdministrativeGender.of(client.getGender());
		HumanName firstName = HumanName.builder().given(String.builder().value(client.getFirstName()).build(),
				String.builder().value(client.getMiddleName()).build()).
				family(String.builder().value(client.getLastName()).build()).
				text(String.builder().value(client.fullName()).build()).build();
		java.lang.String deceasedDate = ISODateTimeFormat.dateTime().print(client.getDeathdate());
		DateTime deceasedDateTime = DateTime.builder().value(deceasedDate).build();

		Identifier identifier;
		Collection<Identifier> identifierList = new ArrayList<>();
		for (Map.Entry<java.lang.String, java.lang.String> entry : client.getIdentifiers().entrySet()) {
			identifier = Identifier.builder()
					.system(Uri.builder().value(entry.getKey()).build())
					.value(String.builder().value(entry.getValue()).build())
					.build();
			identifierList.add(identifier);
		}

		CodeableConcept codeableConcept;
		Extension extension;
		Collection<Extension> extensions = new ArrayList<>();
		Coding coding;

		if (client.getRelationships() != null) {
			for (Map.Entry<java.lang.String, List<java.lang.String>> entry : client.getRelationships().entrySet()) {
				for (java.lang.String entryValue : entry.getValue()) {
					extension = Extension.builder().value(String.builder().value(entryValue).build()).build();
					extensions.add(extension);
				}
				coding = Coding.builder().extension(extensions).build();
				codeableConcept = CodeableConcept.builder().text(String.builder().value(entry.getKey()).build())
						.coding(coding)
						.build();
				identifier = Identifier.builder().type(codeableConcept).build();
				identifierList.add(identifier);
			}
		}

		if (client.getAttributes() != null) {
			for (Map.Entry<java.lang.String, java.lang.Object> entry : client.getAttributes().entrySet()) {
				if (entry.getValue() instanceof java.lang.String) {
					codeableConcept = CodeableConcept.builder().text(String.builder().value(entry.getValue().toString()).build()).id(entry.getKey())
							.build();
					identifier = Identifier.builder().type(codeableConcept).build();
					identifierList.add(identifier);
				}
			}
		}

		Patient patient = Patient.builder().gender(administrativeGender).
				name(firstName).deceased(deceasedDateTime).birthDate(birthDate).identifier(identifierList).
				id(client.getBaseEntityId()).build();
		return patient;
	}
}
