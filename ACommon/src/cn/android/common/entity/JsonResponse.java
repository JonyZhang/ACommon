package cn.android.common.entity;

/**
 * @author JonyZhang E-mail:xxpgd2@gmail.com
 * @version Create time：2014-6-10
 * @description
 */
public class JsonResponse {

	private String status;
	private String errorCode;
	private String msg;
	private Object obj;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
