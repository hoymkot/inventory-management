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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.tyreplus.guanglong.inventory.entity.Item;
import cn.tyreplus.guanglong.inventory.exception.DataLogicException;
import cn.tyreplus.guanglong.inventory.service.repository.ItemRepository;
import cn.tyreplus.guanglong.web.util.PaginationUtil;



@Component("itemService")
@Transactional
class ItemServiceImpl implements ItemService {

	static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	private final ItemRepository repo;


	@Autowired
	public ItemServiceImpl(ItemRepository repo) {
		this.repo= repo;
	}

	@Override
	public Page<Item> find(String search_value, Pageable pageable) {
		if (!StringUtils.hasLength(search_value)) {
			logger.info("find all : start: " + pageable.getPageNumber() + " size: " + pageable.getPageSize());
			return this.repo.findAll(pageable);
		}
		return this.repo.findAll(new ItemSpecification(search_value), pageable);
	}
	
	class ItemSpecification implements Specification {
		String search_value; 
		ItemSpecification(String search_value){
			this.search_value = search_value;
		}
		@Override
		public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
			if (!StringUtils.hasLength(search_value)) {
				return cb.conjunction();
			}
			return cb.like(root.get("name"), "%"+search_value+"%");
		}
	}
	
	@Override
	public Long countFiltered(String search_value) {
		if (!StringUtils.hasLength(search_value)) {
			return this.repo.count();
		}
		return this.repo.count(new ItemSpecification(search_value));
	}
	
	@Override
	public Long countRecords() {
		return this.repo.count();
	}

	@Override
	public void delete(String item) {
		logger.info("find " + item);
		Item i = repo.findOne(item);
		if (i != null){
			logger.info("delete " + i.getName());
			repo.delete(i);
		}
	}

	@Override
	public void add(Item item) {
		if (null == repo.findOne(item.getName())){
			logger.info("item " + item.getName() + " saved ");
			repo.save(item);
		} else {
			logger.info("item " + item.getName() + " already exists");
		}
			
	}

	@Override
	public Item getItem(String name) {
		Page<Item> itemlist = this.find(name, PaginationUtil.getOneItemPagable());
		if (itemlist.getSize() == 0) {
			return null;
		}
		return itemlist.iterator().next();
	}
	
}
