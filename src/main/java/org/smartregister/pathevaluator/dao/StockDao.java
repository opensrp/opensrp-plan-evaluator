package org.smartregister.pathevaluator.dao;

import com.ibm.fhir.model.resource.Bundle;

import java.util.List;

public interface StockDao {

	List<Bundle> findInventoryItemsInAJurisdiction(String jurisdictionId);

	List<Bundle> findInventoryInAServicePoint(String servicePointId);

	List<Bundle> getStockById(String stockId);

}
