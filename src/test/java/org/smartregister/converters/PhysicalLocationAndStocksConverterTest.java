package org.smartregister.converters;

import com.google.gson.Gson;
import com.ibm.fhir.model.resource.Bundle;
import org.junit.Test;
import org.smartregister.domain.PhysicalLocationAndStocks;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhysicalLocationAndStocksConverterTest {

	private static String physicalLocationAndStocksJsonObject = "{\"stocks\":[{\"identifier\":\"15\",\"vaccine_type_id\":\"1\",\"transaction_type\":\"issued\",\"providerid\":\"biddemo1\",\"value\":-55,\"to_from\":\"C/C\",\"version\":1521112141441,\"deliveryDate\":null,\"accountabilityEndDate\":null,\"donor\":null,\"serialNumber\":null,\"locationId\":\"90398\",\"customProperties\":{},\"product\":null,\"creator\":null,\"editor\":null,\"dateEdited\":null,\"voided\":null,\"dateVoided\":null,\"voider\":null,\"voidReason\":null,\"serverVersion\":1521112141441,\"clientApplicationVersion\":null,\"clientDatabaseVersion\":null,\"type\":\"Stock\",\"id\":\"05934ae338431f28bf6793b241b3cd6f\",\"revision\":\"1-83e0bc2abeda38e5aa282d1f8d75e6bc\",\"conflicts\":null},{\"identifier\":\"1\",\"vaccine_type_id\":\"2\",\"transaction_type\":\"issued\",\"providerid\":\"biddemo\",\"value\":-3,\"to_from\":\"C/C\",\"date_updated\":1520939303991,\"version\":1521003030510,\"deliveryDate\":null,\"accountabilityEndDate\":null,\"donor\":null,\"serialNumber\":null,\"locationId\":\"90398\",\"customProperties\":{},\"product\":null,\"creator\":null,\"editor\":null,\"dateEdited\":null,\"voided\":null,\"dateVoided\":null,\"voider\":null,\"voidReason\":null,\"serverVersion\":1521003030510,\"clientApplicationVersion\":null,\"clientDatabaseVersion\":null,\"type\":\"Stock\",\"id\":\"05934ae338431f28bf6793b24181ea5e\",\"revision\":\"1-4f090916632e2ae10f1ef7f972aaf6d2\",\"conflicts\":null}],\"type\":\"Feature\",\"id\":\"90398\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[43.5978597,-14.1699446],[43.5978956,-13.1699609],[43.5978794,-13.1699947],[42.5978434,-14.1699784],[42.5978597,-14.1699446]]]},\"properties\":{\"uid\":\"3970790a-5a00-11ea-82b4-0242ac130003\",\"code\":\"21384421\",\"type\":\"Larval Dipping\",\"status\":\"Active\",\"parentId\":\"3724\",\"geographicLevel\":4,\"version\":0},\"serverVersion\":1542376382842,\"locationTags\":null}";

	@Test
	public void testConvertLocationAndStocksToBundleResourceShouldReturnLocationStockBundle() {
		PhysicalLocationAndStocks physicalLocationAndStocks = new Gson().fromJson(physicalLocationAndStocksJsonObject,
				PhysicalLocationAndStocks.class);
		Bundle bundle = PhysicalLocationAndStocksConverter
				.convertLocationAndStocksToBundleResource(physicalLocationAndStocks);
		assertEquals(physicalLocationAndStocks.getId(), bundle.getId());
		assertFalse(bundle.getEntry().isEmpty());
		assertEquals(3, bundle.getEntry().size());
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle, "Bundle.entry.resource.ofType(Location).id = '90398'"));
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle, "Bundle.entry.resource.ofType(Location).partOf.reference='3724'"));
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle,
						"Bundle.entry.resource.ofType(Location).identifier.where(system='hasGeometry').value='true'"));
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle, "Bundle.entry.resource.ofType(Location).count() = 1"));
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle, "Bundle.entry.resource.ofType(SupplyDelivery).count() = 2"));
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle,
						"Bundle.entry.resource.ofType(SupplyDelivery).where(id='05934ae338431f28bf6793b241b3cd6f').exists()"));
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle,
						"Bundle.entry.resource.ofType(SupplyDelivery).where(id='05934ae338431f28bf6793b24181ea5e').exists()"));
		System.out.println(bundle);
	}
}
