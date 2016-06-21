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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.tyreplus.guanglong.inventory.service.repository.InventoryRepository;

@Component("txService")
@Transactional
public class InventoryServiceImpl implements InventoryService{
	
	static Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
	private final InventoryRepository repo;


	@Autowired
	public InventoryServiceImpl(InventoryRepository repo) {
		this.repo= repo;
	}
	
	
	/**
	 * error reporting inventory already recorded.
	 * validation
	 * @param lastMonth
	 * @param thisMonth
	 * @return
	 */
	@Override
	public List<String[]> previewEndOfMonthInventory(String lastMonth, String thisMonth){
		List<String> reports = this.availableInventoryReport() ;
		if (reports.contains(thisMonth)) {
			logger.warn("inventory report for " + thisMonth + " already exists");
			return new LinkedList<String[]>();
		}
		return repo.previewEndOfMonthInventory(lastMonth, thisMonth);
		
	}
	/**
	 * no op on error
	 */
	@Override
	public void saveEndOfMonthInventory(String lastMonth, String thisMonth){
		List<String> reports = this.availableInventoryReport() ;
		if (reports.contains(thisMonth)) {
			logger.warn("inventory report for " + thisMonth + " already exists");
		}
		repo.saveEndOfMonthInventory(lastMonth, thisMonth);
		
	}
	@Override
	public List<String> availableInventoryReport() {
		return repo.availableInventoryReports();
	}
	
	
}
