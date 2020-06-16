package org.smartregister.converters;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.LocationMode;
import com.ibm.fhir.model.type.code.LocationStatus;
import org.smartregister.domain.PhysicalLocation;

import java.lang.Boolean;

public class LocationConverter {

	public static Location convertPhysicalLocationToLocationResource(PhysicalLocation physicalLocation) {
		LocationStatus locationStatus = LocationStatus.builder().value(physicalLocation.getProperties().getStatus().name())
				.build();
		Reference partOf = Reference.builder()
				.reference(String.builder().value(physicalLocation.getProperties().getParentId()).build()).build();
		String name = String.builder().value(physicalLocation.getProperties().getName()).build();
		java.lang.String version = java.lang.String.valueOf(physicalLocation.getProperties().getVersion());
		Id versionId = Id.builder().value(version).build();
		java.lang.String serverVersion = java.lang.String.valueOf(physicalLocation.getServerVersion());
		Instant lastUpdated = Instant.builder().value(serverVersion).build();
		Meta meta = Meta.builder().versionId(versionId).lastUpdated(lastUpdated).build();
		java.lang.String openMrsId = physicalLocation.getProperties().getCustomProperties().get("OpenMRS_Id");
		java.lang.String externalId = physicalLocation.getProperties().getCustomProperties().get("externalId");
		String name_en = String.builder().value(physicalLocation.getProperties().getCustomProperties().get("name_en"))
				.build();
		Identifier identifier1 = Identifier.builder().system(Uri.builder().id("OpenMRS_Id").value(openMrsId).build())
				.build();
		Identifier identifier2 = Identifier.builder().system(Uri.builder().id("externalId").value(externalId).build())
				.build();

		Coding jdnCoding = Coding.builder().code(Code.code("jdn")).build();
		Coding buCoding = Coding.builder().code(Code.code("bu")).build();
		CodeableConcept physicalType = physicalLocation.isJurisdiction() == Boolean.TRUE ?
				CodeableConcept.builder().coding(jdnCoding).build()
				:
				CodeableConcept.builder().coding(buCoding).build();

		LocationMode mode = LocationMode.builder().id("instance").build();
		Location.Builder builder = Location.builder().status(locationStatus).partOf(partOf).name(name)
				.identifier(identifier1, identifier2).alias(name_en).meta(meta).mode(mode).physicalType(physicalType);
		return builder.build();
	}
}
