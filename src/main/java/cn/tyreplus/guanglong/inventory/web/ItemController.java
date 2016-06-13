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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tyreplus.guanglong.inventory.entity.Item;
import cn.tyreplus.guanglong.inventory.service.ItemService;
import cn.tyreplus.guanglong.web.util.DataTable;

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
		Integer draw = Integer.valueOf(req.getParameter("draw"));
		Integer start = Integer.valueOf(req.getParameter("start"));
		Integer length = Integer.valueOf(req.getParameter("length"));
		String searchValue = req.getParameter("search[value]");
		String direction = req.getParameter("order[0][dir]");
		String orderByColIdx = req.getParameter("order[0][column]");
		String orderBy = req.getParameter("columns[" + orderByColIdx + "][data]");
		DataTable response = new DataTable();
		response.setDraw(draw);
		logger.info("start: " + start + " length: " + length + " search: " + searchValue);

		Map<String, String[]> pMap = req.getParameterMap();
		logger.info("full map: " + pMap.toString());

		logger.info("order by: " + orderBy);
		logger.info("direction: " + direction);

		response.setRecordsTotal(0);
		response.setRecordsFiltered(0);
		Pageable page = new PageRequest(start / length, length, Direction.fromString(direction), orderBy);
		Page<Item> items = this.itemService.findItems(searchValue, page);
		List<Item> data = new LinkedList<Item>();
		for (Item r : items) {
			data.add(r);
		}
		response.setData(data);
		return response;
	}


}
