package cn.tyreplus.guanglong.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.ResponseBody;

public class PaginationUtil {
	static Logger logger = LoggerFactory.getLogger(PaginationUtil.class);

	static int COLS = 7;
	HttpServletRequest req ;
	
	public PaginationUtil(HttpServletRequest req) {
		this.req = req;
	}

	static public PaginationUtil getInstance(HttpServletRequest req) {
		return new PaginationUtil(req);
	}

	public Pageable getPageable() {
		
		Integer start = Integer.valueOf(req.getParameter("start"));
		Integer length = Integer.valueOf(req.getParameter("length"));
		logger.info("start: " + start + " length: " + length );
		
		String direction = req.getParameter("order[0][dir]");
		String orderByColIdx = req.getParameter("order[0][column]");
		String orderBy = req.getParameter("columns[" + orderByColIdx + "][data]");

		Map<String, String[]> pMap = req.getParameterMap();
		logger.info("full map: " + pMap.toString());

		logger.info("order by: " + orderBy);
		logger.info("direction: " + direction);

		return new PageRequest(start / length, length, Direction.fromString(direction), orderBy);
	}
	
	public Integer getDraw(){
		return Integer.valueOf(req.getParameter("draw")); 
	}
	
	public String getSearchValue(){ 
		String searchValue = req.getParameter("search[value]");
		logger.info(" search: " + searchValue);
		return searchValue;
		
	}
	public Map<String, String> getSearchMap(){
		Map<String, String> map = new HashMap<String, String>();
		for ( Integer i = 0 ; i < COLS ; i++){
			if (! "true".equals(req.getParameter("columns["+i.toString()+"][searchable]"))) continue;	
			String searchValue = req.getParameter("columns["+i.toString()+"][search][value]");	
			String searchName = req.getParameter("columns["+i.toString()+"][data]");
			logger.error("search name: " + searchName);
			logger.error("search value:" + searchValue);
			if (searchName != null && !searchName.equals("") && searchValue != null && ! searchValue.equals("") ){
				logger.error("search name: " + searchName);
				logger.error("search value:" + searchValue);
				map.put(searchName, searchValue);
			}
		}
		return map;
	}

}
