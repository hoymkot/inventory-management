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

import java.util.List;

public interface InventoryService {
	
	/**
	 * error reporting inventory already recorded.
	 * @param lastMonth
	 * @param thisMonth
	 * @return
	 */
	void saveEndOfMonthInventory(String lastMonth, String thisMonth);
	
	List<String> availableInventoryReport();
	List<String> availableWarehouse();

	void deleteReport(String period);

	List<String[]> viewReport(String period);
	

	
}
