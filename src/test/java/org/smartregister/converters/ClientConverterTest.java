package org.smartregister.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.path.FHIRPathElementNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.smartregister.domain.Client;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientConverterTest {

	private String CLIENT_JSON = "{\"firstName\":\"Khaleesi\",\"lastName\":\"Smith\",\"birthdate\":\"2000-11-21T02:00:00.000+02:00\",\"birthdateApprox\":false,\"deathdateApprox\":false,\"gender\":\"Female\",\"relationships\":{\"family\":[\"e6d3ea63-1309-4302-bd93-a6d0571cc645\"]},\"baseEntityId\":\"0587dcd6-4d8c-4540-be44-48706c0613db\",\"identifiers\":{\"opensrp_id\":\"20366639\"},\"addresses\":[],\"attributes\":{\"residence\":\"40930448-ea84-4ba7-8780-2a98dd8e902e\"},\"dateCreated\":\"2019-11-21T18:08:04.305+02:00\",\"serverVersion\":1574352482893,\"clientApplicationVersion\":7,\"clientDatabaseVersion\":3,\"type\":\"Client\",\"id\":\"a709a6b1-becf-4abd-91a7-dccaafe8714d\",\"revision\":\"v1\"}";

	private String CLIENT_JSON_2 = "{\"firstName\":\"Khaleesi\",\"lastName\":\"Smith\",\"birthdate\":\"2000-11-21T02:00:00.000+02:00\",\"birthdateApprox\":false,\"deathdateApprox\":false,\"gender\":\"Unknown\",\"relationships\":{\"family\":[\"e6d3ea63-1309-4302-bd93-a6d0571cc645\"]},\"baseEntityId\":\"0587dcd6-4d8c-4540-be44-48706c0613db\",\"identifiers\":{\"opensrp_id\":\"\"},\"addresses\":[],\"attributes\":{\"residence\":\"40930448-ea84-4ba7-8780-2a98dd8e902e\"},\"dateCreated\":\"2019-11-21T18:08:04.305+02:00\",\"serverVersion\":1574352482893,\"clientApplicationVersion\":7,\"clientDatabaseVersion\":3,\"type\":\"Client\",\"id\":\"a709a6b1-becf-4abd-91a7-dccaafe8714d\",\"revision\":\"v1\"}";

	private static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.serializeNulls().create();

	@Test
	public void testConvertToPatientResource() {
		Client client = gson.fromJson(CLIENT_JSON, Client.class);
		DateTimeZone zoneUTC = DateTimeZone.UTC;
		DateTime epochDateTime = new DateTime(0l, zoneUTC);
		client.setBirthdate(epochDateTime);
		client.setDeathdate(epochDateTime);
		client.setFirstName("John");
		client.setMiddleName("Lewis");
		client.setLastName("Johny");
		client.setGender("male");
		Patient patient = ClientConverter.convertClientToPatientResource(client);
		assertNotNull(patient);
		assertEquals(client.getGender(), patient.getGender().getValueAsEnumConstant().value());
		assertEquals(client.getLastName(), patient.getName().get(0).getFamily().getValue());
		assertEquals(client.fullName(), patient.getName().get(0).getText().getValue());
		assertEquals(client.getBaseEntityId(), patient.getId());
		int identifiersSize = client.getRelationships().entrySet().size() + client.getAttributes().entrySet().size() + client
				.getIdentifiers().entrySet().size();
		assertEquals(identifiersSize, patient.getIdentifier().size());
		assertEquals(client.getBirthdate().toString("YYYY-MM-DD"), patient.getBirthDate().getValue().toString());
		assertEquals(client.getDeathdate().toString("yyyy-MM-dd'T'HH:mm'Z'"),
				patient.getDeceased().as(com.ibm.fhir.model.type.DateTime.class).getValue().toString());
		PathEvaluatorLibrary.init(null, null, null,null, null);
		FHIRPathElementNode node = PathEvaluatorLibrary.getInstance().evaluateElementExpression(patient, "Patient.identifier.where(system='opensrp_id')");
		Identifier identifierNode = node.element().as(Identifier.class);
		assertEquals("20366639", identifierNode.getValue().getValue());

		node = PathEvaluatorLibrary.getInstance().evaluateElementExpression(patient, "Patient.identifier.type.where(text='family')");
		CodeableConcept familyNode = node.element().as(CodeableConcept.class);
		assertEquals(1, familyNode.getCoding().get(0).getExtension().size());
		assertEquals("e6d3ea63-1309-4302-bd93-a6d0571cc645", familyNode.getCoding().get(0).getExtension().get(0).getValue().as(com.ibm.fhir.model.type.String.class).getValue());

		node = PathEvaluatorLibrary.getInstance().evaluateElementExpression(patient, "Patient.identifier.where(id='residence')");
		Identifier residenceIdentifier = node.element().as(Identifier.class);
		assertEquals("residence", residenceIdentifier.getId());
		assertEquals("40930448-ea84-4ba7-8780-2a98dd8e902e", residenceIdentifier.getValue().getValue());
		assertEquals("attribute", residenceIdentifier.getType().getCoding().get(0).getCode().getValue());

		System.out.println(patient);
	}

	@Test
	public void testConvertToPatientResourceV2() {
		Client client = gson.fromJson(CLIENT_JSON_2, Client.class);
		DateTimeZone zoneUTC = DateTimeZone.UTC;
		DateTime epochDateTime = new DateTime(0l, zoneUTC);
		client.setBirthdate(epochDateTime);
		client.setDeathdate(epochDateTime);
		client.setFirstName("John");
		client.setMiddleName("Lewis");
		client.setLastName("Johny");
		client.setGender("unknown");
		Patient patient = ClientConverter.convertClientToPatientResource(client);
		assertNotNull(patient);
		assertEquals(client.getGender(), patient.getGender().getValueAsEnumConstant().value());
		assertEquals(client.getLastName(), patient.getName().get(0).getFamily().getValue());
		assertEquals(client.fullName(), patient.getName().get(0).getText().getValue());
		assertEquals(client.getBaseEntityId(), patient.getId());
		int identifiersSize = client.getRelationships().entrySet().size() + client.getAttributes().entrySet().size() + client
				.getIdentifiers().entrySet().size() - 1;
		assertEquals(identifiersSize, patient.getIdentifier().size());
		assertEquals(client.getBirthdate().toString("YYYY-MM-DD"), patient.getBirthDate().getValue().toString());
		assertEquals(client.getDeathdate().toString("yyyy-MM-dd'T'HH:mm'Z'"),
				patient.getDeceased().as(com.ibm.fhir.model.type.DateTime.class).getValue().toString());
		PathEvaluatorLibrary.init(null, null, null,null, null);
		FHIRPathElementNode node = PathEvaluatorLibrary.getInstance().evaluateElementExpression(patient, "Patient.identifier.type.where(text='family')");
		CodeableConcept familyNode = node.element().as(CodeableConcept.class);
		assertEquals(1, familyNode.getCoding().get(0).getExtension().size());
		assertEquals("e6d3ea63-1309-4302-bd93-a6d0571cc645", familyNode.getCoding().get(0).getExtension().get(0).getValue().as(com.ibm.fhir.model.type.String.class).getValue());

		node = PathEvaluatorLibrary.getInstance().evaluateElementExpression(patient, "Patient.identifier.where(id='residence')");
		Identifier residenceIdentifier = node.element().as(Identifier.class);
		assertEquals("residence", residenceIdentifier.getId());
		assertEquals("40930448-ea84-4ba7-8780-2a98dd8e902e", residenceIdentifier.getValue().getValue());
		assertEquals("attribute", residenceIdentifier.getType().getCoding().get(0).getCode().getValue());

		System.out.println(patient);
	}
}
