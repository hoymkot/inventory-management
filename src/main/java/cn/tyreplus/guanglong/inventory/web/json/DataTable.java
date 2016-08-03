package cn.tyreplus.guanglong.inventory.web.json;

import java.util.LinkedList;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataTable {
	
	static Logger logger = LoggerFactory.getLogger(DataTable.class);

	Integer draw;
	Long recordsTotal ; 
	Long recordsFiltered; 
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Long recordsFiltered) {
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
