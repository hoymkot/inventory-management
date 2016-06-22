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

package cn.tyreplus.guanglong.inventory.web;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.tyreplus.guanglong.inventory.service.InventoryService;


@Controller
@RequestMapping("/inventory")
public class InventoryController {

	static Logger logger = LoggerFactory.getLogger(InventoryController.class);

	@Autowired
	private InventoryService inventoryService;

	@RequestMapping(method = RequestMethod.GET, value="/generate")
	public String inventoryGeneration(Model model) {
		List<String[]> report = new LinkedList<String[]>(); 
		inventoryService.saveEndOfMonthInventory("2016-05-31", "2016-06-30");
		
		model.addAttribute("report", report);
		model.addAttribute("layout_content", "inventory/view");
		return "layout/general";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/")
	@Transactional(readOnly = true)
	public String list(Model model) {
		model.addAttribute("layout_content", "inventory/list");
		return "layout/general";
	}
}
