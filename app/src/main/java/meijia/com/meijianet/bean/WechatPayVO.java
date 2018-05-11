package meijia.com.meijianet.bean;
public class WechatPayVO {

    /**
     * appid : wx8c19cdf052d03742
     * noncestr : de9240f5c623bf031dcf0fca9770db44
     * partnerid : 1501810531
     * prepayid : wx03143401486374be494e43e91943314656
     * sign : 5C3354C65EF3D96769804D46CF01A05F
     * timestamp : 1525329124
     */

    private String appid;
    private String noncestr;
    private String partnerid;
    private String prepayid;
    private String sign;
    private int timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
