/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Location;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public interface LocationDao {
	
	/**
	 * Gets the jurisdictions using an identifier
	 * 
	 * @param id the identifier
	 * @return jurisdictions with the identifier
	 */
	List<Location> findJurisdictionsById(String id);
	
	/**
	 * Gets the structures using an identifier
	 * 
	 * @param id the identifier
	 * @return structures with the identifier
 */
	List<Location> findLocationsById(String id);
	
	/**
	 * Gets the locations/structures in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return the structures in a jurisdiction
	 */
	List<Location> findLocationByJurisdiction(String jurisdiction);

	List<String> findChildLocationByJurisdiction(String id);

	/**
	 * Gets the locations/structures with its stock in a particular jurisdiction
	 * @param jurisdiction the jurisdiction Id
	 * @return a Bundle
	 */
	List<Bundle> findLocationAndStocksByJurisdiction(String jurisdiction);
}
