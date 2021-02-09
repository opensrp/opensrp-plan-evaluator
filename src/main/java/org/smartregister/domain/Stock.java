package org.smartregister.domain;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock extends BaseDataObject {
	
	@JsonProperty
	private String identifier;
	
	@JsonProperty("vaccine_type_id")
	@SerializedName("vaccine_type_id")
	private String vaccineTypeId;
	
	@JsonProperty("transaction_type")
	@SerializedName("transaction_type")
	private String transactionType;
	
	@JsonProperty
	private String providerid;
	
	@JsonProperty
	private int value;
	
	@JsonProperty
	private Long date_created;
	
	@JsonProperty("to_from")
	@SerializedName("to_from")
	private String toFrom;
	
	@JsonProperty("date_updated")
	@SerializedName("date_updated")
	private Long dateUpdated;
	
	@JsonProperty
	private long version;

	@JsonProperty
	private Date deliveryDate;

	@JsonProperty
	private Date accountabilityEndDate;

	@JsonProperty
	private String donor;

	@JsonProperty
	private String serialNumber;

	@JsonProperty
	private String locationId;

	@JsonProperty
	private Map<String, String> customProperties = new HashMap();

	@JsonProperty
	private ProductCatalogue product;

	public Stock() {
		this.version = System.currentTimeMillis();
	}
	
	public Stock(String identifier, String vaccineTypeId, String transactionType, String providerid, int value,
	    Long date_created, String toFrom, Long dateUpdated, long version) {
		this.identifier = identifier;
		this.vaccineTypeId = vaccineTypeId;
		this.transactionType = transactionType;
		this.providerid = providerid;
		this.value = value;
		this.date_created = date_created;
		this.toFrom = toFrom;
		this.dateUpdated = dateUpdated;
		this.version = version;
	}

	public Stock(String identifier, String vaccineTypeId, String transactionType, String providerid, int value,
			StockObjectMetadata stockObjectMetadata, Inventory inventory, ProductCatalogue product) {
		this.identifier = identifier;
		this.vaccineTypeId = vaccineTypeId;
		this.transactionType = transactionType;
		this.providerid = providerid;
		this.value = value;
		this.date_created = stockObjectMetadata.getDate_created();
		this.toFrom = stockObjectMetadata.getTo_from();
		this.dateUpdated = stockObjectMetadata.getDate_updated();
		this.version = stockObjectMetadata.getVersion();
		this.deliveryDate = inventory.getDeliveryDate();
		this.donor = inventory.getDonor();
		this.serialNumber = inventory.getSerialNumber();
		this.locationId = inventory.getServicePointId();
		this.providerid = inventory.getProviderId();
		this.product = product;
	}

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getVaccineTypeId() {
		return vaccineTypeId;
	}
	
	public void setVaccineTypeId(String vaccineTypeId) {
		this.vaccineTypeId = vaccineTypeId;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getProviderid() {
		return providerid;
	}
	
	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public Long getDate_created() {
		return date_created;
	}

	public void setDate_created(Long date_created) {
		this.date_created = date_created;
	}
	
	public String getToFrom() {
		return toFrom;
	}
	
	public void setToFrom(String toFrom) {
		this.toFrom = toFrom;
	}
	
	public Long getDateUpdated() {
		return dateUpdated;
	}
	
	public void setDateUpdated(Long dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getAccountabilityEndDate() {
		return accountabilityEndDate;
	}

	public void setAccountabilityEndDate(Date accountabilityEndDate) {
		this.accountabilityEndDate = accountabilityEndDate;
	}

	public String getDonor() {
		return donor;
	}

	public void setDonor(String donor) {
		this.donor = donor;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public Map<String, String> getCustomProperties() {
		return customProperties;
	}

	public void setCustomProperties(Map<String, String> customProperties) {
		this.customProperties = customProperties;
	}

	public ProductCatalogue getProduct() {
		return product;
	}

	public void setProduct(ProductCatalogue product) {
		this.product = product;
	}

	@Override
	public final boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, "id", "revision");
	}
	
	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id", "revision");
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
