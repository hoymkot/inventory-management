package cn.tyreplus.guanglong.inventory.exception;


public class DataLogicException extends RuntimeException {

	public DataLogicException(String msg) {
		super(msg);
	}

	public DataLogicException(Exception e) {
		super(e);
	}


}