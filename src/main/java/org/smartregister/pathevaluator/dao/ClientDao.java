/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import java.util.List;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.RelatedPerson;

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
	List<RelatedPerson> getFamilies(String jurisdiction);
	
	/**
	 * Gets the family members in a particular jurisdiction
	 * 
	 * @param jurisdiction the jurisdiction identifier
	 * @return family members in a jurisdiction
	 */
	List<Patient> getFamilyMembers(String jurisdiction);
	
}
