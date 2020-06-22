package org.smartregister.converters;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.domain.LocationProperty.PropertyStatus;
import org.smartregister.domain.PhysicalLocation;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.LocationMode;
import com.ibm.fhir.model.type.code.LocationStatus;

public class LocationConverter {

	public static Location convertPhysicalLocationToLocationResource(PhysicalLocation physicalLocation) {
		Location.Builder builder = Location.builder().id(physicalLocation.getId());
		if(physicalLocation.getProperties().getStatus().equals(PropertyStatus.PENDING_REVIEW)) {
			physicalLocation.getProperties().setStatus(PropertyStatus.ACTIVE);
		}
		LocationStatus locationStatus = LocationStatus.builder()
				.value(StringUtils.toRootLowerCase(physicalLocation.getProperties().getStatus().name()))
				.build();
	
		Reference partOf = Reference.builder()
				.reference(String.builder().value(physicalLocation.getProperties().getParentId()).build()).build();
		if(StringUtils.isNotBlank(physicalLocation.getProperties().getName())) {
			builder.name(String.of(physicalLocation.getProperties().getName()));
		}
		
		if(StringUtils.isNotBlank(physicalLocation.getProperties().getType())) {
			builder.type(CodeableConcept.builder().id("locationType").text(String.of(physicalLocation.getProperties().getType())).build());
		}
		
		java.lang.String version = java.lang.String.valueOf(physicalLocation.getProperties().getVersion());
		Id versionId = Id.builder().value(version).build();
		java.time.ZonedDateTime zdt = java.time.ZonedDateTime
				.ofInstant(java.time.Instant.ofEpochMilli(physicalLocation.getServerVersion()), ZoneId.systemDefault());
		Instant lastUpdated = Instant.builder().value(zdt).build();
		Meta meta = Meta.builder().versionId(versionId).lastUpdated(lastUpdated).build();
		if (physicalLocation.getProperties().getCustomProperties().get("name_en") != null) {
			builder.alias(String.of(physicalLocation.getProperties().getCustomProperties().get("name_en")));
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
		builder.status(locationStatus).partOf(partOf)
				.identifier(identifiers).meta(meta).mode(mode).physicalType(physicalType);

		return builder.build();
	}
}
