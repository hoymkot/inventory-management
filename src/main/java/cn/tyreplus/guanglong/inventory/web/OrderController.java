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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tyreplus.guanglong.inventory.entity.Order;
import cn.tyreplus.guanglong.inventory.service.OrderService;
import cn.tyreplus.guanglong.inventory.web.form.OrderForm;
import cn.tyreplus.guanglong.web.util.DataTable;
import cn.tyreplus.guanglong.web.util.PaginationUtil;


@Controller
@RequestMapping("/order")
public class OrderController {

	static Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@RequestMapping("/")
	@Transactional(readOnly = true)
	public String home(Model model) {
		model.addAttribute("layout_content", "order/home");
		return "layout/general";
	}
	
	@RequestMapping("/list")
	@Transactional(readOnly = true)
	public @ResponseBody DataTable list(HttpServletRequest req) {

		PaginationUtil paginationUtil = PaginationUtil.getInstance(req);
		Page<Order> orders = this.orderService.find(paginationUtil.getSearchValue(), paginationUtil.getPageable());

		DataTable response = new DataTable();
		response.setDraw(paginationUtil.getDraw());
		response.setRecordsTotal(0);
		response.setRecordsFiltered(0);
		List<Order> data = new LinkedList<Order>();
		for (Order d : orders) {
			data.add(d);
		}
		response.setData(data);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/add")
	public String addForm(Model model, OrderForm orderForm) {
		model.addAttribute("layout_content", "order/add");
		return "layout/general";
	}
	
	
	@RequestMapping(value="/add", params={"addItem"})
	public String addRow(final OrderForm orderForm, final BindingResult bindingResult, Model model) {
		
        if (bindingResult.hasErrors()) {
        	model.addAttribute("layout_content", "order/add");
        	return "layout/general";
        }
		orderForm.addItem();
	    model.addAttribute("layout_content", "order/add");
		return "layout/general";
	}

	@RequestMapping(value="/add", params={"removeItem"})
	public String removeRow( Model model,
			final OrderForm orderForm, final BindingResult bindingResult, 
	        final HttpServletRequest req) {
        if (bindingResult.hasErrors()) {
        	model.addAttribute("layout_content", "order/add");
        	return "layout/general";
        }
	    final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
	    orderForm.removeItem(rowId);
	    model.addAttribute("layout_content", "order/add");
		return "layout/general";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	@Transactional
	public String addSubmit(OrderForm orderForm, BindingResult bindingResult, Model model ) {
		

        if (bindingResult.hasErrors()) {
        	model.addAttribute("layout_content", "order/add");
        	return "layout/general";
        }
        return "redirect:/order/";
	}

}