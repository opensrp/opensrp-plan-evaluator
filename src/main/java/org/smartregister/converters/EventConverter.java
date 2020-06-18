package org.smartregister.converters;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.QuestionnaireResponseStatus;
import org.smartregister.domain.Event;

import java.time.ZoneId;

public class EventConverter {

	public static QuestionnaireResponse convertEventToEncounterResource(Event event) {
		Canonical eventType = Canonical.builder().id("eventType").value(event.getEventType()).build();

		QuestionnaireResponse.Item.Answer locationIdAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getLocationId()).build()).build();
		QuestionnaireResponse.Item locationId = QuestionnaireResponse.Item.builder()
				.linkId(String.builder().value("locationId").build())
				.answer(locationIdAnswer).build();
		QuestionnaireResponse.Item.Answer childLocationIdAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getChildLocationId()).build()).build();
		QuestionnaireResponse.Item childLocationId = QuestionnaireResponse.Item.builder()
				.linkId(String.builder().value("childLocationId").build())
				.answer(childLocationIdAnswer).build();

		QuestionnaireResponse.Item.Answer teamIdAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getTeamId()).build()).build();
		QuestionnaireResponse.Item teamId = QuestionnaireResponse.Item.builder()
				.linkId(String.builder().value("teamId").build()).answer(teamIdAnswer).build();

		QuestionnaireResponse.Item.Answer teamAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getTeam()).build()).build();
		QuestionnaireResponse.Item team = QuestionnaireResponse.Item.builder().linkId(String.builder().value("team").build())
				.answer(teamAnswer).build();

		Reference providerId = Reference.builder().id("providerId")
				.reference(String.builder().value(event.getProviderId()).build()).build();
		Reference baseEntityId = Reference.builder().id("baseEntityId")
				.reference(String.builder().value(event.getBaseEntityId()).build()).build();

		java.lang.String version = java.lang.String.valueOf(event.getServerVersion());
		Id versionId = Id.builder().value(version).build();
		Meta meta = Meta.builder().versionId(versionId).build();

		//TODO : Add mapping for identifier and entityType

		QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder().
				status(QuestionnaireResponseStatus.COMPLETED).questionnaire(eventType)
				.item(locationId, childLocationId, teamId, team).
						author(providerId).
						subject(baseEntityId).
						meta(meta).
						build();
		return questionnaireResponse;
	}
}
