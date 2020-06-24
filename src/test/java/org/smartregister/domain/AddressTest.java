package org.smartregister.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.smartregister.common.AddressField;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.smartregister.domain.Address;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddressTest {
	
	@Test
	public void testEqualAndHashCode() {
		EqualsVerifier.forClass(Address.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}
	
	@Test
	public void testGetterAndSetter() {
		Validator validator = ValidatorBuilder.create().with(new SetterTester()).with(new GetterTester()).build();
		
		validator.validate(PojoClassFactory.getPojoClass(Address.class));
	}
	
	@Test
	public void testSetAndGetAddressField() {
		Address address = new Address();
		address.withAddressFields(null);
		assertNull(address.getAddressFields());
		
		address.withAddressField("field", "val");
		assertEquals("val", address.getAddressField("field"));
		assertNull(address.getAddressField("ddd"));
		
		address.withAddressFields(null);
		assertNull(address.getAddressFields());
		
		address.withAddressField(AddressField.AREA, "val");
		assertEquals("val", address.getAddressField(AddressField.AREA));
		assertNull(address.getAddressField(AddressField.HOUSE_NUMBER));
		
		address.withAddressFields(null);
		assertNull(address.getAddressFields());
		
	}
	
	@Test
	public void testAddAndRemoveAddressField() {
		Address address = new Address();
		address.withAddressFields(null);
		assertNull(address.getAddressFields());
		
		address.addAddressField("field", "val");
		address.addAddressField(AddressField.AREA, "val");
		assertEquals(2, address.getAddressFields().size());
		assertEquals("val", address.getAddressField("field"));
		assertEquals("val", address.getAddressField(AddressField.AREA));
		
		address.removeAddressField("ddd");
		address.removeAddressField(AddressField.HOUSE_NUMBER);
		assertEquals(2, address.getAddressFields().size());
		assertEquals("val", address.getAddressField("field"));
		assertEquals("val", address.getAddressField(AddressField.AREA));
		
		address.removeAddressField("field");
		address.removeAddressField(AddressField.AREA);
		assertEquals(0, address.getAddressFields().size());
		assertNull(address.getAddressField("field"));
		assertNull(address.getAddressField(AddressField.AREA));
	}
	
	@Test
	public void testGetAddressFieldUsingRegex() {
		Address address = new Address();
		address.withAddressFields(null);
		assertNull(address.getAddressFields());
		
		address.addAddressField(AddressField.AREA, "area");
		address.addAddressField("field", "val");
		
		assertNull(address.getAddressFieldMatchingRegex(""));
		assertNull(address.getAddressFieldMatchingRegex("^[0-9]+"));
		assertEquals("val", address.getAddressFieldMatchingRegex("^field"));
		assertNull(address.getAddressFieldMatchingRegex("^TYPE"));
		assertEquals("area", address.getAddressFieldMatchingRegex("^AREA"));
		assertNull(address.getAddressFieldMatchingRegex("^area"));
		assertNull(address.getAddressFieldMatchingRegex("^id"));
		
	}
	
	@Test
	public void testIsActive() {
		Address address = new Address();
		assertTrue(address.isActive());
		Date date = new Date();
		date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(5));
		address.withEndDate(date);
		assertTrue(address.isActive());
		date.setTime(date.getTime() - TimeUnit.DAYS.toMillis(5));
		address.withEndDate(date);
		assertFalse(address.isActive());
	}

	@Test
	public void testDurationCalculation() {
		Address address = new Address();
		assertEquals(-1, address.durationInDays());
		Date date = new Date();
		date.setTime(date.getTime() - TimeUnit.DAYS.toMillis(5));
		address.withStartDate(date);
		assertEquals(5, address.durationInDays());

		address.withStartDate(new Date());
		date = new Date();
		date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(62));
		address.withEndDate(date);
		assertEquals(2, address.durationInMonths());

		date = new Date();
		date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(14));
		address.withEndDate(date);
		assertEquals(2, address.durationInWeeks());

		date = new Date();
		date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(730));
		address.withEndDate(date);
		assertEquals(2, address.durationInYears());
	}

	@Test
	public void testCreatingObject() {
		Address address = new Address();
		String latitude = "222";
		String longitude = "22";
		String geopoint = "geopoint";
		String postalCode = "postalCode";
		String town = "town";
		String subDistrict = "sub";
		String countyDistrict = "count";
		String cityVillage = "village";
		String stateProvince = "province";
		String country = "country";
		address.withLatitude(latitude).withLongitude(longitude).withGeopoint(geopoint).withPostalCode(postalCode)
		        .withTown(town).withSubDistrict(subDistrict).withCountyDistrict(countyDistrict).withCityVillage(cityVillage)
		        .withStateProvince(stateProvince).withCountry(country);

		assertEquals(latitude, address.getLatitude());
		assertEquals(longitude, address.getLongitude());
		assertEquals(geopoint, address.getGeopoint());
		assertEquals(postalCode, address.getPostalCode());
		assertEquals(town, address.getTown());
		assertEquals(subDistrict, address.getSubDistrict());
		assertEquals(countyDistrict, address.getCountyDistrict());
		assertEquals(cityVillage, address.getCityVillage());
		assertEquals(country, address.getCountry());
	}
}
