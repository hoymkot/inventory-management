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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
	
	static int[] LAST_DAY_OF_MONTH = {31,28,31,30,31,30,31,31,30,31,30,31};

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
		c.add(Calendar.MONTH, 1);// -1 
		Integer day = LAST_DAY_OF_MONTH[c.get(Calendar.MONTH) + 1];
		Integer month = c.get(Calendar.MONTH) + 1;
		Integer year = c.get(Calendar.YEAR);
		if (year % 4 == 0 && month == 2) {
			day = 29;
		}
		String new_period = year.toString() +
				"-" + month.toString() + 
				"-" + day.toString();
		logger.info("create report for new period: " + new_period );
		service.saveEndOfMonthInventory(period, new_period);
		
		return "redirect:/inventory/view/";
	}
	@RequestMapping(method = RequestMethod.GET, value="/view")
	public String viewReport(String period, Model model) {
		List<String> available = this.service.availableInventoryReport();
		if (period == null || period.equals("")){
			period = available.iterator().next();
		}
		List<String[]> report = this.service.viewReport(period);
		
		HashMap<String, HashMap<String, String>> table = new LinkedHashMap<String, HashMap<String, String>>();
		for (Object[] in : report) {
			String i = in[0].toString();
			if ( table.get(i) == null){
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put(in[1].toString(), (new Long(in[2].toString())).toString());
				table.put(i, entry);
			} else {
				table.get(i).put(in[1].toString(), (new Long(in[2].toString())).toString());
			}
		}
		List<String> houseList = this.service.availableWarehouse();
		Set<String> itemList = table.keySet();
		model.addAttribute("current_period", period);
		model.addAttribute("table", table);
		model.addAttribute("keyList", itemList);
		model.addAttribute("houseList", houseList);
		model.addAttribute("available", available);
		model.addAttribute("layout_content", "inventory/view");
		return "layout/general";
	}
	@RequestMapping(method = RequestMethod.GET, value="/view", params = { "delete" })
	public String deleteReport(String period, Model model) {
		if(period != null && !period.equals("2016-06-30"))
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
