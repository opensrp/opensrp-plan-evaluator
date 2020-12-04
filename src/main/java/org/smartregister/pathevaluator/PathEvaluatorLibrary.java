/**
 * 
 */
package org.smartregister.pathevaluator;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;
import lombok.Getter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.text.StringEscapeUtils;
import org.smartregister.pathevaluator.dao.*;


import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathDateValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private StockProvider stockProvider;
	
	private PathEvaluatorLibrary(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao, EventDao eventDao, StockDao stockDao) {
		fhirPathEvaluator = FHIRPathEvaluator.evaluator();
		locationProvider = new LocationProvider(locationDao);
		clientProvider = new ClientProvider(clientDao);
		taskProvider = new TaskProvider(taskDao);
		eventProvider = new EventProvider(eventDao);
		stockProvider = new StockProvider(stockDao);
	}
	
	public static void init(LocationDao locationDao, ClientDao clientDao, TaskDao taskDao, EventDao eventDao, StockDao stockDao) {
		instance = new PathEvaluatorLibrary(locationDao, clientDao, taskDao, eventDao, stockDao);
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
		String escapedExpression =unescapeHtml(expression);
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
	
	private String unescapeHtml(String expression) {
		 String escapedExpression = StringEscapeUtils.unescapeHtml4(expression);
		 if(escapedExpression.equals(StringEscapeUtils.unescapeHtml4(escapedExpression))) {
			 return escapedExpression;
		 }else {
			 return unescapeHtml(escapedExpression);
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
			logger.log(Level.SEVERE, "Error executing expression " + expression, e);
			return null;
		}
	}
	
	public FHIRPathStringValue evaluateStringExpression(DomainResource resource, String expression) {
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(resource, expression).iterator();
			return iterator.hasNext() ? iterator.next().as(FHIRPathStringValue.class) : null;
		} catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error executing expression " + expression, e);
			return null;
		}
	}

	/**
	 * Evaluates a FHIR Path {@param expression} on a {@param bundle} and returns a String result
	 * or null if the query fails or doesn't have results
	 *
	 * @param bundle
	 * @param expression
	 * @return
	 */
	
	public String extractStringFromBundle(Bundle bundle, String expression) {
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(bundle, expression).iterator();
			return iterator.hasNext() ? convertElementNodeValToStr(iterator.next().asElementNode()) : null;
		} catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error executing expression " + expression, e);
			return null;
		}
	}


	/**
	 * Evaluates a FHIR Path {@param expression} on a {@param bundle} and returns a List of String results
	 * or null if the query fails or doesn't have results
	 *
	 * @param bundle
	 * @param expression
	 * @return
	 */
	public List<String> extractStringsFromBundle(Bundle bundle, String expression) {
		List<String> strs = new ArrayList<>();
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(bundle, expression).iterator();
			while (iterator.hasNext()) {
				strs.add(convertElementNodeValToStr(iterator.next().asElementNode()));
			}
		} catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error executing expression " + expression, e);
		}
		return strs;
	}

	/**
	 * Returns a {@link FHIRPathElementNode}'s value as a String
	 * @param fhirPathElementNode
	 * @return
	 */
	private String convertElementNodeValToStr(FHIRPathElementNode fhirPathElementNode) {
		return fhirPathElementNode.getValue().asStringValue().string();
	}
	
	public FHIRPathDateValue evaluateDateExpression(DomainResource resource, String expression) {
		
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(resource, expression).iterator();
			return iterator.hasNext() ? iterator.next().as(FHIRPathDateValue.class) : null;
		}
		catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error execuring expression " + expression, e);
			return null;
		}
	}
	

	/**
	 * Evaluates a FHIR Path {@param expression} on a {@param bundle} and returns a List of {@link Element}s
	 * or null if the query fails or doesn't have results
	 *
	 * @param bundle
	 * @param expression
	 * @return
	 */
	public List<Element> extractElementsFromBundle(Bundle bundle, String expression) {
		List<Element> elements = new ArrayList<>();
		try {
			Iterator<FHIRPathNode> iterator = fhirPathEvaluator.evaluate(bundle, expression).iterator();
			while (iterator.hasNext()) {
				elements.add(iterator.next().asElementNode().element());
			}
		} catch (FHIRPathException e) {
			logger.log(Level.SEVERE, "Error executing expression " + expression, e);
			return elements;
		}
		return elements;
	}

	/**
	 * Evaluates a FHIR Path {@param expression} on a {@param bundle} and returns a {@link Resource} result
	 * or null if the query fails or doesn't have results
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
			logger.log(Level.SEVERE, "Error executing expression " + expression, e);
			return null;
		}
	}
}
