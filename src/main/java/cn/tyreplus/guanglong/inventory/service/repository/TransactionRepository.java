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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tyreplus.guanglong.inventory.entity.Transaction;


public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

	@Query("from Transaction tx where tx.item.name like %?1% ")
	List<Transaction> search(String itemName);

//	@Query("select tx.item.name as name, sum(tx.number) as sum from Transaction tx where tx.item.name like %?1% and tx.createdOn >= ?2 and tx.createdOn < ?3 and tx.number < 0 group by tx.item order by name asc")
	@Query(nativeQuery=true, value="Select item, sum(number), sum(number * price) from transaction where item like %?1% and created_on >= ?2 and created_on <= ?3 and number < 0 group by item order by item asc")
	List<Object[]> salesReport(String itemName, Date from, Date to);
}
