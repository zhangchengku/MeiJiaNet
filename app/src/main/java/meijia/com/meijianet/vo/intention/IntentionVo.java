package meijia.com.meijianet.vo.intention;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 * Create：2018/3/22
 * 意向房源
 */
public class IntentionVo {
    /**
     * "id": 1,
     "title": "大房子",
     "name": "大房子111",
     "piclogo": "http://www.sda.com./sdj/155.jpg",
     "pay": 1,
     "createTime": 2018-02-24 18:00:52
     */

    private int pay;
    private String createTime;
    private String ordernum;
    private IntentionInfo house;

    public String getOrdernum() {
        return ordernum == null ? "" : ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public IntentionInfo getHouse() {
        return house;
    }

    public void setHouse(IntentionInfo house) {
        this.house = house;
    }
}
