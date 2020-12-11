package org.smartregister.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.SupplyDelivery;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.FHIRDeviceStatus;
import com.ibm.fhir.model.type.code.SupplyDeliveryStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.smartregister.domain.ProductCatalogue;
import org.smartregister.domain.Stock;
import org.smartregister.domain.StockAndProductDetails;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StockConverterTest {

	private String STOCK_JSON = " {\n"
			+ " \t\"type\": \"Stock\",\n"
			+ " \t\"serverVersion\": 1521112142815,\n"
			+ " \t\"identifier\": 1,\n"
			+ " \t\"transaction_type\": \"Inventory\",\n"
			+ " \t\"providerid\": \"3c11c3ad-c366-443f-8ca8-c8c21f5bdd22\",\n"
			+ " \t\"value\": 10,\n"
			+ " \t\"version\": 1606748740346,\n"
			+ " \t\"deliveryDate\": \"2020-06-06\",\n"
			+ " \t\"accountabilityEndDate\": \"2021-06-21\",\n"
			+ " \t\"donor\": \"ADB\",\n"
			+ " \t\"serialNumber\": \"1234serial\",\n"
			+ " \t\"locationId\": \"90397\",\n"
			+ " \t\"customProperties\": {\n"
			+ " \t\t\"PO Number\": \"111\",\n"
			+ " \t\t\"UNICEF section\": \"Health\"\n"
			+ " \t},\n"
			+ " \t\"_id\": \"7e278a0d-a4eb-4d23-aea0-836d813836a5\",\n"
			+ " \t\"date_created\": \"1520892000000\",\n"
			+ " \t\"_rev\": \"v1\"\n"
			+ " }";

	private static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
			.serializeNulls().create();

	@Test
	public void testConvertToBundleResource() {

		StockAndProductDetails stockAndProductDetails = new StockAndProductDetails();
		Stock stock = gson.fromJson(STOCK_JSON, Stock.class);
		stock.setId("7e278a0d-a4eb-4d23-aea0-836d813836a5");
		ProductCatalogue productCatalogue = new ProductCatalogue();
		productCatalogue.setProductName("School supplies");
		productCatalogue.setIsAttractiveItem(true);
		productCatalogue.setMaterialNumber("MX-1234");
		productCatalogue.setAvailability("Yes available");
		productCatalogue.setCondition("Not in a good condition");
		productCatalogue.setAppropriateUsage("Yes");
		productCatalogue.setAccountabilityPeriod(12);
		productCatalogue.setUniqueId(1l);
		stockAndProductDetails.setProductCatalogue(productCatalogue);
		stockAndProductDetails.setStock(stock);

		Bundle bundle = StockConverter.convertStockToBundleResource(stockAndProductDetails);
		assertNotNull(bundle);
		assertEquals("7e278a0d-a4eb-4d23-aea0-836d813836a5", bundle.getId());
		assertEquals(BundleType.COLLECTION, bundle.getType());
		assertEquals(2, bundle.getEntry().size());
		assertEquals("https://fhir.smartregister.org/device/" + productCatalogue.getUniqueId(),
				bundle.getEntry().get(0).getFullUrl().getValue());
		assertEquals("7e278a0d-a4eb-4d23-aea0-836d813836a5", bundle.getEntry().get(0).getResource().getId());
		assertEquals(FHIRDeviceStatus.ACTIVE.getValue(),
				((Device) bundle.getEntry().get(0).getResource()).getStatus().getValue());
		assertEquals("1234serial", ((Device) bundle.getEntry().get(0).getResource()).getSerialNumber().getValue());
		assertEquals("Organization/Health",
				((Device) bundle.getEntry().get(0).getResource()).getOwner().getReference().getValue());
		assertEquals("Location/90397",
				((Device) bundle.getEntry().get(0).getResource()).getLocation().getReference().getValue());

		assertEquals("https://fhir.smartregister.org/supplyDelivery/" + stock.getId(),
				bundle.getEntry().get(1).getFullUrl().getValue());
		assertEquals("111",
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getIdentifier().get(0).getValue().getValue());
		assertEquals(SupplyDeliveryStatus.COMPLETED.getValue(),
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getStatus().getValue());
		assertEquals("http://terminology.hl7.org/CodeSystem/supply-item-type",
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getType().getCoding().get(0).getSystem()
						.getValue());
		assertEquals("device",
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getType().getCoding().get(0).getCode().getValue());
		assertEquals(BigDecimal.TEN,
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getSuppliedItem().getQuantity().getValue()
						.getValue());
		assertEquals(1, ((SupplyDelivery) bundle.getEntry().get(1).getResource()).getSuppliedItem().getItem().as(
				CodeableConcept.class).getCoding().size());
		assertEquals("School supplies",
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getSuppliedItem().getItem().as(
						CodeableConcept.class).getCoding().get(0).getCode().getValue());
		assertEquals("School supplies",
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getSuppliedItem().getItem().as(
						CodeableConcept.class).getCoding().get(0).getDisplay().getValue());
		assertEquals("2020-06-06", ((SupplyDelivery) bundle.getEntry().get(1).getResource()).getOccurrence().as(
				com.ibm.fhir.model.type.DateTime.class).getValue().toString());
		assertEquals("Organization/ADB",
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getSupplier().getReference().getValue());
		assertEquals("Organization/ADB",
				((SupplyDelivery) bundle.getEntry().get(1).getResource()).getSupplier().getDisplay().getValue());
		System.out.println(bundle);

	}

}
