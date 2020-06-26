/**
 * 
 */
package org.smartregister.pathevaluator;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;

/**
 * @author Samuel Githengi created on 06/10/20
 */
public class PathEvaluatorLibraryTest {
	
	private PathEvaluatorLibrary pathEvaluatorLibrary;
	
	private Patient patient;
	
	@Before
	public void startUp() {
		PathEvaluatorLibrary.init(null, null, null, null);
		pathEvaluatorLibrary = PathEvaluatorLibrary.getInstance();
		patient = Patient.builder().id("12345").birthDate(Date.of("1990-12-19"))
		        .identifier(Identifier.builder().id("1234").value(of("1212313")).build())
		        .name(HumanName.builder().family(of("John")).given(of("Doe")).build()).build();
		
		Reference.Builder builder = Reference.builder();
		builder.id("12345");
		builder.reference(of(patient.getId()));
	}
	
	@Test
	public void testWhere() {
		assertFalse(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "Patient.where(name.given = 'Does').exists()"));
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "Patient.where(name.given = 'Doe').exists()"));
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "Patient.exists()"));
		
	}
	
	@Test
	public void testExpressions() {
		assertFalse(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "Patient.name.family = 'Kelvin'"));
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "Patient.name.family = 'John'"));
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "Patient.birthDate >= @1990-12-19"));
	}
	
	@Test
	public void testIsExpressions() {
		
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(TestData.createLocation(), "$this.is(FHIR.Location)"));
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "$this.is(FHIR.Patient)"));
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(TestData.createTask(), "$this.is(FHIR.Task)"));
		assertFalse(pathEvaluatorLibrary.evaluateBooleanExpression(TestData.createTask(), "$this.is(FHIR.Patient)"));
	}
	
}
