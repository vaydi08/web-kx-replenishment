package com.sol.kx.replenishment.dao;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynmicDataSource extends AbstractRoutingDataSource{

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceHolder.getDataSource();
	}

}
