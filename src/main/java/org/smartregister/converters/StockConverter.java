package org.smartregister.converters;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.SupplyDelivery;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.FHIRDeviceStatus;
import com.ibm.fhir.model.type.code.SupplyDeliveryStatus;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.ISODateTimeFormat;
import org.smartregister.domain.Stock;
import org.smartregister.domain.StockAndProductDetails;
import org.smartregister.utils.ApplicationConstants;
import org.smartregister.utils.ApplicationProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class StockConverter {

	private static final java.lang.String PO_NUMBER = "PO Number";

	private static final java.lang.String DEVICE = "device";

	private static final java.lang.String UNICEF_SECTION = "UNICEF section";

	private static final java.lang.String SUPPLY_DELIVERY = "supplyDelivery";

	private static final java.lang.String CODE_SYSTEM = "CodeSystem";

	private static final java.lang.String SUPPLY_ITEM_TYPE = "supply-item-type";

	private static final java.lang.String ORGANIZATION = "Organization";

	private static final java.lang.String LOCATION = "Location";

	private static final java.lang.String IS_PAST_ACCOUNTABILITY_DATE = "isPastAccountabilityDate";

	public static Bundle convertStockToBundleResource(StockAndProductDetails stockAndProductDetails) {

		Bundle.Builder bundleBuilder = Bundle.builder();

		Device.Builder deviceBuilder = Device.builder();
		if (stockAndProductDetails != null && stockAndProductDetails.getStock() != null) {
			deviceBuilder.id(stockAndProductDetails.getStock().getId());
		}

		java.lang.String unicefSection = stockAndProductDetails.getStock() != null
				&& stockAndProductDetails.getStock().getCustomProperties() != null ?
				stockAndProductDetails.getStock().getCustomProperties().get(UNICEF_SECTION) : "";

		java.lang.String locationId = stockAndProductDetails != null && stockAndProductDetails.getStock() != null &&
				stockAndProductDetails.getStock().getLocationId() != null ?
				stockAndProductDetails.getStock().getLocationId() :
				"";

		Reference owner = Reference.builder().reference(String.of(
				java.lang.String.format("%s/%s", ORGANIZATION, unicefSection))).build();
		Reference location = Reference.builder().reference(String.of(
				java.lang.String.format("%s/%s", LOCATION, locationId))).build();

		String serialNumber = stockAndProductDetails != null && stockAndProductDetails.getStock() != null &&
				StringUtils.isNotBlank(stockAndProductDetails.getStock().getSerialNumber()) ?
				String.of(stockAndProductDetails.getStock().getSerialNumber()) : null;
		FHIRDeviceStatus fhirDeviceStatus = FHIRDeviceStatus.ACTIVE;
		deviceBuilder.owner(owner).location(location).serialNumber(serialNumber)
				.status(fhirDeviceStatus);

		List<Bundle.Entry> entryList = new ArrayList<>();
		java.lang.String productId =
				stockAndProductDetails != null && stockAndProductDetails.getProductCatalogue() != null &&
						stockAndProductDetails.getProductCatalogue().getUniqueId() != null ?
						java.lang.String.valueOf(stockAndProductDetails.getProductCatalogue().getUniqueId()) : "";

		java.lang.String deviceUriString = java.lang.String.format("%s/%s/%s",
				ApplicationProperties.getInstance().getProperty(ApplicationConstants.PropertiesConstants.FHIR_SERVER_URL),
				DEVICE, productId);
		Uri deviceUri = Uri.builder().value(deviceUriString).build();
		Bundle.Entry deviceEntry = Bundle.Entry.builder().fullUrl(deviceUri).resource(deviceBuilder.build()).build();
		entryList.add(deviceEntry);
		entryList.add(createBundleEntryFromStockAndProduct(stockAndProductDetails));
		if (stockAndProductDetails != null && stockAndProductDetails.getStock() != null) {
			bundleBuilder.id(stockAndProductDetails.getStock().getId());
		}

		return bundleBuilder.type(BundleType.COLLECTION).entry(entryList).build();
	}

	public static Bundle.Entry createBundleEntryFromStockAndProduct(StockAndProductDetails stockAndProductDetails) {
		Identifier poNumberIdentifier = null;
		Reference supplier = null;
		Element deviceItemElement = null;
		java.lang.String deliveryDateInString;
		Element occurrenceDateTime;
		Collection<Identifier> identifiers = new ArrayList<>();

		Code deviceItemCode;
		Coding deviceItemCoding;
		Uri system = Uri.builder().value(java.lang.String.format("%s/%s/%s", ApplicationProperties.getInstance().getProperty(
				ApplicationConstants.PropertiesConstants.HL7_TERMINOLOGY_URL), CODE_SYSTEM, SUPPLY_ITEM_TYPE)).build();
		Code deviceCode = Code.builder().value(DEVICE).build();
		Coding coding = Coding.builder().code(deviceCode).system(system).build();
		CodeableConcept typeCodeableConcept = CodeableConcept.builder().coding(coding).build();
		SupplyDelivery.Builder supplyDeliveryBuilder = SupplyDelivery.builder();
		Decimal decimal = Decimal.builder().value(java.lang.String.valueOf(
				stockAndProductDetails != null && stockAndProductDetails.getStock() != null ?
						stockAndProductDetails.getStock().getValue() :
						"0.0"))
				.build();

		java.lang.String donor = stockAndProductDetails != null && stockAndProductDetails.getStock() != null &&
				stockAndProductDetails.getStock().getDonor() != null ?
				java.lang.String.format("%s/%s", ORGANIZATION, stockAndProductDetails.getStock().getDonor()) :
				null;

		String donorString = donor != null ? String.of(donor) : null;

		if (donorString != null) {
			supplier = Reference.builder().reference(donorString)
					.display(donorString).build();
		}

		Date deliveryDate = stockAndProductDetails != null && stockAndProductDetails.getStock() != null
				&& stockAndProductDetails.getStock().getDeliveryDate() != null ?
				stockAndProductDetails.getStock().getDeliveryDate() :
				null;

		deliveryDateInString = deliveryDate != null ? ISODateTimeFormat.date().print(deliveryDate.getTime()) : null;
		occurrenceDateTime = deliveryDateInString != null ? DateTime.builder().value(deliveryDateInString).build() : null;

		if (stockAndProductDetails != null && stockAndProductDetails.getProductCatalogue() != null &&
				stockAndProductDetails.getProductCatalogue().getProductName() != null) {
			deviceItemCode = Code.builder().value(stockAndProductDetails.getProductCatalogue().getProductName())
					.build();
			deviceItemCoding = Coding.builder().code(deviceItemCode)
					.display(String.of(stockAndProductDetails.getProductCatalogue().getProductName())).build();
			deviceItemElement = CodeableConcept.builder().coding(deviceItemCoding).build();
		}

		SimpleQuantity simpleQuantity = SimpleQuantity.builder().value(decimal).build();

		SupplyDelivery.SuppliedItem suppliedItem = SupplyDelivery.SuppliedItem.builder().quantity(simpleQuantity)
				.item(deviceItemElement).build();

		java.lang.String poNumber = stockAndProductDetails.getStock() != null
				&& stockAndProductDetails.getStock().getCustomProperties() != null ?
				stockAndProductDetails.getStock().getCustomProperties().get(PO_NUMBER) : "";

		String poNumberString = poNumber != null ? String.of(poNumber) : null;

		if (poNumberString != null) {
			poNumberIdentifier = Identifier.builder().system(Uri.builder().value(PO_NUMBER.replace(" ", "")).build())
					.value(poNumberString).build();
			if (poNumberIdentifier != null) {
				identifiers.add(poNumberIdentifier);
			}
		}

		java.lang.String stockId = stockAndProductDetails != null && stockAndProductDetails.getStock() != null &&
				stockAndProductDetails.getStock().getId() != null ?
				java.lang.String.valueOf(stockAndProductDetails.getStock().getId()) :
				"";

		Identifier identifier;
		if (stockAndProductDetails != null && stockAndProductDetails.getStock() != null) {
			Stock stock = stockAndProductDetails.getStock();
			if (stock.getAccountabilityEndDate() != null) {
				Date accountabilityDate = stock.getAccountabilityEndDate();
				boolean isInPast = accountabilityDate.before(new Date());
				identifier = Identifier.builder().system(Uri.builder().value(IS_PAST_ACCOUNTABILITY_DATE).build())
						.value(String.builder().value(java.lang.String.valueOf(isInPast)).build())
						.build();
				identifiers.add(identifier);
			}
		}
		SupplyDelivery supplyDelivery = supplyDeliveryBuilder.id(stockId).suppliedItem(suppliedItem)
				.status(SupplyDeliveryStatus.COMPLETED)
				.supplier(supplier).type(typeCodeableConcept)
				.occurrence(occurrenceDateTime)
				.identifier(identifiers).build();

		java.lang.String supplyDeliveryUriString = java.lang.String.format("%s/%s/%s",
				ApplicationProperties.getInstance().getProperty(ApplicationConstants.PropertiesConstants.FHIR_SERVER_URL),
				SUPPLY_DELIVERY, stockId);

		Uri supplyDeliveryUri = Uri.builder().value(supplyDeliveryUriString).build();

		return Bundle.Entry.builder().fullUrl(supplyDeliveryUri)
				.resource(supplyDelivery).build();
	}

}
