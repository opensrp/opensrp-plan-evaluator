/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

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
public class LocationProvider extends BaseProvider {
	
	private static final String RESIDENCE = "residence";
	
	private static final String LOCATION_ID = "location_id";
	
	private LocationDao locationDao;
	
	public List<Location> getLocations(Resource resource, ResourceType fromResourceType) {
		switch (fromResourceType) {
			case JURISDICTION:
				return locationDao.getLocations(resource.getId());
			case FAMILY:
			case FAMILY_MEMBER:
				Identifier residence = getIdentifier((Patient) resource, RESIDENCE);
				if (residence != null) {
					return locationDao.getJurisdictionsById(residence.getValue().getValue());
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
				Identifier location = getIdentifier((Patient) resource, LOCATION_ID);
				if (location != null) {
					return locationDao.getJurisdictionsById(location.getValue().getValue());
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
	public List<Location> getJurisdictions(String jurisdiction) {
		return locationDao.getJurisdictions(jurisdiction);
	}
	
	/**
	 * @param jurisdiction
	 * @return
	 */
	public List<Location> getLocations(String jurisdiction) {
		return locationDao.getLocations(jurisdiction);
	}
	
}
