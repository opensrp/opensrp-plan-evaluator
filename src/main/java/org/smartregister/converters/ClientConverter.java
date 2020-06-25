package org.smartregister.converters;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.ISODateTimeFormat;
import org.smartregister.domain.Client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClientConverter {
	
	public static Patient convertClientToPatientResource(Client client) {
		
		Patient.Builder builder = Patient.builder();
		java.lang.String strDate = ISODateTimeFormat.date().print(client.getBirthdate());
		Date birthDate = Date.builder().value(strDate).build();
		AdministrativeGender administrativeGender = AdministrativeGender.of(StringUtils.toRootLowerCase(client.getGender()));
		
		HumanName.Builder nameBuilder = HumanName.builder().given(String.of(client.getFirstName()))
		        .family(String.of(client.getLastName())).text(String.of(client.fullName()));
		if (StringUtils.isBlank(client.getMiddleName())) {
			nameBuilder.given(String.of(client.getMiddleName()));
		}
		builder.name(nameBuilder.build());
		if (client.getDeathdate() != null) {
			java.lang.String deceasedDate = ISODateTimeFormat.dateTime().print(client.getDeathdate());
			DateTime deceasedDateTime = DateTime.builder().value(deceasedDate).build();
			builder.deceased(deceasedDateTime);
		}
		
		Identifier identifier;
		Collection<Identifier> identifierList = new ArrayList<>();
		for (Map.Entry<java.lang.String, java.lang.String> entry : client.getIdentifiers().entrySet()) {
			identifier = Identifier.builder().system(Uri.builder().value(entry.getKey()).build())
			        .value(String.builder().value(entry.getValue()).build()).build();
			identifierList.add(identifier);
		}
		
		CodeableConcept codeableConcept;
		Extension extension;
		Collection<Extension> extensions = new ArrayList<>();
		Coding coding;
		
		if (client.getRelationships() != null) {
			for (Map.Entry<java.lang.String, List<java.lang.String>> entry : client.getRelationships().entrySet()) {
				for (java.lang.String entryValue : entry.getValue()) {
					extension = Extension.builder().value(String.builder().value(entryValue).build()).url("relationships")
					        .build();
					extensions.add(extension);
				}
				coding = Coding.builder().extension(extensions).code(Code.code("relationship")).build();
				codeableConcept = CodeableConcept.builder().text(String.builder().value(entry.getKey()).build())
				        .coding(coding).build();
				identifier = Identifier.builder().type(codeableConcept).build();
				identifierList.add(identifier);
			}
		}
		
		if (client.getAttributes() != null) {
			for (Map.Entry<java.lang.String, java.lang.Object> entry : client.getAttributes().entrySet()) {
				if (entry.getValue() instanceof java.lang.String) {
					coding = Coding.builder().code(Code.code("attribute")).build();
					codeableConcept = CodeableConcept.builder().coding(coding).build();
					identifier = Identifier.builder().type(codeableConcept).id(entry.getKey())
					        .value(String.builder().value(entry.getValue().toString()).build()).build();
					identifierList.add(identifier);
				}
			}
		}
		
		Patient patient = builder.gender(administrativeGender)
		        .birthDate(birthDate).identifier(identifierList).id(client.getBaseEntityId()).build();
		return patient;
	}
}
