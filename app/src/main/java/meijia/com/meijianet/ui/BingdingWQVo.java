package meijia.com.meijianet.ui;

/**
 * Created by Administrator on 2018/4/27.
 */
public class BingdingWQVo {


    /**
     * code : not_register
     * message : 该手机号未注册
     * data : 13855455444
     * status : 0
     */

    private String code;
    private String message;
    private String data;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
