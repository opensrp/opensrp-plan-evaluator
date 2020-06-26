package org.smartregister.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.smartregister.domain.Client;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;

public class ClientConverter {
	
	public static Patient convertClientToPatientResource(Client client) {
		Patient.Builder builder = Patient.builder().id(client.getBaseEntityId());
		try {
			builder = Patient.builder().id(client.getBaseEntityId());
			java.lang.String strDate = ISODateTimeFormat.date().print(client.getBirthdate());
			builder.birthDate(Date.builder().value(strDate).build());
			
			if (StringUtils.isNotBlank(client.getGender())) {
				builder.gender(AdministrativeGender.of(StringUtils.toRootLowerCase(client.getGender())));
			} else {
				builder.gender(AdministrativeGender.UNKNOWN);
			}
			
			HumanName.Builder nameBuilder = HumanName.builder().family(String.of(client.getLastName()))
			        .text(String.of(client.fullName()));
			if (StringUtils.isNotBlank(client.getFirstName())) {
				nameBuilder.given(String.of(client.getFirstName()));
			}
			if (StringUtils.isNotBlank(client.getMiddleName())) {
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
				if (StringUtils.isBlank(entry.getValue()))
					continue;
				identifier = Identifier.builder().system(Uri.builder().value(entry.getKey()).build())
				        .value(String.builder().value(entry.getValue()).build()).build();
				identifierList.add(identifier);
			}
			
			CodeableConcept codeableConcept;
			Collection<Extension> extensions = new ArrayList<>();
			Coding coding;
			
			if (client.getRelationships() != null) {
				for (Map.Entry<java.lang.String, List<java.lang.String>> entry : client.getRelationships().entrySet()) {
					if (entry.getValue() == null || entry.getValue().isEmpty())
						continue;
					for (java.lang.String entryValue : entry.getValue()) {
						Extension extension = Extension.builder().value(String.builder().value(entryValue).build())
						        .url("relationships").build();
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
			
			Patient patient = builder.identifier(identifierList).build();
			return patient;
		}
		catch (Exception e) {
			Logger.getLogger(ClientConverter.class.getName()).log(Level.SEVERE,
			    "error conveting client " + ReflectionToStringBuilder.toString(client), e);
			return null;
		}
	}
}
