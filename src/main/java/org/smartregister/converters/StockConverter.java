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
import org.smartregister.domain.StockAndProductDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockConverter {

	public static Bundle convertStockToBundleResource(StockAndProductDetails stockAndProductDetails) {

		Bundle.Builder bundleBuilder = Bundle.builder();

		Device.Builder deviceBuilder = Device.builder();
		if (stockAndProductDetails != null && stockAndProductDetails.getStock() != null) {
			deviceBuilder.id(stockAndProductDetails.getStock().getId());
		}

		java.lang.String unicefSection = stockAndProductDetails.getStock() != null
				&& stockAndProductDetails.getStock().getCustomProperties() != null ?
				stockAndProductDetails.getStock().getCustomProperties().get("UNICEF section") : "";

		java.lang.String locationId = stockAndProductDetails != null && stockAndProductDetails.getStock() != null &&
				stockAndProductDetails.getStock().getLocationId() != null ?
				stockAndProductDetails.getStock().getLocationId() :
				"";

		Reference owner = Reference.builder().reference(String.of("Organization/" + unicefSection)).build();
		Reference location = Reference.builder().reference(String.of("Location/" + locationId)).build();

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

		java.lang.String deviceUriString = "https://fhir.smartregister.org/device/" + productId;
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

		Code deviceItemCode;
		Coding deviceItemCoding;
		Uri system = Uri.builder().value("http://terminology.hl7.org/CodeSystem/supply-item-type").build();
		Code deviceCode = Code.builder().value("device").build();
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
				"Organization/" + stockAndProductDetails.getStock().getDonor() :
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
				stockAndProductDetails.getStock().getCustomProperties().get("PO Number") : "";

		String poNumberString = poNumber != null ? String.of(poNumber) : null;

		if (poNumberString != null) {
			poNumberIdentifier = Identifier.builder().value(poNumberString).build();
		}

		if (poNumberIdentifier != null) {
			supplyDeliveryBuilder.identifier(poNumberIdentifier);
		}

		java.lang.String stockId = stockAndProductDetails != null && stockAndProductDetails.getStock() != null &&
				stockAndProductDetails.getStock().getId() != null ?
				java.lang.String.valueOf(stockAndProductDetails.getStock().getId()) :
				"";

		SupplyDelivery supplyDelivery = supplyDeliveryBuilder.id(stockId).suppliedItem(suppliedItem)
				.status(SupplyDeliveryStatus.COMPLETED)
				.supplier(supplier).type(typeCodeableConcept).occurrence(occurrenceDateTime).build();

		java.lang.String supplyDeliveryUriString = "https://fhir.smartregister.org/supplyDelivery/" + stockId;

		Uri supplyDeliveryUri = Uri.builder().value(supplyDeliveryUriString).build();

		return Bundle.Entry.builder().fullUrl(supplyDeliveryUri)
				.resource(supplyDelivery).build();
	}

}
