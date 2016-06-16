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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.tyreplus.guanglong.inventory.entity.Item;
import cn.tyreplus.guanglong.inventory.service.repository.ItemRepository;



@Component("itemService")
@Transactional
class ItemServiceImpl implements ItemService {

	static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	private final ItemRepository itemRepository;


	@Autowired
	public ItemServiceImpl(ItemRepository itemRepository) {
		this.itemRepository= itemRepository;
	}

	@Override
	public Page<Item> find(String search_value, Pageable pageable) {

		if (!StringUtils.hasLength(search_value)) {
			logger.info("find all : start: " + pageable.getPageNumber() + " size: " + pageable.getPageSize());
			return this.itemRepository.findAll(pageable);
		}
		System.out.println("I am here querying");

//		return this.itemRepository.findByNameContainingIgnoringCase(search_value, pageable);
		return this.itemRepository.findAll(pageable);
	}

	@Override
	public Item getItem(Long id) {
		return this.itemRepository.findOne(id);
	}
	
}
