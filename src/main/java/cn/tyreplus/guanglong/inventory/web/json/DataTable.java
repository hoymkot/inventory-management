package cn.tyreplus.guanglong.inventory.web.json;

import java.util.LinkedList;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataTable {
	
	static Logger logger = LoggerFactory.getLogger(DataTable.class);

	Integer draw;
	Integer recordsTotal ; 
	Integer recordsFiltered; 
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public Iterable getData() {
		return data;
	}
	public void setData(Iterable data) {
		this.data = data;
	}
	Iterable data = new LinkedList();
	

}
