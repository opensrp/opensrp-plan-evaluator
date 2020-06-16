package org.smartregister.converters;

import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import org.smartregister.domain.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventConverter {

	public static Encounter convertEventToEncounterResource(Event event) {

		CodeableConcept eventType = CodeableConcept.builder().text(String.builder().value(event.getEventType()).build())
				.build();
		CodeableConcept entityType = CodeableConcept.builder().text(String.builder().value(event.getEntityType()).build())
				.build();
		Reference referenceLocation = Reference.builder().reference(String.builder().value(event.getLocationId()).build())
				.build();
		Reference referenceChildLocation = Reference.builder()
				.reference(String.builder().value(event.getChildLocationId()).build()).build();
		Encounter.Location location = Encounter.Location.builder().location(referenceLocation).build();
		Encounter.Location childLocation = Encounter.Location.builder().location(referenceChildLocation).build();
		Reference partOf = Reference.builder().id(event.getTeamId()).display(String.builder().value(event.getTeam()).build())
				.build();
		Reference serviceProvider = Reference.builder().id(event.getProviderId()).build();
		Reference baseEntityId = Reference.builder().id(event.getBaseEntityId()).build();

		Encounter encounter = Encounter.builder().identifier().type(eventType).serviceType(entityType).
				location(location, childLocation).partOf(partOf).
				serviceProvider(serviceProvider).
				subject(baseEntityId).
				id(event.getFormSubmissionId()).
				identifier().build();
		return encounter;
	}
}
