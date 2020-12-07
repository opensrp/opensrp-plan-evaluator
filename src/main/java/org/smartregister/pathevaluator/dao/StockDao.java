package org.smartregister.pathevaluator.dao;

import com.ibm.fhir.model.resource.Basic;
import org.smartregister.domain.Stock;

import java.util.List;

public interface StockDao {

	List<Basic> findInventoryItemsInAJurisdiction(String jurisdictionId);

	List<Basic> findInventoryInAServicePoint(String servicePointId);

}
