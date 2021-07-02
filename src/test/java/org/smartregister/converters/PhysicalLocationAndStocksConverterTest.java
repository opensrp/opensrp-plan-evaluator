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

	private static String physicalLocationAndStocksJsonObject = "{\"stocks\":[{\"type\":\"Stock\",\"serverVersion\":1521112142815,\"identifier\":15,\"transaction_type\":\"Inventory\",\"providerid\":\"3c11c3ad-c366-443f-8ca8-c8c21f5bdd22\",\"value\":10,\"version\":1606748740346,\"deliveryDate\":\"2020-06-06\",\"accountabilityEndDate\":\"2020-07-02T10:00:00.000+0000\",\"donor\":\"ADB\",\"serialNumber\":\"1234serial\",\"locationId\":\"90398\",\"customProperties\":{\"PO Number\":\"111\",\"UNICEF section\":\"Health\"},\"id\":\"05934ae338431f28bf6793b241b3cd6f\",\"date_created\":\"1520892000000\",\"_rev\":\"v1\"},{\"type\":\"Stock\",\"serverVersion\":1521112142815,\"identifier\":1,\"transaction_type\":\"Inventory\",\"providerid\":\"3c11c3ad-c366-443f-8ca8-c8c21f5bdd22\",\"value\":10,\"version\":1606748740346,\"deliveryDate\":\"2020-06-06\",\"accountabilityEndDate\":\"2020-07-02T10:00:00.000+0000\",\"donor\":\"ADB\",\"serialNumber\":\"1234serial\",\"locationId\":\"90398\",\"customProperties\":{\"PO Number\":\"111\",\"UNICEF section\":\"Health\"},\"id\":\"05934ae338431f28bf6793b24181ea5e\",\"date_created\":\"1520892000000\",\"_rev\":\"v1\"}],\"type\":\"Feature\",\"id\":\"90398\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[43.5978597,-14.1699446],[43.5978956,-13.1699609],[43.5978794,-13.1699947],[42.5978434,-14.1699784],[42.5978597,-14.1699446]]]},\"properties\":{\"uid\":\"3970790a-5a00-11ea-82b4-0242ac130003\",\"code\":\"21384421\",\"type\":\"Larval Dipping\",\"status\":\"Active\",\"parentId\":\"3724\",\"geographicLevel\":4,\"version\":0},\"serverVersion\":1542376382842,\"locationTags\":null}";

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
		assertTrue(PathEvaluatorLibrary.getInstance()
				.evaluateBooleanExpression(bundle,
						"Bundle.entry.resource.ofType(SupplyDelivery).identifier.where(system='isPastAccountabilityDate' and value='true').exists()"));
		System.out.println(bundle);
	}
}
