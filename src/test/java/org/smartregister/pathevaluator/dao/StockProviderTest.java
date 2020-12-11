package org.smartregister.pathevaluator.dao;

import com.ibm.fhir.model.resource.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.pathevaluator.TestData;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockProviderTest {

	@Mock
	private StockDao stockDao;

	private StockProvider stockProvider;

	private Bundle bundle = TestData.createBundle();

	private List<Bundle> expected = Collections.singletonList(bundle);

	@Before
	public void setUp() {
		stockProvider = new StockProvider(stockDao);
	}

	@Test
	public void testGetStocksAgainstServicePointId() {
		when(stockDao.findInventoryInAServicePoint(anyString())).thenReturn(expected);
		assertEquals(expected, stockProvider.getStocksAgainstServicePointId("3734"));
		verify(stockDao).findInventoryInAServicePoint("3734");
	}

}
