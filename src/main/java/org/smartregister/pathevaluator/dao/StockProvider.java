package org.smartregister.pathevaluator.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartregister.domain.Stock;

import java.util.List;

@AllArgsConstructor
@Getter
public class StockProvider extends BaseProvider {

	private StockDao stockDao;

	public List<Stock> getStocksAgainstServicePointId(String servicePointId) {
		return stockDao.findInventoryInAServicePoint(servicePointId);
	}
}
