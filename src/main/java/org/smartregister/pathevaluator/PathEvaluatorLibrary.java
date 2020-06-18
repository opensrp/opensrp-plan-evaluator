/**
 * 
 */
package org.smartregister.pathevaluator;

import java.util.Collection;

import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.ClientProvider;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.LocationProvider;
import org.smartregister.pathevaluator.dao.TaskDao;
import org.smartregister.pathevaluator.dao.TaskProvider;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;

import lombok.Getter;

/**
 * @author Samuel Githengi created on 06/15/20
 */
@Getter
public class PathEvaluatorLibrary {
	
	private static PathEvaluatorLibrary instance;
	
	private FHIRPathEvaluator fhirPathEvaluator;
	
	private LocationProvider locationProvider;
	
	private ClientProvider clientProvider;
	
	private TaskProvider taskProvider;
	
	private PathEvaluatorLibrary(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao) {
		fhirPathEvaluator = FHIRPathEvaluator.evaluator();
		locationProvider = new LocationProvider(locationDao);
		clientProvider = new ClientProvider(clientDao);
		taskProvider = new TaskProvider(taskDao);
	}
	
	public static void init(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao) {
		instance = new PathEvaluatorLibrary(locationDao, clientDao, taskDao);
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
