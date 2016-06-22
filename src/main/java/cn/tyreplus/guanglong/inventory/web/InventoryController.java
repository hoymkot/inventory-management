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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.tyreplus.guanglong.inventory.entity.Inventory;
import cn.tyreplus.guanglong.inventory.service.InventoryService;


@Controller
@RequestMapping("/inventory")
public class InventoryController {

	static Logger logger = LoggerFactory.getLogger(InventoryController.class);
	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


	@Autowired
	private InventoryService service;

	@RequestMapping(method = RequestMethod.GET, value="/view" , params = { "generate" })
	public String inventoryGeneration(Model model) {

		List<String> available = this.service.availableInventoryReport();
		if (available.size() == 0) return "redirect:/inventory/view/";
		
		String period = available.iterator().next();
		Date recent = new Date(); Date now = recent;
		try {
			recent = df.parse(period);
		} catch (ParseException e) {
			e.printStackTrace();
			return "redirect:/inventory/view/";
		}
		Calendar c = Calendar.getInstance();
		c.set(recent.getYear() + 1900, recent.getMonth() + 1 ,0,0,0);
		c.clear(Calendar.MILLISECOND);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);;
		String new_period = (new Integer(c.get(Calendar.YEAR))).toString() +
				"-" + (new Integer(c.get(Calendar.MONTH) + 1)).toString() + 
				"-" + (new Integer(c.get(Calendar.DAY_OF_MONTH))).toString();
		service.saveEndOfMonthInventory(period, new_period);
		
		return "redirect:/inventory/view/";
	}
	@RequestMapping(method = RequestMethod.GET, value="/view")
	public String viewReport(String period, Model model) {
		List<String> available = this.service.availableInventoryReport();
		if (period == null || period.equals("")){
			period = available.iterator().next();
		}
		List<Inventory> report = this.service.viewReport(period);
		
		model.addAttribute("current_period", period);
		model.addAttribute("report", report);
		model.addAttribute("available", available);
		model.addAttribute("layout_content", "inventory/view");
		return "layout/general";
	}
	@RequestMapping(method = RequestMethod.GET, value="/view", params = { "delete" })
	public String deleteReport(String period, Model model) {
		if(period != null && !period.equals("2016-05-31"))
			this.service.deleteReport(period);
		return "redirect:/inventory/view";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/")
	@Transactional(readOnly = true)
	public String list(Model model) {
		model.addAttribute("layout_content", "inventory/list");
		return "layout/general";
	}
}
