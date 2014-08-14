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
	private String args1;
	private String args2;

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

	public String getArgs1() {
		return args1;
	}

	public void setArgs1(String args1) {
		this.args1 = args1;
	}

	public String getArgs2() {
		return args2;
	}

	public void setArgs2(String args2) {
		this.args2 = args2;
	}

}
