package cn.tyreplus.guanglong.inventory.web.form;

import java.math.BigDecimal;

public class ItemForm {

	private String item;

	Integer number;

	BigDecimal price;
	
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
