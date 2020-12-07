package org.smartregister.converters;

import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import org.smartregister.domain.ProductCatalogue;
import org.smartregister.domain.Stock;
import org.smartregister.pathevaluator.dao.ProductCatalogueDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class StockConverter {

	private static ProductCatalogueDao productCatalogueDao;

	public static Basic convertStockToBasicResource(Stock stock) {
		Basic.Builder builder = Basic.builder().id(stock.getId());
		Code code = Code.builder().value("Inventory").build();
		Coding coding = Coding.builder().code(code).build();
		CodeableConcept codeableConcept = CodeableConcept.builder().coding(coding).build();

		Reference subject = Reference.builder().reference(String.of(java.lang.String.valueOf(stock.getIdentifier())))
				.build();
		Date dateCreated = null;
		
		if(stock != null && stock.getDate_created() != null) {
			java.util.Date utilDate = new java.util.Date(stock.getDate_created());
			java.lang.String dateInString = convertUtilDateToString(utilDate);
			dateCreated = Date.builder().value(dateInString).build();
		}

		Reference author = Reference.builder().reference(String.of(java.lang.String.valueOf(stock.getIdentifier()))).build(); //TODO

		ProductCatalogue productCatalogue = productCatalogueDao.getProductCatalogueById(stock.getIdentifier());
		Collection<Identifier> identifierList = new ArrayList<>();
		Identifier productNameIdentifier = null;
		Identifier isAttractiveItemIdentifier = null;
		Identifier materialNumberIdentifier = null;
		Identifier availabilityIdentifier = null;
		Identifier conditionIdentifier = null;
		Identifier isAppropriateUsageIdentifier = null;
		Identifier accountabilityPeriodIdentifier = null;
		if (productCatalogue != null && productCatalogue.getProductName() != null) {
			productNameIdentifier = Identifier.builder().system(Uri.builder().value("productName").build())
					.value(String.builder().value(productCatalogue.getProductName()).build()).build();
		}
		if (productCatalogue != null && productCatalogue.getIsAttractiveItem() != null) {
			isAttractiveItemIdentifier = Identifier.builder().system(Uri.builder().value("isAttractiveItem").build())
					.value(String.builder().value(java.lang.String.valueOf(productCatalogue.getIsAttractiveItem())).build())
					.build();
		}
		if (productCatalogue != null && productCatalogue.getMaterialNumber() != null) {
			materialNumberIdentifier = Identifier.builder().system(Uri.builder().value("materialNumber").build())
					.value(String.builder().value(productCatalogue.getMaterialNumber()).build()).build();
		}
		if (productCatalogue != null && productCatalogue.getAvailability() != null) {
			availabilityIdentifier = Identifier.builder().system(Uri.builder().value("availability").build())
					.value(String.builder().value(productCatalogue.getAvailability()).build()).build();
		}

		if (productCatalogue != null && productCatalogue.getCondition() != null) {
			conditionIdentifier = Identifier.builder().system(Uri.builder().value("condition").build())
					.value(String.builder().value(productCatalogue.getCondition()).build()).build();
		}

		if (productCatalogue != null && productCatalogue.getAppropriateUsage() != null) {
			isAppropriateUsageIdentifier = Identifier.builder()
					.system(Uri.builder().value("isAppropriateUsage").build())
					.value(String.builder().value(productCatalogue.getAppropriateUsage()).build()).build();
		}

		if (productCatalogue != null && productCatalogue.getAccountabilityPeriod() != null) {
			accountabilityPeriodIdentifier = Identifier.builder()
					.system(Uri.builder().value("accountabilityPeriod").build())
					.value(String.builder().value(java.lang.String.valueOf(productCatalogue.getAccountabilityPeriod()))
							.build()).build();
		}

		if (productNameIdentifier != null) {
			identifierList.add(productNameIdentifier);
		}
		if (isAttractiveItemIdentifier != null) {
			identifierList.add(isAttractiveItemIdentifier);
		}

		if (materialNumberIdentifier != null) {
			identifierList.add(materialNumberIdentifier);
		}
		if (availabilityIdentifier != null) {
			identifierList.add(availabilityIdentifier);
		}
		if (conditionIdentifier != null) {
			identifierList.add(conditionIdentifier);
		}
		if (isAppropriateUsageIdentifier != null) {
			identifierList.add(isAppropriateUsageIdentifier);
		}
		if (accountabilityPeriodIdentifier != null) {
			identifierList.add(accountabilityPeriodIdentifier);
		}

		Collection<Extension> extensions = new ArrayList<>();
		java.lang.String unicefSection = stock != null && stock.getCustomProperties() != null ?
				stock.getCustomProperties().get("UNICEF section") : "";

		java.lang.String poNumber = stock != null && stock.getCustomProperties() != null ?
				stock.getCustomProperties().get("PO Number") : "";

		Extension unicefSectionExtension = null;
		Extension quantityExtension = null;
		Extension deliveryDateExtension = null;
		Extension donorExtension = null;
		Extension servicePointIdExtension = null;
		Extension poNumberExtension = null;
		Extension serialNumberExtension = null;

		if (unicefSection != null) {
			unicefSectionExtension = Extension.builder().value(String.builder().value(unicefSection).build())
					.url("inventory-details").id("Unicef Section").build();
		}
		if (stock != null && stock.getValue() > 0) {
			quantityExtension = Extension.builder()
					.value(String.builder().value(java.lang.String.valueOf(stock.getValue())).build())
					.url("inventory-details").id("Quantity").build();
		}
		if (stock != null && stock.getDeliveryDate() != null) {
			deliveryDateExtension = Extension.builder()
					.value(String.builder().value(java.lang.String.valueOf(stock.getDeliveryDate())).build())
					.url("inventory-details").id("Delivery Date").build();
		}
		if (stock != null && stock.getDonor() != null) {
			donorExtension = Extension.builder()
					.value(String.builder().value(java.lang.String.valueOf(stock.getDonor())).build())
					.url("inventory-details").id("Donor").build();
		}
		if (stock != null && stock.getLocationId() != null) {
			servicePointIdExtension = Extension.builder()
					.value(String.builder().value(java.lang.String.valueOf(stock.getLocationId())).build())
					.url("inventory-details").id("Service Point Id").build();
		}
		if (poNumber != null) {
			poNumberExtension = Extension.builder().value(String.builder().value(poNumber).build())
					.url("inventory-details").id("PO Number").build();
		}
		if (stock != null && stock.getSerialNumber() != null) {
			serialNumberExtension = Extension.builder()
					.value(String.builder().value(java.lang.String.valueOf(stock.getSerialNumber())).build())
					.url("inventory-details").id("Serial Number").build();
		}

		if (unicefSectionExtension != null) {
			extensions.add(unicefSectionExtension);
		}
		if (quantityExtension != null) {
			extensions.add(quantityExtension);
		}
		if (deliveryDateExtension != null) {
			extensions.add(deliveryDateExtension);
		}
		if (donorExtension != null) {
			extensions.add(donorExtension);
		}
		if (serialNumberExtension != null) {
			extensions.add(servicePointIdExtension);
		}
		if (poNumberExtension != null) {
			extensions.add(poNumberExtension);
		}
		if (serialNumberExtension != null) {
			extensions.add(serialNumberExtension);
		}

		builder.code(codeableConcept).subject(subject).created(dateCreated).author(author).identifier(identifierList)
				.extension(extensions);

		return builder.build();
	}

	public static java.lang.String convertUtilDateToString(java.util.Date date) {
		return (date != null) ? new SimpleDateFormat("yyyy-MM-dd").format(date) : null;
	}
}
