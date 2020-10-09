/**
 * 
 */
package org.smartregister.pathevaluator;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.path.FHIRPathResourceNode;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.text.StringEscapeUtils;
import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.ClientProvider;
import org.smartregister.pathevaluator.dao.EventDao;
import org.smartregister.pathevaluator.dao.EventProvider;
import org.smartregister.pathevaluator.dao.LocationDao;
import org.smartregister.pathevaluator.dao.LocationProvider;
import org.smartregister.pathevaluator.dao.TaskDao;
import org.smartregister.pathevaluator.dao.TaskProvider;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathStringValue;
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
	
	private EventProvider eventProvider;
	
	private PathEvaluatorLibrary(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao, EventDao eventDao) {
		fhirPathEvaluator = FHIRPathEvaluator.evaluator();
		locationProvider = new LocationProvider(locationDao);
		clientProvider = new ClientProvider(clientDao);
		taskProvider = new TaskProvider(taskDao);
		eventProvider = new EventProvider(eventDao);
	}
	
	public static void init(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao, EventDao eventDao) {
		instance = new PathEvaluatorLibrary(locationDao, clientDao, taskDao, eventDao);
	}
	
	/**
	 * Get the library instance
	 * 
	 * @return the instance
	 */
	public static PathEvaluatorLibrary getInstance() {
		if (instance == null) {
			PathEvaluatorLibrary.init(null, null, null, null);
		}
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
		if (resource == null) {
			return false;
		}
		String escapedExpression = StringEscapeUtils.unescapeHtml4(expression);
		try {
			Collection<FHIRPathNode> nodes = fhirPathEvaluator.evaluate(resource, escapedExpression);
			return nodes != null && nodes.iterator().hasNext()
			        ? nodes.iterator().next().as(FHIRPathBooleanValue.class)._boolean()
			        : false;
		}
		catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error executing expression " + escapedExpression + " on resource \n"
			        + ReflectionToStringBuilder.toString(resource),
			    e);
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
	public FHIRPathElementNode evaluateElementExpression(DomainResource resource, String expression) {
		
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(resource, expression).iterator();
			return iterator.hasNext() ? iterator.next().asElementNode() : null;
		}
		catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error execuring expression " + expression, e);
			return null;
		}
	}
	
	public FHIRPathStringValue evaluateStringExpression(DomainResource resource, String expression) {
		
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(resource, expression).iterator();
			return iterator.hasNext() ? iterator.next().as(FHIRPathStringValue.class) : null;
		}
		catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error execuring expression " + expression, e);
			return null;
		}
	}

	/**
	 * Evaluates a String expression on a bundle for an expression and returns a String result or null
	 *
	 * @param bundle
	 * @param expression
	 * @return
	 */
	public String extractStringFromBundle(Bundle bundle, String expression) {
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(bundle, expression).iterator();
			return iterator.hasNext() ? iterator.next().asElementNode().getValue().asStringValue().string() : null;
		}
		catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error execuring expression " + expression, e);
			return null;
		}
	}

	/**
	 * Evaluates a String expression on a bundle for an expression and returns a Resource result or null
	 *
	 * @param bundle
	 * @param expression
	 * @return
	 */
	public Resource extractResourceFromBundle(Bundle bundle, String expression) {
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(bundle, expression).iterator();
			return iterator.hasNext() ? iterator.next().asResourceNode().resource() : null;
		} catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error execuring expression " + expression, e);
			return null;
		}
	}

	public void setLocationProvider(LocationProvider locationProvider) {
		this.locationProvider = locationProvider;
	}

	public void setClientProvider(ClientProvider clientProvider) {
		this.clientProvider = clientProvider;
	}

	public void setTaskProvider(TaskProvider taskProvider) {
		this.taskProvider = taskProvider;
	}

	public void setEventProvider(EventProvider eventProvider) {
		this.eventProvider = eventProvider;
	}
}
