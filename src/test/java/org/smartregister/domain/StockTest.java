package org.smartregister.domain;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class StockTest {

	@Test
	public void testGetterAndSetter() {
		Validator validator = ValidatorBuilder.create().with(new SetterTester()).with(new GetterTester()).build();

		validator.validate(PojoClassFactory.getPojoClass(Stock.class));
	}

	@Test
	public void testConstructor1() {
		String identifier = "identifier";
		String vaccineTypeId = "polio-1";
		String transactionType = "restock";
		String providerId = "catherine";
		int value = 45;
		long dateCreated = 8900;
		String toFrom = "0-45";
		Long dateUpdated = 8907L;
		long version = 880;

		Stock stock = new Stock(identifier, vaccineTypeId, transactionType, providerId, value, dateCreated, toFrom, dateUpdated, version);

		Assert.assertEquals(identifier, stock.getIdentifier());
		Assert.assertEquals(vaccineTypeId, stock.getVaccineTypeId());
		Assert.assertEquals(transactionType, stock.getTransactionType());
		Assert.assertEquals(providerId, stock.getProviderid());
		Assert.assertEquals(value, stock.getValue());
		Assert.assertEquals(0L, dateCreated, stock.getDate_created());
		Assert.assertEquals(toFrom, stock.getToFrom());
		Assert.assertEquals(dateUpdated, stock.getDateUpdated());
		Assert.assertEquals(version, stock.getVersion());
	}


	@Test
	public void testConstructor2() {
		String identifier = "identifier";
		String vaccineTypeId = "polio-1";
		String transactionType = "restock";
		String providerId = "catherine";
		int value = 45;
		long dateCreated = 8900;
		String toFrom = "0-45";
		Long dateUpdated = 8907L;
		long version = 880;
		String donor = "WHO";
		Date deliveryDate = new Date();
		String serialNo = "serial-no";
		String locationId = "serial-no";

		Inventory inventory = new Inventory();
		inventory.setDonor(donor);
		inventory.setDeliveryDate(deliveryDate);
		inventory.setSerialNumber(serialNo);
		inventory.setProviderId(providerId);
		inventory.setServicePointId(locationId);

		StockObjectMetadata stockObjectMetadata = new StockObjectMetadata();
		stockObjectMetadata.setDate_created(dateCreated);
		stockObjectMetadata.setTo_from(toFrom);
		stockObjectMetadata.setDate_updated(dateUpdated);
		stockObjectMetadata.setVersion(version);


		Stock stock = new Stock(identifier, vaccineTypeId, transactionType, providerId, value, stockObjectMetadata, inventory);

		Assert.assertEquals(identifier, stock.getIdentifier());
		Assert.assertEquals(vaccineTypeId, stock.getVaccineTypeId());
		Assert.assertEquals(transactionType, stock.getTransactionType());
		Assert.assertEquals(providerId, stock.getProviderid());
		Assert.assertEquals(value, stock.getValue());
		Assert.assertEquals(0L, dateCreated, stock.getDate_created());
		Assert.assertEquals(toFrom, stock.getToFrom());
		Assert.assertEquals(dateUpdated, stock.getDateUpdated());
		Assert.assertEquals(version, stock.getVersion());
		Assert.assertEquals(deliveryDate, stock.getDeliveryDate());
		Assert.assertEquals(donor, stock.getDonor());
		Assert.assertEquals(serialNo, stock.getSerialNumber());
		Assert.assertEquals(locationId, stock.getLocationId());
	}
}
