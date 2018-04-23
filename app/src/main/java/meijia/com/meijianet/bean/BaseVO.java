package meijia.com.meijianet.bean;

public class BaseVO {
	/**成功:success  错误：error 异常：exception*/
//	protected long timestamp;
	/**0代表成功，其他值待定*/
	protected int status;
	/**错误提示*/
	protected String message;
	/**业务数据*/
	protected String data;

	private String code;//成功失败的字符串

	public BaseVO() {

	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
