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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tyreplus.guanglong.inventory.entity.Supplier;

public interface SupplierService {
	

	void delete(String supplier);
	void add(Supplier supplier);

	Page<Supplier> find(String search_value, Pageable pageable);
	Long countRecords();
	Long countFiltered(String search_value);
	
	
}
