package meijia.com.meijianet.activity;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/15
 *         "code": "FY0001",
"title": "西区高档小区 新湖玫瑰园 多层住宅",
"application": "复式",
"province": 浙江省,
"city": 衢州市,
"area": 柯城区,
"address": "浙江省衢州市柯城区碧水蓝天小区",
"name": "碧水蓝天小区",
"totalprice": 100,
"downpayment": 30,
"price": 1,
"acreage": 100,
"piclogo": "http://mjwpc.oss-cn-hangzhou.aliyuncs.com/mjw-images/upload/201803/59dOOy.jpg",
"room": 1,
"hall": 1,
"toilet": 1,
"kitchen": 1,
"balcony": 1,
"orientation": "南",
"storey": "6",
"sumfloor": 12,
"decoration": 2,
"browse_count": 100,
"intention_count": 120,
"collect_count": 60,
"uptime": "2018-03-14 11:39:36",
}],
 */
public class NewHouseInfo {
    private long id;
    private String code;//房源编号
    private String title;
    private String application;
    private String province;
    private String city;
    private String area;
    private String address;
    private String name;
    private String totalprice;
    private String downpayment;
    private String price;
    private String acreage;
    private String piclogo;
    private String room;
    private String hall;
    private String toilet;
    private String kitchen;
    private String balcony;
    private String orientation;
    private String storey;
    private String sumfloor;
    private int decoration;//装修情况(1毛坯,2简单装修,3 中档装修,4,精装修,5豪华装修
    private int browse_count;
    private int collect_count;
    private int intentionCount;
    private String uptime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApplication() {
        return application == null ? "" : application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area == null ? "" : area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalprice() {
        return totalprice == null ? "" : totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getDownpayment() {
        return downpayment == null ? "" : downpayment;
    }

    public void setDownpayment(String downpayment) {
        this.downpayment = downpayment;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAcreage() {
        return acreage == null ? "" : acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getPiclogo() {
        return piclogo == null ? "" : piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }

    public String getRoom() {
        return room == null ? "" : room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getHall() {
        return hall == null ? "" : hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getToilet() {
        return toilet == null ? "" : toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
    }

    public String getKitchen() {
        return kitchen == null ? "" : kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getBalcony() {
        return balcony == null ? "" : balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    public String getOrientation() {
        return orientation == null ? "" : orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getStorey() {
        return storey == null ? "" : storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }

    public String getSumfloor() {
        return sumfloor == null ? "" : sumfloor;
    }

    public void setSumfloor(String sumfloor) {
        this.sumfloor = sumfloor;
    }

    public int getDecoration() {
        return decoration;
    }

    public void setDecoration(int decoration) {
        this.decoration = decoration;
    }

    public int getBrowse_count() {
        return browse_count;
    }

    public void setBrowse_count(int browse_count) {
        this.browse_count = browse_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public int getIntentionCount() {
        return intentionCount;
    }

    public void setIntentionCount(int intentionCount) {
        this.intentionCount = intentionCount;
    }

    public String getUptime() {
        return uptime == null ? "" : uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}
