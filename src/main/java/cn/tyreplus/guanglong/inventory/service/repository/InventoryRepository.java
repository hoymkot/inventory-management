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

package cn.tyreplus.guanglong.inventory.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import cn.tyreplus.guanglong.inventory.entity.Inventory;


public interface InventoryRepository extends Repository<Inventory, Integer> {
	
	@Query(nativeQuery=true, value="select tx.item, tx.warehouse, inven.number as old_value,  sum(tx.number) as net, (sum(tx.number) + inven.number) as total, ?2 from inventory inven inner join transaction tx on inven.item = tx.item and tx.warehouse = inven.warehouse where created_on > ?1 and created_on <= ?2 and period = ?1 group by tx.item order by tx.warehouse, tx.item")
	List<String[]> previewEndOfMonthInventory(String lastMonth, String thisMonth);

	@Query(nativeQuery=true, value="INSERT INTO `inventory`( `number`, `warehouse`, `item`, `period`) select sum(tx.number) + inven.number,  tx.warehouse, tx.item, ?2 from inventory inven inner join transaction tx on inven.item = tx.item and tx.warehouse = inven.warehouse where created_on > ?1 and created_on <= ?2 and period = ?! group by tx.item") 
	void saveEndOfMonthInventory(String lastMonth, String thisMonth);
	
	@Query(nativeQuery=true, value="select period from inventory group by period")
	List<String> availableInventoryReports();
	
}
	
	