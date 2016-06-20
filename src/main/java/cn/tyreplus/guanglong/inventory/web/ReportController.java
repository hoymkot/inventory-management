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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.descriptor.java.CalendarDateTypeDescriptor;
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

import cn.tyreplus.guanglong.inventory.service.TransactionService;
import cn.tyreplus.guanglong.inventory.web.form.SalesForm;



@Controller
@RequestMapping("/report")
public class ReportController {

	static Logger logger = LoggerFactory.getLogger(ReportController.class);
	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private TransactionService txService;

	@RequestMapping(method = RequestMethod.GET, value = "/sales")
	@Transactional(readOnly = true)
	public String sales(Model model ,SalesForm form) {
		Date from = new Date();
		Date to = new Date();
		if ( null != form.getFrom() && null != form.getTo()){ 
			try {
				from = df.parse(form.getFrom());
				to = df.parse(form.getTo());
			} catch (ParseException e1) {
			    Calendar calendar = Calendar.getInstance();
			    int year = calendar.get(Calendar.YEAR);
			    int month = calendar.get(Calendar.MONTH);
			    // Do you really want 0-based months, like Java has? Consider month - 1.
			    calendar.set(year, month, 1, 0, 0, 0);
			    calendar.clear(Calendar.MILLISECOND);
			    from = calendar.getTime();
	
			    // Get to the last millisecond in the month
			    calendar.add(Calendar.MONTH, 1);
			    calendar.add(Calendar.MILLISECOND, -1);
			    to = calendar.getTime();
				
			}
		}
		
		List<Map<String, String>> table = new LinkedList<Map<String, String>>();	
		table = txService.salesReport("", from, to);
		model.addAttribute("sales_list", table );
		model.addAttribute("layout_content", "report/sales");
		return "layout/general";
	}
	
	@RequestMapping("/")
	@Transactional(readOnly = true)
	public String list(Model model) {
		model.addAttribute("layout_content", "report/list");
		return "layout/general";
	}
	
}