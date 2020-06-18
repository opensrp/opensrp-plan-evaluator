/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.Patient;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public interface ClientDao {
	
	/** Gets Patient resources using primary key
	 * @param id the identifier for patient
	 * @return patient with the id
	 */
	List<Patient> findClientById(String id);
	
	/**
	 * Gets the families in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return families in a jurisdiction
	 */
	List<Patient> findFamilyByJurisdiction(String jurisdiction);
	
	/**
	 * Gets the families that reside in a structure
	 * 
	 * @param structureId the structure identifier
	 * @return families residing in a structure
	 */
	List<Patient> findFamilyByResidence(String structureId);
	
	/**
	 * Gets the family members in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return family members in a jurisdiction
	 */
	List<Patient> findFamilyMemberyByJurisdiction(String jurisdiction);
	
	/**
	 * Gets the family members that reside in a structure
	 * 
	 * @param structureId the structure identifier
	 * @return family members residing in a structure
	 */
	List<Patient> findFamilyMemberByResidence(String structureId);
	
	/** Gets the patient using a relationships
	 * @param relationship the type of relation
	 * @param id the value of relationship identifier
	 * @return patients that have relationships
	 */
	List<Patient> findClientByRelationship(String relationship, String id);
	
}
