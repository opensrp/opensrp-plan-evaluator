package org.smartregister.pathevaluator.dao;

import com.ibm.fhir.model.resource.Bundle;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class StockProvider extends BaseProvider {

	private StockDao stockDao;

	public List<Bundle> getStocksAgainstServicePointId(String servicePointId) {
		return stockDao.findInventoryInAServicePoint(servicePointId);
	}
}
