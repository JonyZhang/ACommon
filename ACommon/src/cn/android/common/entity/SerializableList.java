package cn.android.common.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author JonyZhang E-mail:xxpgd2@gmail.com
 * @version Create time：2014-8-3
 * @description Used for Intent
 */
public class SerializableList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Map<String, String>> list;

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
