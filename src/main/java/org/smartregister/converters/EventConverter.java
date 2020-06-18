package org.smartregister.converters;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.QuestionnaireResponseStatus;
import org.smartregister.domain.Event;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EventConverter {

	public static QuestionnaireResponse convertEventToEncounterResource(Event event) {
		Collection<QuestionnaireResponse.Item> items = new ArrayList<>();
		Canonical eventType = Canonical.builder().id("eventType").value(event.getEventType()).build();

		QuestionnaireResponse.Item.Answer locationIdAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getLocationId()).build()).build();
		QuestionnaireResponse.Item locationId = QuestionnaireResponse.Item.builder()
				.linkId(String.builder().value("locationId").build())
				.answer(locationIdAnswer).build();
		items.add(locationId);

		QuestionnaireResponse.Item.Answer childLocationIdAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getChildLocationId()).build()).build();
		QuestionnaireResponse.Item childLocationId = QuestionnaireResponse.Item.builder()
				.linkId(String.builder().value("childLocationId").build())
				.answer(childLocationIdAnswer).build();
		items.add(childLocationId);

		QuestionnaireResponse.Item.Answer teamIdAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getTeamId()).build()).build();
		QuestionnaireResponse.Item teamId = QuestionnaireResponse.Item.builder()
				.linkId(String.builder().value("teamId").build()).answer(teamIdAnswer).build();
		items.add(teamId);

		QuestionnaireResponse.Item.Answer teamAnswer = QuestionnaireResponse.Item.Answer.builder()
				.value(String.builder().value(event.getTeam()).build()).build();
		QuestionnaireResponse.Item team = QuestionnaireResponse.Item.builder().linkId(String.builder().value("team").build())
				.answer(teamAnswer).build();
		items.add(team);

		Reference providerId = Reference.builder().id("providerId")
				.reference(String.builder().value(event.getProviderId()).build()).build();
		Reference baseEntityId = Reference.builder().id("baseEntityId")
				.reference(String.builder().value(event.getBaseEntityId()).build()).build();

		java.lang.String version = java.lang.String.valueOf(event.getServerVersion());
		Id versionId = Id.builder().value(version).build();
		Meta meta = Meta.builder().versionId(versionId).build();

		QuestionnaireResponse.Item.Answer answer;
		QuestionnaireResponse.Item item;

		if (event.getIdentifiers() != null) {
			for (Map.Entry<java.lang.String, java.lang.String> entry : event.getIdentifiers().entrySet()) {
				answer = QuestionnaireResponse.Item.Answer.builder()
						.value(String.builder().value(entry.getValue()).build()).build();
				item = QuestionnaireResponse.Item.builder().linkId(String.builder().value(entry.getKey()).build())
						.answer(answer).build();
				items.add(item);
			}
		}

		if (event.getDetails() != null) {
			for (Map.Entry<java.lang.String, java.lang.String> entry : event.getDetails().entrySet()) {
				answer = QuestionnaireResponse.Item.Answer.builder()
						.value(String.builder().value(entry.getValue()).build()).build();
				item = QuestionnaireResponse.Item.builder().linkId(String.builder().value(entry.getKey()).build())
						.answer(answer).build();
				items.add(item);
			}
		}

		//TODO : Add mapping for entityType

		QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder().
				status(QuestionnaireResponseStatus.COMPLETED).questionnaire(eventType)
				.item(items).
						author(providerId).
						subject(baseEntityId).
						meta(meta).
						build();
		return questionnaireResponse;
	}
}
