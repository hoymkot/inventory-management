/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.tyreplus.guanglong.inventory.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.tyreplus.guanglong.inventory.entity.Inventory;
import cn.tyreplus.guanglong.inventory.service.repository.InventoryRepository;

@Component("inventoryService")
@Transactional
public class InventoryServiceImpl implements InventoryService{
	
	static Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
	private final InventoryRepository repo;

	@Autowired
	public InventoryServiceImpl(InventoryRepository repo) {
		this.repo = repo;
	}

	
	/**
	 * no op on error
	 */
	@Override
	public void saveEndOfMonthInventory(String lastMonth, String thisMonth){
		List<String> reports = this.availableInventoryReport() ;
		if (reports.contains(thisMonth)) {
			logger.warn("inventory report for " + thisMonth + " already exists");
		} else {
			logger.info("copy last month inventory " + lastMonth );
			repo.copyLastMonthInventory(lastMonth, thisMonth);
			logger.info("apply update to inventory for this month ");
			repo.applyDiffToInventory(lastMonth, thisMonth);
			logger.info("add new item to inventory for this month ");
			repo.addNewlyAddedItemToInventory(lastMonth, thisMonth);
		}
		
	}
	@Override
	public List<String> availableInventoryReport() {
		return repo.availableInventoryReports();
	}
	@Override
	public List<String> availableWarehouse() {
		return repo.availableWarehouse();
	}

	@Override
	public List<String[]> viewReport(String period) {
		return repo.findByPeriod(period);
	}
	
	
	/**
	 * TODO: may try example or criteria api 
	 */
	@Override
	public void deleteReport(String period) {
		logger.info("delete inventory record for period " + period);
		repo.deleteByPeriod(period);
	}


	@Override
	public List<Map<String, String>> untouchedReport(String from, String to) {
		List<Map<String, String>> table = new LinkedList<Map<String, String>>();
		logger.info("date range " + from.toString() + ": " + to.toString());
		List<Object[]> list = repo.untouchedReport(from, to);
		for (Object[] obj : list) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("name", obj[0].toString());
			row.put("total", obj[1].toString());
			table.add(row);
		}
		return table;
	}
	
}
