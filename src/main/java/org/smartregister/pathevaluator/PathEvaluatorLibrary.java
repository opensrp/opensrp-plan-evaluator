/**
 * 
 */
package org.smartregister.pathevaluator;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.ClientProvider;
import org.smartregister.pathevaluator.dao.EventDao;
import org.smartregister.pathevaluator.dao.EventProvider;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.LocationProvider;
import org.smartregister.pathevaluator.dao.TaskDao;
import org.smartregister.pathevaluator.dao.TaskProvider;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;

import lombok.Getter;

/**
 * @author Samuel Githengi created on 06/15/20
 */
@Getter
public class PathEvaluatorLibrary {
	
	private static Logger logger = Logger.getLogger(PathEvaluatorLibrary.class.getSimpleName());
	
	private static PathEvaluatorLibrary instance;
	
	private FHIRPathEvaluator fhirPathEvaluator;
	
	private LocationProvider locationProvider;
	
	private ClientProvider clientProvider;
	
	private TaskProvider taskProvider;
	
	private String userName;
	
	private EventProvider eventProvider;
	
	private PathEvaluatorLibrary(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao, EventDao eventDao,
	    String userName) {
		fhirPathEvaluator = FHIRPathEvaluator.evaluator();
		locationProvider = new LocationProvider(locationDao);
		clientProvider = new ClientProvider(clientDao);
		taskProvider = new TaskProvider(taskDao);
		eventProvider = new EventProvider(eventDao);
		this.userName = userName;
	}
	
	public static void init(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao, EventDao eventDao,
	        String userName) {
		instance = new PathEvaluatorLibrary(locationDao, clientDao, taskDao, eventDao, userName);
	}
	
	/**
	 * Get the library instance
	 * 
	 * @return the instance
	 */
	public static PathEvaluatorLibrary getInstance() {
		return instance;
	}
	
	/**
	 * Evaluates a boolean FHIR Path expression on a resource
	 * 
	 * @param resource the resource expression is being evaluated on
	 * @param expression the expression to evaluate
	 * @return results of expression or false if the expression is not valid
	 */
	public boolean evaluateBooleanExpression(Resource resource, String expression) {
		
		try {
			Collection<FHIRPathNode> nodes = fhirPathEvaluator.evaluate(resource, expression);
			return nodes != null && nodes.iterator().hasNext()
			        ? nodes.iterator().next().as(FHIRPathBooleanValue.class)._boolean()
			        : false;
		}
		catch (FHIRPathException e) {
			logger.log(Level.SEVERE,
			    "Error execuring expression " + expression + "resource " + ReflectionToStringBuilder.toString(resource), e);
			return false;
		}
	}
	
	/**
	 * Evaluates a FHIR Path expression on a resource
	 * 
	 * @param resource the resource expression is being evaluated on
	 * @param expression the expression to evaluate
	 * @return results of expression
	 */
	public FHIRPathElementNode evaluateElementExpression(Resource resource, String expression) {
		
		try {
			return fhirPathEvaluator.evaluate(resource, expression).iterator().next().asElementNode();
		}
		catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error execuring expression "+expression, e);
			return null;
		}
	}
	
}
