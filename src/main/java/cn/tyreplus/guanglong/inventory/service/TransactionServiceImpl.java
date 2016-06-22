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

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.tyreplus.guanglong.inventory.entity.Transaction;
import cn.tyreplus.guanglong.inventory.service.repository.TransactionRepository;

@Component("txService")
@Transactional
class TransactionServiceImpl implements TransactionService {

	static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
	private final TransactionRepository txRepo;

	@Autowired
	public TransactionServiceImpl(TransactionRepository txRepo) {
		this.txRepo= txRepo;
	}

	@Override
	public Page<Transaction> find(String searchValue, Pageable pageable) {
		logger.info("find all : start: " + pageable.getPageNumber() + " size: " + pageable.getPageSize());
		if(searchValue.equals("")){
			Sort sort = pageable.getSort().and(new Sort(new Sort.Order(Direction.fromString("desc"), "id")));
			Pageable p = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
			return this.txRepo.findAll(p);
		}
		else {
			List<Transaction> result = txRepo.search(searchValue);
			result.sort(new Comparator<Transaction>(){

				@Override
				public int compare(Transaction arg0, Transaction arg1) {
					if (arg0.getCreatedOn().compareTo(arg1.getCreatedOn()) == 0) {
						if ( arg0.getId() < arg1.getId()) return 1;
						else return -1;
					} else return arg0.getCreatedOn().compareTo(arg1.getCreatedOn()) * -1;
				}
				
			});
			return new PageImpl<Transaction>(result);
		}
	}

	@Override
	public void updateMany(List<Transaction> manyOrder) {
		txRepo.save(manyOrder);
	}

	@Override
	public void adjustMany(List<Transaction> manyFrom, List<Transaction> manyTo) {
		if (manyFrom.size() != manyTo.size()) {
			logger.warn("the number of adjustment items doesn't match.");
			return;
		}
		for(int i = 0; i < manyFrom.size() ; i++ ) {
			logger.info("saving  " + manyFrom.get(i).getItem().getName() );
			logger.info("saving  " + manyTo.get(i).getItem().getName() );
			txRepo.save(manyFrom.get(i));
			txRepo.save(manyTo.get(i));
		}
	}

	@Override
	public List<Map<String, String>> salesReport(String item, Date from, Date to) {
		List<Map<String, String>> table = new LinkedList<Map<String, String>>();	
		List<Object[]> list = txRepo.salesReport(item, from, to);
		for ( Object[] obj : list) {
			Map<String, String> row= new HashMap<String, String>();
			row.put("name", obj[0].toString());
			row.put("total", obj[1].toString());
			row.put("sales", obj[2].toString());
			table.add(row);
		}
		return table;
	}
	
	@Override
	public List<Map<String, String>> purchaseReport(String item, Date from, Date to) {
		List<Map<String, String>> table = new LinkedList<Map<String, String>>();	
		List<Object[]> list = txRepo.purchaseReport(item, from, to);
		for ( Object[] obj : list) {
			Map<String, String> row= new HashMap<String, String>();
			row.put("name", obj[0].toString());
			row.put("total", obj[1].toString());
			row.put("sales", obj[2].toString());
			table.add(row);
		}
		return table;
	}
	
}
