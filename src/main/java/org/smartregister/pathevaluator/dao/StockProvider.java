package org.smartregister.pathevaluator.dao;

import com.ibm.fhir.model.resource.Basic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartregister.domain.Stock;

import java.util.List;

@AllArgsConstructor
@Getter
public class StockProvider extends BaseProvider {

	private StockDao stockDao;

	public List<Basic> getStocksAgainstServicePointId(String servicePointId) {
		return stockDao.findInventoryInAServicePoint(servicePointId);
	}
}
