/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.Location;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public interface LocationDao {
	
	/**
	 * Gets the jurisdictions associated with a resource
	 * 
	 * @param id a resource
	 * @param fromResourceType
	 * @return jurisdictions associated with a resource
	 */
	List<Location> findJurisdictionsById(String id);
	
	/**
	 * Gets the structures associated with a resource
	 * 
	 * @param resource a resource
	 * @param fromResourceType
	 * @return structures associated with a resource
	 */
	List<Location> findLocationsById(String id);
	
	/**
	 * Gets the locations/structures in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return the structures in a jurisdiction
	 */
	List<Location> findLocationByJurisdiction(String jurisdiction);
	
}
