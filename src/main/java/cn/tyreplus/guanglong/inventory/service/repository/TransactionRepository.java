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

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tyreplus.guanglong.inventory.entity.Transaction;


public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> , JpaSpecificationExecutor {

	@Query("from Transaction tx where tx.item.name like %?1% or tx.warehouse like %?1%")
	List<Transaction> search(String itemName);

//	@Query("select tx.item.name as name, sum(tx.number) as sum from Transaction tx where tx.item.name like %?1% and tx.createdOn >= ?2 and tx.createdOn < ?3 and tx.number < 0 group by tx.item order by name asc")
	@Query(nativeQuery=true, value="Select item, sum(number), sum(number * price) from transaction where item like %?1% and created_on >= ?2 and created_on <= ?3 and consumer not in ('金鸡', '吉大') and number < 0 group by item order by item asc")
	List<Object[]> salesReport(String itemName, Date from, Date to);

	@Query(nativeQuery=true, value="Select item, sum(number), sum(number * price) from transaction where item like %?1% and created_on >= ?2 and created_on <= ?3 and supplier not in ('金鸡', '吉大') and number > 0 group by item order by item asc")
	List<Object[]> purchaseReport(String itemName, Date from, Date to);

	@Query(nativeQuery=true, value="Select item, sum(number), sum(number * price) from transaction where item like %?1% and created_on >= ?2 and created_on <= ?3 and supplier = '信诚' and number > 0 group by item order by item asc")
	List<Object[]> xinchengReport(String itemName, Date from, Date to);
	
	@Query(nativeQuery=true, value="select sum(tx.number) + inven.number as total, sum(tx.number) as net, inven.number as old_value, tx.warehouse, tx.item, ?2 from inventory inven inner join transaction tx on inven.item = tx.item and tx.warehouse = inven.warehouse where created_on > ?1 and created_on <= ?2 and period = ?1 group by tx.item")
	List<String[]> previewEndOfMonthInventory(String lastMonth, String thisMonth);
	

	@Query(nativeQuery=true, value="INSERT INTO `inventory`( `number`, `warehouse`, `item`, `period`) select sum(tx.number) + inven.number,  tx.warehouse, tx.item, ?2 from inventory inven inner join transaction tx on inven.item = tx.item and tx.warehouse = inven.warehouse where created_on > ?1 and created_on <= ?2 and period = ?! group by tx.item") 
	void saveEndOfMonthInventory(String lastMonth, String thisMonth);

}
	
	
