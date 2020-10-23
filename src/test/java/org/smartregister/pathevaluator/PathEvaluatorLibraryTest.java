/**
 * 
 */
package org.smartregister.pathevaluator;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import org.junit.Before;
import org.junit.Test;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.exception.FHIRPathException;

/**
 * @author Samuel Githengi created on 06/10/20
 */
public class PathEvaluatorLibraryTest {
	
	private PathEvaluatorLibrary pathEvaluatorLibrary;
	
	private Patient patient;

	private Bundle deviceDefinitionBundle;
	
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
	
	@Test
	public void testEvaluateBooleanExpressionWithResourceAsNull() {
		assertFalse(pathEvaluatorLibrary.evaluateBooleanExpression(null, "$this.contained.exists()"));
	}
	
	@Test
	public void testEvaluateActionExpressions() {
		FHIRPathStringValue node = pathEvaluatorLibrary.evaluateStringExpression(patient, "$this.id");
		assertEquals(patient.getId(), node.string());
		
		node = pathEvaluatorLibrary.evaluateStringExpression(patient, "'Cancelled'");
		assertEquals("Cancelled", node.string());
	}

	@Test
	public void testExtractStringFromBundleShouldReturnCorrectString() throws Exception {
		String str = pathEvaluatorLibrary.extractStringFromBundle(getDeviceDefinitionBundle(), "$this.entry.resource.where(identifier.where(value='d3fdac0e-061e-b068-2bed-5a95e803636f')).capability.where(type.where(text='instructions')).description.text");
		assertEquals("Collect blood sample", str);
		str = pathEvaluatorLibrary.extractStringFromBundle(getDeviceDefinitionBundle(), "$this.entry.resource.where(identifier.where(value='620d3142-0a70-de75-88bb-8ad688195663')).capability.where(type.where(text='instructions')).description.text");
		assertEquals("Collect 2 blood samples", str);
	}

	@Test
	public void testExtractStringsFromBundleShouldReturnAllRelevantStrings() throws Exception {
		List<String> strs = pathEvaluatorLibrary.extractStringsFromBundle(getDeviceDefinitionBundle(), "$this.entry.resource.identifier.value");
		assertTrue(strs.contains("620d3142-0a70-de75-88bb-8ad688195663"));
		assertTrue(strs.contains("8020a21e-671e-0c31-717a-bf6735800677"));
		assertTrue(strs.contains("d8d7a874-2760-a382-de82-59c07fee0db6"));
		assertTrue(strs.contains("47540dbf-ec9a-4c06-c684-eb626795b332"));
	}

	private Bundle getDeviceDefinitionBundle() throws Exception {
		if (deviceDefinitionBundle == null) {
			InputStream stream = getClass().getClassLoader().getResourceAsStream("DeviceDefinition.json");
			deviceDefinitionBundle = FHIRParser.parser(Format.JSON).parse(stream);
		}
		return deviceDefinitionBundle;
	}

	public static String getTestFilePath() {
		return getBasePackageFilePath() + "src/main/java/org/smartregister/pathevaluator" + File.separator;
	}

	public static String getBasePackageFilePath() {
		return Paths.get(".").toAbsolutePath().normalize().toString() + File.separator;
	}
}
