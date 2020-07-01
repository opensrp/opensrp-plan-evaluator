/**
 * 
 */
package org.smartregister.pathevaluator;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.path.exception.FHIRPathException;

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
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(TestData.createTask(), "$this.is(FHIR.Patient).not()"));
	}
	
	@Test
	public void testAgeExpressions() throws FHIRPathException {
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(patient, "Patient.birthDate <= today() - 5 'years'"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		//1 year 
		assertFalse(pathEvaluatorLibrary.evaluateBooleanExpression(patient,
		    "@" + cal.get(Calendar.YEAR) + "  <= today() - 5 'years'"));
		
		//5 year 
		cal.add(Calendar.YEAR, -5);
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(patient,
		    "@" + cal.get(Calendar.YEAR) + "  <= today() - 5 'years'"));
	}
	
	@Test
	public void testContainedExpressions() throws FHIRPathException {
		Location location = TestData.createLocation();
		assertFalse(pathEvaluatorLibrary.evaluateBooleanExpression(location, "$this.contained.exists()"));
		location = location.toBuilder().contained(patient).build();
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(location, "$this.contained.exists()"));
		
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(location,
		    "$this.contained.where(Patient.name.family = 'John').exists()"));
		assertFalse(pathEvaluatorLibrary.evaluateBooleanExpression(location,
		    "$this.contained.where(Patient.name.family = 'Kelvin').exists()"));
	}
	
}
