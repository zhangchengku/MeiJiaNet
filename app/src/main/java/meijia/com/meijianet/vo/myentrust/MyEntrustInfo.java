package meijia.com.meijianet.vo.myentrust;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/22
 */
public class MyEntrustInfo {
    /**
     * "employee": {
     "code": "369",
     "id": 14,
     "introduce": "136",
     "nickname": "美女客服",
     "phone": "13958623652",
     "professional": 5
     }
     */

    private String code;
    private long id;
    private String introduce;
    private String nickname;
    private String phone;
    private int professional;

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce == null ? "" : introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getProfessional() {
        return professional;
    }

    public void setProfessional(int professional) {
        this.professional = professional;
    }
}
