package org.smartregister.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhysicalLocationAndStocks extends PhysicalLocation {

	private static final long serialVersionUID = 1L;

	private List<Stock> stocks;

}
