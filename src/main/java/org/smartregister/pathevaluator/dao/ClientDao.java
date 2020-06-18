/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import org.smartregister.pathevaluator.ResourceType;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;

/**
 * @author Samuel Githengi created on 06/15/20
 */
public interface ClientDao {
	
	/**
	 * Gets the families in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return families in a jurisdiction
	 */
	List<Patient> findFamilyByJurisdiction(String jurisdiction);
	
	/**
	 * Gets the family members in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return family members in a jurisdiction
	 */
	List<Patient> findFamilyMemberyByJurisdiction(String jurisdiction);
	
	/**
	 * Gets the families associated with a resource
	 * 
	 * @param resource a resource
	 * @param fromResourceType
	 * @return families associated with a resource
	 */
	List<Patient> getFamilies(Resource resource, ResourceType fromResourceType);
	
	/**
	 * Gets the family members associated with a resource
	 * 
	 * @param resource a resource
	 * @param fromResourceType
	 * @return family members associated with a resource
	 */
	List<Patient> getFamilyMembers(Resource resource, ResourceType fromResourceType);
	
	/**
	 * @param structureId
	 * @return
	 */
	List<Patient> findFamilyByResidence(String structureId);
	
	/**
	 * @param locationId
	 * @return
	 */
	List<Patient> findClientById(String id);
	
	/**
	 * @param id
	 * @return
	 */
	List<Patient> findFamilyMemberByResidence(String structureId);
	
	/**
	 * @param family
	 * @param id
	 * @return
	 */
	List<Patient> findFamilyMemberByRelationship(String relationship, String id);
	
}
