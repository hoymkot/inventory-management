package cn.tyreplus.guanglong.inventory.web.form;

import java.math.BigDecimal;

public class ItemForm {

	private String item;

	Integer number;

	Integer price;
	
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getPrice() {
		return price;
	}

	public ItemForm setPrice(Integer price) {
		this.price = price;
		return this;
	}
}
