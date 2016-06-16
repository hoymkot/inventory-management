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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.tyreplus.guanglong.inventory.entity.Item;
import cn.tyreplus.guanglong.inventory.entity.Transaction;

@Component("txService")
@Transactional
class TransactionServiceImpl implements TransactionService {

	static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
	private final TransactionRepository txRepo;
	private final ItemRepository itemRepo;

	@Autowired
	public TransactionServiceImpl(TransactionRepository txRepo, ItemRepository itemRepo) {
		this.txRepo= txRepo;
		this.itemRepo = itemRepo;
	}

	@Override
	public Page<Transaction> find(String searchValue, Pageable pageable) {
		logger.info("find all : start: " + pageable.getPageNumber() + " size: " + pageable.getPageSize());
		
		return this.txRepo.findAll(pageable);
	}
	@Override
	public Transaction update(Transaction order) {
		Item i = new Item(); 
		i.setName("205/55R16 Xm2");
		i.setDescription("blah");
		i.setCreatedOn(new Date());
		itemRepo.save(i);
		order.setItem(i);
		return txRepo.save(order);
	}
	
}
