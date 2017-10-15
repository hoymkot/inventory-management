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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tyreplus.guanglong.inventory.entity.Transaction;

public interface TransactionService {
	


	void updateMany(List<Transaction> manyOrder);
	void adjustMany(List<Transaction> manyFrom, List<Transaction> manyTo);
	public List<Map<String, String>> salesReport(String item, Date from, Date to) ;
	public List<Map<String, String>> xinchengReport(String item, Date from, Date to) ;
	public List<Map<String, String>> purchaseReport(String item, Date from, Date to) ;

	Page<Transaction> find(String searchValue, Map<String, String> searchMap, Pageable pageable);
	Long getTotalRecords();
	Long getTotalFiltered(String searchValue, Map<String, String> searchMap, Pageable pageable);
	Transaction getDetail(Long id);
	void delete(Long id);
	void save(Transaction tx);
	
}
