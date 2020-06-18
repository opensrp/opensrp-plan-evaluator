package org.smartregister.converters;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.LocationMode;
import com.ibm.fhir.model.type.code.LocationStatus;
import org.apache.commons.lang3.StringUtils;
import org.smartregister.domain.PhysicalLocation;

import java.lang.Boolean;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class LocationConverter {

	public static Location convertPhysicalLocationToLocationResource(PhysicalLocation physicalLocation) {
		LocationStatus locationStatus = LocationStatus.builder()
				.value(StringUtils.toRootLowerCase(physicalLocation.getProperties().getStatus().name()))
				.build();
		Reference partOf = Reference.builder()
				.reference(String.builder().value(physicalLocation.getProperties().getParentId()).build()).build();
		String name = String.builder().value(physicalLocation.getProperties().getName()).build();
		java.lang.String version = java.lang.String.valueOf(physicalLocation.getProperties().getVersion());
		Id versionId = Id.builder().value(version).build();
		java.time.ZonedDateTime zdt = java.time.ZonedDateTime
				.ofInstant(java.time.Instant.ofEpochMilli(physicalLocation.getServerVersion()), ZoneId.systemDefault());
		Instant lastUpdated = Instant.builder().value(zdt).build();
		Meta meta = Meta.builder().versionId(versionId).lastUpdated(lastUpdated).build();
		String name_en = null;
		if (physicalLocation.getProperties().getCustomProperties().get("name_en") != null) {
			name_en = String.builder().value(physicalLocation.getProperties().getCustomProperties().get("name_en"))
					.build();
		}

		Identifier identifier;
		Collection<Identifier> identifiers = new ArrayList<>();

		for (Map.Entry<java.lang.String, java.lang.String> entry : physicalLocation.getProperties().getCustomProperties()
				.entrySet()) {
			identifier = Identifier.builder()
					.system(Uri.builder().value(entry.getKey()).build())
					.value(String.builder().value(entry.getValue()).build())
					.build();
			identifiers.add(identifier);
		}

		Coding jdnCoding = Coding.builder().code(Code.code("jdn")).build();
		Coding buCoding = Coding.builder().code(Code.code("bu")).build();
		CodeableConcept physicalType = physicalLocation.isJurisdiction() == Boolean.TRUE ?
				CodeableConcept.builder().coding(jdnCoding).build()
				:
				CodeableConcept.builder().coding(buCoding).build();

		LocationMode mode = LocationMode.builder().id("mode").value("instance").build();
		Location.Builder builder;
		if (name_en != null) {
			builder = Location.builder().status(locationStatus).partOf(partOf).name(name)
					.identifier(identifiers).alias(name_en).meta(meta).mode(mode).physicalType(physicalType);
		} else {
			builder = Location.builder().status(locationStatus).partOf(partOf).name(name)
					.identifier(identifiers).meta(meta).mode(mode).physicalType(physicalType);
		}

		return builder.build();
	}
}
