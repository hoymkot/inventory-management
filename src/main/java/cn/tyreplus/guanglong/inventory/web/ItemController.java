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



import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tyreplus.guanglong.inventory.entity.Item;
import cn.tyreplus.guanglong.inventory.entity.Transaction;
import cn.tyreplus.guanglong.inventory.service.ItemService;
import cn.tyreplus.guanglong.inventory.web.form.ItemDefForm;
import cn.tyreplus.guanglong.inventory.web.json.DataTable;
import cn.tyreplus.guanglong.inventory.web.json.ItemListJson;
import cn.tyreplus.guanglong.web.util.PaginationUtil;


@Controller
@RequestMapping("/item")
public class ItemController {

	static Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;
	

	@RequestMapping("/")
	@Transactional(readOnly = true)
	public String home(Model model) {
		model.addAttribute("layout_content", "item/home");
		return "layout/general";
	}
	@RequestMapping("/list")
	@Transactional(readOnly = true)
	public @ResponseBody DataTable list(HttpServletRequest req) {
		
		 PaginationUtil paginationUtil = PaginationUtil.getInstance(req);
		 Page<Item> items = this.itemService.find(
				 paginationUtil.getSearchValue()
				 , paginationUtil.getPageable());
		
		 DataTable response = new DataTable();
		 response.setDraw(paginationUtil.getDraw());
		 response.setRecordsTotal(0);
		 response.setRecordsFiltered(0);

		List<Item> data = new LinkedList<Item>();
		for (Item i : items) {
			data.add(i);
		}
		response.setData(data);
		return response;
	}
	
	@RequestMapping("/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<String> plainTxt(@RequestParam(value = "term") String item) {
		Page<Item> items = this.itemService.find(item,new PageRequest(0, 10000));
		StringBuilder sb = new StringBuilder();
		List<String> data = new LinkedList<String>();
		for (Item i : items) {
			data.add(i.getName());
		}
		return data;
	}

	
	@RequestMapping("/delete")
	@Transactional()
	public String delete(@RequestParam(value = "item") String item) {
		
		this.itemService.delete(item);
		return "redirect:/item/";
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/add")
	@Transactional()
	public String addForm(ItemDefForm itemDefForm, Model model) {
		model.addAttribute("layout_content", "item/add");
		return "layout/general";
	}	
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	@Transactional()
	public String addSubmit(ItemDefForm itemDefForm, Model model) {
		Item i = new Item() ;
		i.setName(itemDefForm.getItem());
		i.setDescription(itemDefForm.getDescription());
		i.setCreatedOn(new Date());
		this.itemService.add(i);
		return "redirect:/item/";
	}
}
