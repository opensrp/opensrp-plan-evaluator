/**
 * 
 */
package org.smartregister.pathevaluator;

import java.util.Collection;

import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.LocationDao;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Samuel Githengi created on 06/15/20
 */
@Getter
@Setter
public class PathEvaluatorLibrary {
	
	private static PathEvaluatorLibrary instance;
	
	private FHIRPathEvaluator fhirPathEvaluator;
	
	private LocationDao locationDao;
	
	private ClientDao clientDao;
	
	private PathEvaluatorLibrary() {
		fhirPathEvaluator = FHIRPathEvaluator.evaluator();
	}
	
	public static void init() {
		instance = new PathEvaluatorLibrary();
	}
	
	/**
	 * Get the library instance
	 * 
	 * @return the instance
	 */
	public static PathEvaluatorLibrary getInstance() {
		return instance;
	}
	
	public boolean evaluateBooleanExpression(Resource resource, String expression) {
		
		try {
			Collection<FHIRPathNode> nodes = fhirPathEvaluator.evaluate(resource, expression);
			return nodes != null ? nodes.iterator().next().as(FHIRPathBooleanValue.class)._boolean() : false;
		}
		catch (Exception e) {
			return false;
		}
	}
	
}
