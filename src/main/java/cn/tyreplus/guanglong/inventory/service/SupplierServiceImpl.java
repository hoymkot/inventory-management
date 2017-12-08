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

import cn.tyreplus.guanglong.inventory.entity.Supplier;
import cn.tyreplus.guanglong.inventory.service.repository.SupplierRepository;



@Component("supplierService")
@Transactional
class SupplierServiceImpl implements SupplierService {

	static Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);
	private final SupplierRepository repo;


	@Autowired
	public SupplierServiceImpl(SupplierRepository repo) {
		this.repo= repo;
	}

	@Override
	public Page<Supplier> find(String search_value, Pageable pageable) {
		if (!StringUtils.hasLength(search_value)) {
			logger.info("find all : start: " + pageable.getPageNumber() + " size: " + pageable.getPageSize());
			return this.repo.findAll(pageable);
		}
		return this.repo.findAll(new SupplierSpecification(search_value), pageable);
	}
	
	class SupplierSpecification implements Specification {
		String search_value; 
		SupplierSpecification(String search_value){
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
		return this.repo.count(new SupplierSpecification(search_value));
	}
	
	@Override
	public Long countRecords() {
		return this.repo.count();
	}

	@Override
	public void delete(String supplier) {
		logger.info("find " + supplier);
		Supplier i = repo.findOne(supplier);
		if (i != null){
			logger.info("delete " + i.getName());
			repo.delete(i);
		}
	}

	@Override
	public void add(Supplier supplier) {
		if (null == repo.findOne(supplier.getName())){
			logger.info("supplier " + supplier.getName() + " saved ");
			repo.save(supplier);
		} else {
			logger.info("supplier " + supplier.getName() + " already exists");
		}
			
	}
	
}
