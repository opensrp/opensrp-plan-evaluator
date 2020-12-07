package org.smartregister.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.fhir.model.resource.Basic;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.domain.ProductCatalogue;
import org.smartregister.domain.Stock;
import org.smartregister.pathevaluator.dao.ProductCatalogueDao;
import org.smartregister.utils.TaskDateTimeTypeConverter;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StockConverter.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class StockConverterTest {

	@Mock
	private ProductCatalogueDao productCatalogueDao;

	private String STOCK_JSON = " {\n"
			+ " \t\"type\": \"Stock\",\n"
			+ " \t\"serverVersion\": 1521112142815,\n"
			+ " \t\"identifier\": 1,\n"
			+ " \t\"transaction_type\": \"Inventory\",\n"
			+ " \t\"providerid\": \"3c11c3ad-c366-443f-8ca8-c8c21f5bdd22\",\n"
			+ " \t\"value\": 10,\n"
			+ " \t\"version\": 1606748740346,\n"
			+ " \t\"deliveryDate\": \"Jun 6, 2020, 5:00:00 AM\",\n"
			+ " \t\"accountabilityEndDate\": \"Apr 6, 2021, 5:00:00 AM\",\n"
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
	public void testConvertToPatientResource() throws IllegalAccessException {
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

		StockConverter stockConverter = new StockConverter();
		Field field = PowerMockito.field(StockConverter.class, "productCatalogueDao");
		field.set(StockConverter.class, productCatalogueDao);
		when(productCatalogueDao.getProductCatalogueById(anyLong())).thenReturn(productCatalogue);
		Basic basic = stockConverter.convertStockToBasicResource(stock);
		assertNotNull(basic);
		assertEquals(basic.getCode().getCoding().get(0).getCode().getValue(), "Inventory");
		assertEquals(basic.getSubject().getReference().getValue(), "1");
		assertEquals(basic.getCreated().getValue().toString(),"2018-03-13");
//		assertEquals(basic.getAuthor().getValue().toString(),"2018-03-13"); //TODO
		assertEquals(basic.getIdentifier().size(), 7);
		assertEquals(basic.getIdentifier().get(0).getSystem().getValue(), "productName");
		assertEquals(basic.getIdentifier().get(0).getValue().getValue(), "School supplies");

		assertEquals(basic.getIdentifier().get(1).getSystem().getValue(), "isAttractiveItem");
		assertEquals(basic.getIdentifier().get(1).getValue().getValue(), "true");

		assertEquals(basic.getIdentifier().get(2).getSystem().getValue(), "materialNumber");
		assertEquals(basic.getIdentifier().get(2).getValue().getValue(), "MX-1234");

		assertEquals(basic.getIdentifier().get(3).getSystem().getValue(), "availability");
		assertEquals(basic.getIdentifier().get(3).getValue().getValue(), "Yes available");

		assertEquals(basic.getIdentifier().get(4).getSystem().getValue(), "condition");
		assertEquals(basic.getIdentifier().get(4).getValue().getValue(), "Not in a good condition");

		assertEquals(basic.getIdentifier().get(5).getSystem().getValue(), "isAppropriateUsage");
		assertEquals(basic.getIdentifier().get(5).getValue().getValue(), "Yes");

		assertEquals(basic.getIdentifier().get(6).getSystem().getValue(), "accountabilityPeriod");
		assertEquals(basic.getIdentifier().get(6).getValue().getValue(), "12");

		assertEquals(basic.getExtension().size(), 7);
		assertEquals(basic.getExtension().get(0).getValue().as(com.ibm.fhir.model.type.String.class).getValue(), "Health");
		assertEquals(basic.getExtension().get(1).getValue().as(com.ibm.fhir.model.type.String.class).getValue(), "10");
//		assertEquals(basic.getExtension().get(2).getValue().as(com.ibm.fhir.model.type.String.class).getValue(), "Sat Jun 06 05:00:00 PKT 2020");
		assertEquals(basic.getExtension().get(3).getValue().as(com.ibm.fhir.model.type.String.class).getValue(), "ADB");
		assertEquals(basic.getExtension().get(4).getValue().as(com.ibm.fhir.model.type.String.class).getValue(), "90397");
		assertEquals(basic.getExtension().get(5).getValue().as(com.ibm.fhir.model.type.String.class).getValue(), "111");
		assertEquals(basic.getExtension().get(6).getValue().as(com.ibm.fhir.model.type.String.class).getValue(), "1234serial");

		System.out.println(basic);
	}

}
