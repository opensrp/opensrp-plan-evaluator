package org.smartregister.converters;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.QuestionnaireResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.smartregister.domain.Event;
import org.smartregister.domain.Obs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class EventConverter {
	
	public static QuestionnaireResponse convertEventToEncounterResource(Event event) {
		Collection<QuestionnaireResponse.Item> items = new ArrayList<>();
		Canonical eventType = Canonical.builder().id("eventType").value(event.getEventType().trim().replace(" ", "_")).build();
		
		QuestionnaireResponse.Item.Answer locationIdAnswer = QuestionnaireResponse.Item.Answer.builder()
		        .value(String.builder().value(event.getLocationId()).build()).build();
		QuestionnaireResponse.Item locationId = QuestionnaireResponse.Item.builder()
		        .linkId(String.builder().value("locationId").build()).answer(locationIdAnswer).build();
		items.add(locationId);
		
		if (StringUtils.isNotBlank(event.getChildLocationId())) {
			QuestionnaireResponse.Item.Answer childLocationIdAnswer = QuestionnaireResponse.Item.Answer.builder()
			        .value(String.builder().value(event.getChildLocationId()).build()).build();
			QuestionnaireResponse.Item childLocationId = QuestionnaireResponse.Item.builder()
			        .linkId(String.builder().value("childLocationId").build()).answer(childLocationIdAnswer).build();
			items.add(childLocationId);
		}
		
		if (StringUtils.isNotBlank(event.getTeamId())) {
			QuestionnaireResponse.Item.Answer teamIdAnswer = QuestionnaireResponse.Item.Answer.builder()
			        .value(String.builder().value(event.getTeamId()).build()).build();
			QuestionnaireResponse.Item teamId = QuestionnaireResponse.Item.builder()
			        .linkId(String.builder().value("teamId").build()).answer(teamIdAnswer).build();
			items.add(teamId);
		}
		
		if (StringUtils.isNotBlank(event.getTeam())) {
			QuestionnaireResponse.Item.Answer teamAnswer = QuestionnaireResponse.Item.Answer.builder()
			        .value(String.builder().value(event.getTeam()).build()).build();
			QuestionnaireResponse.Item team = QuestionnaireResponse.Item.builder()
			        .linkId(String.builder().value("team").build()).answer(teamAnswer).build();
			items.add(team);
		}
		
		Reference providerId = Reference.builder().id("providerId")
		        .reference(String.builder().value(event.getProviderId()).build()).build();
		Reference baseEntityId = Reference.builder().id("baseEntityId")
		        .reference(String.builder().value(event.getBaseEntityId()).build()).build();
		;
		
		java.lang.String version = java.lang.String.valueOf(event.getServerVersion());
		Id versionId = Id.builder().value(version).build();
		Meta meta = Meta.builder().versionId(versionId).build();
		
		QuestionnaireResponse.Item.Answer answer = null;
		QuestionnaireResponse.Item item;
		
		if (event.getIdentifiers() != null) {
			for (Map.Entry<java.lang.String, java.lang.String> entry : event.getIdentifiers().entrySet()) {
				answer = QuestionnaireResponse.Item.Answer.builder().value(String.builder().value(entry.getValue()).build())
				        .build();
				item = QuestionnaireResponse.Item.builder().linkId(String.builder().value(entry.getKey()).build())
				        .answer(answer).build();
				items.add(item);
			}
		}
		
		if (event.getDetails() != null) {
			for (Map.Entry<java.lang.String, java.lang.String> entry : event.getDetails().entrySet()) {
				if (StringUtils.trim(entry.getValue()).length() >= 1) {
					answer = QuestionnaireResponse.Item.Answer.builder()
					        .value(String.builder().value(entry.getValue()).build()).build();
					item = QuestionnaireResponse.Item.builder().linkId(String.builder().value(entry.getKey()).build())
					        .answer(answer).definition(Uri.of("details")).build();
					items.add(item);
				}
			}
		}
		Extension extension;
		Collection<Extension> extensions = new ArrayList<>();
		if (event.getObs() != null) {
			for (Obs obs : event.getObs()) {
				extensions = new ArrayList<>();
				for (Object obj : obs.getValues()) {
					if (obs.getValues().size() == 1 && obj instanceof java.lang.String) {
						answer = QuestionnaireResponse.Item.Answer.builder()
						        .value(String.builder().value(obj.toString()).build()).build();
					} else {
						if (obj instanceof java.lang.String) {
							extension = Extension.builder().value(String.builder().value(obj.toString()).build())
							        .url("observation").build();
							extensions.add(extension);
						}
						answer = QuestionnaireResponse.Item.Answer.builder().extension(extensions).build();
					}
				}

				java.lang.String obsIdentifier = obs.getFieldCode();

				if (obsIdentifier == null || obsIdentifier.isEmpty()) {
					obsIdentifier = obs.getFormSubmissionField();
				}

				// Skip this obs if it doesn't have a key
				if (obsIdentifier != null && !obsIdentifier.isEmpty()) {
					item = QuestionnaireResponse.Item.builder().linkId(String.builder().value(obsIdentifier).build())
							.answer(answer).build();
					items.add(item);
				}
			}
		}
		
		//TODO : Add mapping for entityType
		
		QuestionnaireResponse questionnaireResponse = QuestionnaireResponse.builder().id(event.getFormSubmissionId())
		        .status(QuestionnaireResponseStatus.COMPLETED).questionnaire(eventType).item(items).author(providerId)
		        .subject(baseEntityId).meta(meta).build();
		return questionnaireResponse;
	}
}
