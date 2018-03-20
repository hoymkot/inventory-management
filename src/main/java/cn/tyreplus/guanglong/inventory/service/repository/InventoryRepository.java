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

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tyreplus.guanglong.inventory.entity.Inventory;



/**
 * Initial Inventory Creation Query
 * SELECT id, '2016-6-30', item, sum(number),warehouse, now() FROM `transaction` WHERE created_on = '2016-06-30 00:00:00' group by item, warehouse 
 * @author Administrator
 *
 */
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
	
	@Query(nativeQuery=true, value="select tx.item, tx.warehouse, inven.number as old_value,  sum(tx.number) as net, (sum(tx.number) + inven.number) as total, ?2 from inventory inven outer join transaction tx on inven.item = tx.item and tx.warehouse = inven.warehouse where created_on > ?1 and created_on <= ?2 and period = ?1 group by tx.item order by tx.warehouse, tx.item")
	List<String[]> previewEndOfMonthInventory(String lastMonth, String thisMonth);
	
	/**
	 * * copy old inventory to new record 
	 * * iterate through tx table and update the table record accordingly 
	 * * 
	 * @param lastMonth
	 * @param thisMonth
	 */
	@Query(nativeQuery=true, value="INSERT INTO `inventory`( `number`, `warehouse`, `item`, `period`) select sum(tx.number) + inven.number,  tx.warehouse, tx.item, ?2 from inventory inven inner join transaction tx on inven.item = tx.item and tx.warehouse = inven.warehouse where created_on > ?1 and created_on <= ?2 and period = ?! group by tx.item") 
	@Transactional
	void saveEndOfMonthInventory(String lastMonth, String thisMonth);
	
	@Query(nativeQuery=true, value="select period from inventory group by period order by period desc")
	List<String> availableInventoryReports();
	
	@Query(nativeQuery=true, value="select warehouse from inventory group by warehouse order by warehouse ASC")
	List<String> availableWarehouse();

	/**
	 * INSERT INTO `inventory`( `number`, `warehouse`, `item`, `period`, last_modified_on) select number, warehouse, item, '2016-06-30', now() from inventory where period = '2016-05-31' 
	 * @param lastMonth
	 * @param thisMonth
	 */
	@Modifying
	@Query(nativeQuery=true, value="INSERT INTO `inventory`( `number`, `warehouse`, `item`, `period`, last_modified_on) select number, warehouse, item, ?2, now() from inventory where period = ?1 ")
	void copyLastMonthInventory(String lastMonth, String thisMonth);

	
	@Modifying
	@Query(nativeQuery=true, value="UPDATE `inventory`i SET i.number = i.number + (select ifnull(sum(tx.number),0) from transaction tx where i.item = tx.item and tx.warehouse = i.warehouse and created_on > ?1 and created_on <= ?2 ), last_modified_on = now() WHERE period = ?2")
	void applyDiffToInventory(String lastMonth, String thisMonth);

	/**
	 * INSERT INTO `inventory`(`period`, `item`, `number`, `warehouse`, `remark`, `last_modified_on`) select '2016-06-30', tx.item, sum(tx.number), tx.warehouse, tx.remark, now() from transaction tx where (tx.item, tx.warehouse) not in ( select i.item, i.warehouse from inventory i where period = '2016-06-30') and created_on > '2016-05-31' and created_on <= '2016-06-30' group by tx.item, tx.warehouse
	 * base query: 
	 * select '2016-06-30', tx.item, sum(tx.number), tx.warehouse, tx.remark, now() from transaction tx where (tx.item, tx.warehouse) not in ( select i.item, i.warehouse from inventory i where period = '2016-06-30') and created_on > '2016-05-31' and created_on <= '2016-06-30' group by tx.item, tx.warehouse  
	 * @param lastMonth
	 * @param thisMonth
	 */
	@Modifying
	@Query(nativeQuery=true, value="INSERT INTO `inventory`(`period`, `item`, `number`, `warehouse`, `remark`, `last_modified_on`) select ?2, tx.item, sum(tx.number), tx.warehouse, tx.remark, now() from transaction tx where (tx.item, tx.warehouse) not in ( select i.item, i.warehouse from inventory i where period = ?2) and created_on > ?1 and created_on <= ?2 group by tx.item, tx.warehouse")
	void addNewlyAddedItemToInventory(String lastMonth, String thisMonth);

	void deleteByPeriod(String period);


	@Query(nativeQuery=true, value=
	"select A.item, "+  
	"(select B.number from inventory B where B.period = ?1 and B.item = A.item and B.warehouse = '吉大') as jidai, "+
	"(select C.number from inventory C where C.period = ?1 and C.item = A.item and C.warehouse = '金鸡') as jinji, "+
	"sum(number) as total "+
	"from inventory A "+
	"where A.period = ?1 "+
	"group by A.item "+
	"having sum(number) <>0; ")

//	@Query(nativeQuery=true, value=
//	"select A.item, 1 as jindai, 1 as jinji," +
//	"sum(number) as total "+
//	"from inventory A "+
//	"where A.period = ?1 "+
//	"group by A.item "+
//	"having sum(number) <>0; ")
	List<String[]> findByPeriod(String period);

	@Query(nativeQuery=true, value="select item , sum(number) as total from inventory where period = ?2 group by item having item not in (select item from transaction where created_on > ?1 and created_on <= ?2 ) and total <> 0;")
	List<Object[]> untouchedReport( String from, String to);
	
}
	
	
