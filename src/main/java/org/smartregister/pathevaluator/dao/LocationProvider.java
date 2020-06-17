/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;
import java.util.Optional;

import org.smartregister.pathevaluator.ResourceType;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.type.Identifier;

import lombok.AllArgsConstructor;

/**
 * @author Samuel Githengi created on 06/17/20
 */
@AllArgsConstructor
public class LocationProvider {
	
	private LocationDao locationDao;
	
	public List<Location> getLocations(Resource resource, ResourceType fromResourceType) {
		switch (fromResourceType) {
			case JURISDICTION:
				return locationDao.getLocations(resource.getId());
			case FAMILY:
			case FAMILY_MEMBER:
				Patient patient = (Patient) resource;
				Optional<Identifier> locationIdentifier = patient.getIdentifier().stream()
				        .filter(identifier -> identifier.getId().equals("residence")).findFirst();
				if (locationIdentifier.isPresent()) {
					locationDao.getJurisdictionsById(locationIdentifier.get().getValue().getValue());
				} else {
					return null;
				}
			case TASK:
				Task task = (Task) resource;
				String locationId = task.getFor().getId();
				return locationDao.getLocationsById(locationId);
			default:
				return null;
		}
	}

	
	public List<Location> getJurisdictions(Resource resource, ResourceType fromResourceType) {
		switch (fromResourceType) {
			case LOCATION:
				return locationDao.getJurisdictionsById(resource.getId());
			case FAMILY:
			case FAMILY_MEMBER:
				Patient patient = (Patient) resource;
				Optional<Identifier> locationIdentifier = patient.getIdentifier().stream()
				        .filter(identifier -> identifier.getId().equals("location_id")).findFirst();
				if (locationIdentifier.isPresent()) {
					locationDao.getJurisdictionsById(locationIdentifier.get().getValue().getValue());
				} else {
					return null;
				}
			case TASK:
				Task task = (Task) resource;
				String locationId = task.getGroupIdentifier().getValue().getValue();
				return locationDao.getJurisdictionsById(locationId);
			default:
				return null;
		}
	}

	
	/**
	 * @param jurisdiction
	 * @return
	 */
	public List<? extends Resource> getJurisdictions(String jurisdiction) {
		return locationDao.getJurisdictions(jurisdiction);
	}

	/**
	 * @param jurisdiction
	 * @return
	 */
	public List<? extends Resource> getLocations(String jurisdiction) {
		return locationDao.getLocations(jurisdiction);
	}
	
}
