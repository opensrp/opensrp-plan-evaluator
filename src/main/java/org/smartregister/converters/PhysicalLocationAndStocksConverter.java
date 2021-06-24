package org.smartregister.converters;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.type.code.BundleType;
import org.smartregister.domain.PhysicalLocationAndStocks;
import org.smartregister.domain.Stock;
import org.smartregister.domain.StockAndProductDetails;

import java.util.ArrayList;
import java.util.List;

public class PhysicalLocationAndStocksConverter {

	public static Bundle convertLocationAndStocksToBundleResource(PhysicalLocationAndStocks physicalLocationAndStocks) {
		Location location = LocationConverter.convertPhysicalLocationToLocationResource(physicalLocationAndStocks);
		Bundle.Builder bundleBuilder = Bundle.builder();
		bundleBuilder.id(physicalLocationAndStocks.getId());

		List<Bundle.Entry> entries = new ArrayList<>();
		entries.add(Bundle.Entry.builder().resource(location).build());
		for (Stock stock : physicalLocationAndStocks.getStocks()) {
			Bundle.Entry entry = StockConverter
					.createBundleEntryFromStockAndProduct(new StockAndProductDetails(stock, null));
			entries.add(entry);
		}
		bundleBuilder.entry(entries);

		return bundleBuilder.type(BundleType.COLLECTION).build();
	}

}
