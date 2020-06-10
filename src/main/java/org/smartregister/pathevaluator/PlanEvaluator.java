package org.smartregister.pathevaluator;

import java.util.Collection;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;

/**
 * 
 */

/**
 * @author Samuel Githengi created on 06/09/20
 */
public class PlanEvaluator {
	
	private FHIRPathEvaluator fhirPathEvaluator = FHIRPathEvaluator.evaluator();
	
	public boolean evaluateBooleanExpression(Resource resource, String expression) {
		
		try {
			Collection<FHIRPathNode> nodes = fhirPathEvaluator.evaluate(resource, expression);
			return nodes != null ? nodes.iterator().next().as(FHIRPathBooleanValue.class)._boolean() : false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
}
