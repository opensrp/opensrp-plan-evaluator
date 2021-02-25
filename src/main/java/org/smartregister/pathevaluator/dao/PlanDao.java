/**
 * 
 */
package org.smartregister.pathevaluator.dao;

import org.smartregister.domain.PlanDefinition;

/**
 * @author Samuel Githengi created on 01/29/21
 */
public interface PlanDao {
	
	PlanDefinition findPlanByIdentifier(String identifier);
	
}
