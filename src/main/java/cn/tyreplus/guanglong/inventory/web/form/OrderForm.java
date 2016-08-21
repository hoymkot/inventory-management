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
package cn.tyreplus.guanglong.inventory.web.form;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderForm implements Serializable {

	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final long serialVersionUID = 1L;

	private Long id; 
	
	private String date=""; 

	private String supplier;

	private String consumer;

	private String warehouse;

	private String remark = "";

	
	private List<ItemForm> items = new ArrayList<ItemForm>();;
	
	public OrderForm(){
		this.setDate(df.format(new Date()));
	}
	
	public void addItem() {
		this.getItems().add(new ItemForm().setPrice(0));
	}
	
	public void removeItem(Integer idx) {
		this.getItems().remove(idx.intValue());
	}
	public List<ItemForm> getItems() {
		return items;
	}

	public void setItems(List<ItemForm> items) {
		this.items = items;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setId(Long id) {
		this.id = id;
		
	}
	public Long getId(){
		return id;
	}

	

}