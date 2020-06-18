package org.smartregister.converters;

import com.ibm.fhir.model.resource.QuestionnaireResponse;
import org.junit.Test;
import org.smartregister.domain.Event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventConverterTest {

	@Test
	public void testConvertEventToQuestionnaireResponse() {
		Event event = new Event(); //TODO : Add identifier
		event.setChildLocationId("child-location-id");
		event.setEventType("event-type");
		event.setEntityType("entity-type");
		event.setLocationId("location-id");
		event.setProviderId("provider-id");
		event.setBaseEntityId("base-entity-id");
		event.setTeam("team");
		event.setTeamId("team-id");
		QuestionnaireResponse questionnaireResponse = EventConverter.convertEventToEncounterResource(event);
		assertNotNull(questionnaireResponse);
		assertEquals(questionnaireResponse.getItem().size(), 4);
		System.out.println(questionnaireResponse);
	}
}
