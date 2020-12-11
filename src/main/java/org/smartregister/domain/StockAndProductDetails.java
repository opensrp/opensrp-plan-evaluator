package org.smartregister.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockAndProductDetails {

	private Stock stock;
	private ProductCatalogue productCatalogue;
}
