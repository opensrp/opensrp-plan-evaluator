/**
 * 
 */
package org.smartregister.pathevaluator;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.ObservationStatus;

/**
 * @author Samuel Githengi created on 06/10/20
 */
public class PlanEvaluatorTest {
	
	private PlanEvaluator planEvaluator= new PlanEvaluator();
	
	private static Patient patient;
	
	@BeforeClass
	public static void startUp() {
		patient = Patient.builder().id("12345").birthDate(Date.of("1990-12-19"))
		        .identifier(Identifier.builder().id("1234").value(of("1212313")).build())
		        .name(HumanName.builder().family(of("John")).given(of("Doe")).build()).build();
		
		Reference.Builder builder = Reference.builder();
		builder.id("12345");
		builder.reference(of(patient.getId()));
		Observation observation = Observation.builder()
		        .code(CodeableConcept.builder().id("123").text(of("12343434343")).build()).subject(builder.build())
		        .status(ObservationStatus.FINAL).build();
	}
	
	@Test
	public void testWhere() {
		assertFalse(planEvaluator.evaluateBooleanExpression(patient, "Patient.where(name.given = 'Does').exists()"));
		assertTrue(planEvaluator.evaluateBooleanExpression(patient, "Patient.where(name.given = 'Doe').exists()"));
		assertTrue(planEvaluator.evaluateBooleanExpression(patient, "Patient.exists()"));
		
	}
	
	@Test
	public void testExpressions() {
		assertFalse(planEvaluator.evaluateBooleanExpression(patient, "Patient.name.family = 'Kelvin'"));
		assertTrue(planEvaluator.evaluateBooleanExpression(patient, "Patient.name.family = 'John'"));
		assertTrue(planEvaluator.evaluateBooleanExpression(patient, "Patient.birthDate >= @1990-12-19"));
	}
	
}
