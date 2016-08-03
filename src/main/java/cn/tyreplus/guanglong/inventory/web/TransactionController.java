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
import java.util.ArrayList;
import java.util.Date;
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

import cn.tyreplus.guanglong.inventory.entity.Item;
import cn.tyreplus.guanglong.inventory.entity.Transaction;
import cn.tyreplus.guanglong.inventory.service.TransactionService;
import cn.tyreplus.guanglong.inventory.web.form.AdjustForm;
import cn.tyreplus.guanglong.inventory.web.form.ItemForm;
import cn.tyreplus.guanglong.inventory.web.form.OrderForm;
import cn.tyreplus.guanglong.inventory.web.json.DataTable;
import cn.tyreplus.guanglong.inventory.web.json.TxJson;
import cn.tyreplus.guanglong.web.util.PaginationUtil;

@Controller
@RequestMapping("/tx")
public class TransactionController {

	static Logger logger = LoggerFactory.getLogger(TransactionController.class);
	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private TransactionService txService;

	@RequestMapping("/plain")
	@Transactional(readOnly = true)
	public String plainPage(Model model) {
		model.addAttribute("layout_content", "tx/plain");
		return "layout/general";
	}
	
	/**
	 * For frame only 
	 * @param model
	 * @return
	 */
	@RequestMapping("/")
	@Transactional(readOnly = true)
	public String home(Model model) {
		model.addAttribute("layout_content", "tx/home");
		return "layout/general";
	}
	
	/**
	 * for ajax result
	 * @param req
	 * @return
	 */
	@RequestMapping("/list")
	@Transactional(readOnly = true)
	public @ResponseBody DataTable list(HttpServletRequest req) {

		 PaginationUtil paginationUtil = PaginationUtil.getInstance(req);
		 Page<Transaction> orders = this.txService.find(
				 paginationUtil.getSearchValue(),
				 paginationUtil.getSearchMap(),
				 paginationUtil.getPageable());
		
		 DataTable response = new DataTable();
		 response.setDraw(paginationUtil.getDraw());
		 response.setRecordsTotal(txService.getTotalRecords());
		 response.setRecordsFiltered(txService.getTotalFiltered(
				 paginationUtil.getSearchValue(),
				 paginationUtil.getSearchMap(),
				 paginationUtil.getPageable()
				 ));
		 List<TxJson> data = new LinkedList<TxJson>();
		 for (Transaction tx : orders) {
			 TxJson json = new TxJson() ;
			 json.setConsumer(tx.getConsumer());
			 json.setCreatedOn(df.format(tx.getCreatedOn()));
			 json.setId(tx.getId());
			 json.setItem(tx.getItem().getName());
			 json.setNumber(tx.getNumber());
			 json.setPrice(tx.getPrice());
			 json.setSupplier(tx.getSupplier());
			 json.setWarehouse(tx.getWarehouse());
			 data.add(json);
		 }
		 response.setData(data);
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/add")
	public String addForm(Model model, OrderForm orderForm) {
		model.addAttribute("layout_content", "tx/add");
		return "layout/general";
	}

	@RequestMapping(value = "/add", params = { "addItem" })
	public String addRow(final OrderForm orderForm, final BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("layout_content", "tx/add");
			return "layout/general";
		}
		orderForm.addItem();
		model.addAttribute("layout_content", "tx/add");
		return "layout/general";
	}

	@RequestMapping(value = "/add", params = { "removeItem" })
	public String removeRow(Model model, final OrderForm orderForm, final BindingResult bindingResult,
			final HttpServletRequest req) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("layout_content", "tx/add");
			return "layout/general";
		}
		final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
		orderForm.removeItem(rowId);
		model.addAttribute("layout_content", "tx/add");
		return "layout/general";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public String addSubmit(OrderForm orderForm, BindingResult bindingResult, Model model) {

		df.setLenient(false);

		if (bindingResult.hasErrors()) {
			model.addAttribute("layout_content", "tx/add");
			return "layout/general";
		}
		List<Transaction> orders = new ArrayList<Transaction>(); 

		for (ItemForm itemF : orderForm.getItems()) {
			if (itemF.getItem().equals(""))
				continue;
			
			System.out.println("Create one item");
			Transaction tx = new Transaction();
			try {
				tx.setCreatedOn(df.parse(orderForm.getDate()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tx.setConsumer(orderForm.getConsumer());
			tx.setSupplier(orderForm.getSupplier());
			tx.setWarehouse(orderForm.getWarehouse());
			tx.setRemark(orderForm.getRemark());
			tx.setItem((new Item()).setName(itemF.getItem()));
			tx.setPrice(itemF.getPrice());
			tx.setNumber(itemF.getNumber());
			orders.add(tx);
		}
		txService.updateMany(orders);

		return "redirect:/tx/";

	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/adjust")
	public String adjust(Model model, AdjustForm adjustForm) {
		model.addAttribute("layout_content", "tx/adjust");
		return "layout/general";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/adjust")
	public String adjustSubmit(Model model, AdjustForm adjustForm) {
		Date date = null;
		try {
			date = df.parse(adjustForm.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Transaction> manyFrom = new ArrayList<Transaction>(); 
		List<Transaction> manyTo = new ArrayList<Transaction>(); 
		String prefix = adjustForm.getFrom() + "调" + adjustForm.getTo() + " ";
		
		for (ItemForm itemF : adjustForm.getItems()) {
			Transaction from = new Transaction();
			Transaction to = new Transaction();
			from.setCreatedOn(date);
			from.setConsumer(adjustForm.getTo());
			from.setRemark(prefix + adjustForm.getRemark());
			from.setItem((new Item()).setName(itemF.getItem()));
			from.setNumber(itemF.getNumber()*-1);		
			from.setSupplier("N/A");
			from.setWarehouse(adjustForm.getFrom());
			from.setPrice(0);
			manyFrom.add(from);
			
			to.setCreatedOn(date);
			to.setSupplier(adjustForm.getFrom());
			to.setConsumer("N/A");
			to.setRemark(prefix + adjustForm.getRemark());
			to.setWarehouse(adjustForm.getTo());
			to.setItem((new Item()).setName(itemF.getItem()));
			to.setNumber(itemF.getNumber());
			to.setPrice(0);
			manyTo.add(to);
		}
		logger.info("number of items in record: " + manyFrom.size() );
		txService.adjustMany(manyFrom, manyTo);
		return "redirect:/tx/";
	}
	@RequestMapping(value = "/adjust", params = { "addItem" })
	public String addAdjustRow(final AdjustForm adjustForm, final BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("layout_content", "tx/adjust");
			return "layout/general";
		}
		adjustForm.addItem();
		model.addAttribute("layout_content", "tx/adjust");
		return "layout/general";
	}

	@RequestMapping(value = "/adjust", params = { "removeItem" })
	public String removeAdjustRow(Model model, final AdjustForm adjustForm, final BindingResult bindingResult,
			final HttpServletRequest req) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("layout_content", "tx/adjust");
			return "layout/general";
		}
		final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
		adjustForm.removeItem(rowId);
		model.addAttribute("layout_content", "tx/adjust");
		return "layout/general";
	}
}