/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Identifier;

/**
 * @author Samuel Githengi created on 06/18/20
 */
public abstract class BaseProvider {
	
	protected Identifier getIdentifier(Patient patient, String identifierName) {
		/** @formatter:off */
		return patient.getIdentifier()
				.stream()
		        .filter(identifier -> identifier.getId().equals(identifierName))
		        .findFirst()
		        .orElse(null);
		/** @formatter:on */
	}
}
