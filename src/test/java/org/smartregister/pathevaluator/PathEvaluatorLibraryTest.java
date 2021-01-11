/**
 * 
 */
package org.smartregister.pathevaluator;

import static com.ibm.fhir.model.type.String.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.smartregister.pathevaluator.action.ActionHelper;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.DeviceDefinition;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.exception.FHIRPathException;
import org.smartregister.pathevaluator.dao.StockDao;
import org.smartregister.pathevaluator.plan.PlanEvaluator;

/**
 * @author Samuel Githengi created on 06/10/20
 */
public class PathEvaluatorLibraryTest {
	
	private PathEvaluatorLibrary pathEvaluatorLibrary;
	
	private Patient patient;
	
	@Before
	public void startUp() {
		Whitebox.setInternalState(PathEvaluatorLibrary.class, "instance", (Object) null);
		pathEvaluatorLibrary = PathEvaluatorLibrary.getInstance();
		patient = Patient.builder().id("12345").birthDate(Date.of("1990-12-19"))
		        .identifier(Identifier.builder().id("1234").value(of("1212313")).build())
		        .name(HumanName.builder().family(of("John")).given(of("Doe")).build()).build();
		
		Reference.Builder builder = Reference.builder();
		builder.id("12345");
		builder.reference(of(patient.getId()));
	}
	
	@After
	public void tearDown() {
		Whitebox.setInternalState(PathEvaluatorLibrary.class, "instance", (Object) null);
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
	public void testLocationExpressions() throws FHIRPathException {
		Location location = TestData.createLocation();
		location=location.toBuilder().identifier(Identifier.builder().id("hasGeometry").value(com.ibm.fhir.model.type.String.of("true")).build()).build();
		//assertEquals("",pathEvaluatorLibrary.evaluateStringExpression(location, "$this.identifier.id"));
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(location, "$this.identifier.where(id='hasGeometry').value='true'"));

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
	public void testGetResidenceFromQuestionaire() throws Exception {
		QuestionnaireResponse questionnaireResponse = (QuestionnaireResponse) getResource("Questionnaire.json");
		FHIRPathElementNode node = pathEvaluatorLibrary.evaluateElementExpression(questionnaireResponse,
		    ActionHelper.RESIDENCE_EXPRESSION);
		assertEquals("e02a282b-bc41-499f-8f30-eab2addbddb7",node.getValue().asStringValue().string());
	}

	@Test
	public void testEvaluateBooleanWithDoubleEscapedExpression() throws Exception {
		QuestionnaireResponse questionnaireResponse = (QuestionnaireResponse) getResource("Questionnaire.json");
		assertTrue(pathEvaluatorLibrary.evaluateBooleanExpression(questionnaireResponse,
		    "($this.is(FHIR.Patient) and $this.birthDate &lt;= today() - 5 'years') or ($this.contained.where(Patient.birthDate &amp;lt;= today() - 5 'years').exists())"));
	}

	@Test
	public void testExtractStringFromBundleShouldReturnCorrectString() throws Exception {
		verifyCorrectStringIsExtracted("d3fdac0e-061e-b068-2bed-5a95e803636f", "Collect blood sample");
		verifyCorrectStringIsExtracted("620d3142-0a70-de75-88bb-8ad688195663", "Collect 2 blood samples");
	}

	@Test
	public void testExtractStringsFromBundleShouldReturnAllRelevantStrings() throws Exception {
		List<String> strs = pathEvaluatorLibrary.extractStringsFromBundle(getDeviceDefinitionBundle(),
		    "$this.entry.resource.identifier.value");
		assertTrue(strs.contains("620d3142-0a70-de75-88bb-8ad688195663"));
		assertTrue(strs.contains("8020a21e-671e-0c31-717a-bf6735800677"));
		assertTrue(strs.contains("d8d7a874-2760-a382-de82-59c07fee0db6"));
		assertTrue(strs.contains("47540dbf-ec9a-4c06-c684-eb626795b332"));
	}

	@Test
	public void testExtractElementsFromBundleShouldGetAllRelevantElements() throws Exception {
		List<Element> elements = pathEvaluatorLibrary.extractElementsFromBundle(getDeviceDefinitionBundle(),
		    "$this.entry.resource.where(identifier.where(value='d3fdac0e-061e-b068-2bed-5a95e803636f')).property.where(type.where(text='RDTScan Configuration')).valueCode");
		assertEquals(9, elements.size());
	}

	@Test
	public void testExtractResourceFromBundleShouldExtractCorrectResource() throws Exception {
		verifyResourceIsExtracted("d3fdac0e-061e-b068-2bed-5a95e803636f");
		verifyResourceIsExtracted("cf4443a1-f582-74ea-be89-ae53b5fd7bfe");
	}

	@Test
	public void testExtractStringFromBundleShouldReturnNullWhenFHIRExceptionOccurs() {
		Assert.assertNull(pathEvaluatorLibrary.extractStringFromBundle(mock(Bundle.class), "null=null"));
	}

	@Test
	public void testExtractStringsFromBundleShouldReturnEmptyListWhenFHIRExceptionOccurs() {
		Assert.assertEquals(0, pathEvaluatorLibrary.extractStringsFromBundle(mock(Bundle.class), "null=null").size());
	}

	@Test
	public void testExtractElementsFromBundleShouldReturnEmptyListWhenFHIRExceptionOccurs() {
		Assert.assertEquals(0, pathEvaluatorLibrary.extractElementsFromBundle(mock(Bundle.class), "null=null").size());
	}

	@Test
	public void testExtractResourceFromBundleShouldReturnNullWhenFHIRExceptionOccurs() {
		Assert.assertNull(pathEvaluatorLibrary.extractResourceFromBundle(mock(Bundle.class), "null=null"));
	}

	@Test
	public void testStockDaoAssignmentShouldReturnNullInitiallyThenNotNullAfterAssignment(){
		PathEvaluatorLibrary.init(null,null,null,null);
		PathEvaluatorLibrary pathEvaluatorLibrary = PathEvaluatorLibrary.getInstance();
		Assert.assertNull(pathEvaluatorLibrary.getStockProvider().getStockDao());

		pathEvaluatorLibrary.setStockDao(mock(StockDao.class));

		Assert.assertNotNull(pathEvaluatorLibrary.getStockProvider().getStockDao());
	}

	private void verifyCorrectStringIsExtracted(String resourceId, String expectedString) throws Exception {
		String str = pathEvaluatorLibrary.extractStringFromBundle(getDeviceDefinitionBundle(), String.format(
		    "$this.entry.resource.where(identifier.where(value='%s')).capability.where(type.where(text='instructions')).description.text",
		    resourceId));
		assertEquals(expectedString, str);
	}

	private void verifyResourceIsExtracted(String resourceId) throws Exception {
		DeviceDefinition resource = (DeviceDefinition) pathEvaluatorLibrary.extractResourceFromBundle(
		    getDeviceDefinitionBundle(),
		    String.format("$this.entry.resource.where(identifier.where(value='%s')))", resourceId));
		assertNotNull(resource);
		assertEquals(resourceId, resource.getIdentifier().get(0).getValue().getValue());
	}

	private Bundle getDeviceDefinitionBundle() throws Exception {
		return getResource("DeviceDefinition.json");
	}

	private <T extends Resource> T getResource(String fileName) throws Exception {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
		return FHIRParser.parser(Format.JSON).parse(stream);
	}
}
