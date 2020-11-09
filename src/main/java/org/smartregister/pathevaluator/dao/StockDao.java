package org.smartregister.pathevaluator.dao;

import org.smartregister.domain.Stock;

import java.util.List;

public interface StockDao {

	List<Stock> findInventoryItemsInAJurisdiction(String jurisdictionId);

	List<Stock> findInventoryInAServicePoint(String servicePointId);

}
