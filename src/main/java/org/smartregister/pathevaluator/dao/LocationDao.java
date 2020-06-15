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
	 * Gets the locations/structures in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return the structures in a jurisdiction
	 */
	List<Location> getLocations(String jurisdiction);
	
	/**
	 * Gets the child jurisdictions of a jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return the child jurisdictions of a jurisdiction including the jurisdiction
	 */
	List<Location> getJurisdictions(String jurisdiction);
}