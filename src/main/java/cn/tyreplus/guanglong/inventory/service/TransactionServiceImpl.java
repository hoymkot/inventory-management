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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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
		this.txRepo = txRepo;
	}



	@Override
	public Page<Transaction> find(String searchValue, Map<String, String> searchMap, Pageable pageable) {
		logger.info("find all : start: " + pageable.getPageNumber() + " size: " + pageable.getPageSize());
		Sort sort = pageable.getSort().and(new Sort(new Sort.Order(Direction.fromString("desc"), "id")));
		Pageable p = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return txRepo.findAll(new TransactionSpecification(searchValue, searchMap), p);

	}
		
	@Override
	public Long getTotalFiltered(String searchValue, Map<String, String> searchMap, Pageable pageable) {
		return txRepo.count(new TransactionSpecification(searchValue, searchMap));
		
	}
	@Override
	public Long getTotalRecords() {
		return txRepo.count();		
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
		for (int i = 0; i < manyFrom.size(); i++) {
			logger.info("saving  " + manyFrom.get(i).getItem().getName());
			logger.info("saving  " + manyTo.get(i).getItem().getName());
			txRepo.save(manyFrom.get(i));
			txRepo.save(manyTo.get(i));
		}
	}
	
	@Override
	public Transaction getDetail(Long id) {
		return this.txRepo.findOne(id);
	}

	@Override
	public List<Map<String, String>> salesReport(String item, Date from, Date to) {
		List<Map<String, String>> table = new LinkedList<Map<String, String>>();
		List<Object[]> list = txRepo.salesReport(item, from, to);
		for (Object[] obj : list) {
			Map<String, String> row = new HashMap<String, String>();
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
		for (Object[] obj : list) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("name", obj[0].toString());
			row.put("total", obj[1].toString());
			row.put("sales", obj[2].toString());
			table.add(row);
		}
		return table;
	}

	class TransactionSpecification implements Specification<Transaction>{

		private String searchValue;
		private Map<String, String> searchMap;
		TransactionSpecification(String searchValue, Map<String, String> searchMap){
			this.searchValue = searchValue;
			this.searchMap = searchMap;
		}
		
		@Override
		public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
			Predicate p = builder.conjunction();
			String itemName = searchValue;
			if (searchMap.get("item") != null && !searchMap.get("item").equals("")) {
				itemName = searchMap.get("item");
			}
			if (!"".equals(itemName))
				p = builder.and(p, builder.like(root.get("item").get("name"), "%" + itemName + "%"));
			for ( String key : searchMap.keySet()){
				if (key!= null && !key.equals("") && !key.equals("item") && searchMap.get(key) != null && !searchMap.get(key).equals(""))
					p = builder.and(p, builder.like(root.get(key), "%" + searchMap.get(key) + "%"));
			}
			
			return p;
		}
		
	}
	
	
}
