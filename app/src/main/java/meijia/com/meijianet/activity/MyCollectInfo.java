package meijia.com.meijianet.activity;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 * Create：2018/3/15
 * "acreage": 100,
"address": "浙江省衢州市衢江区",
"application": 4,
"area": "",
"balcony": 1,
"browseCount": 23,
"building": null,
"city": "",
"collectCount": 2,
"decoration": 2,
"downpayment": 30,
"hall": 1,
"id": 5,
"intentionCount": 0,
"intentionPrice": 1000,
"kitchen": 1,
"name": "测试",
"numb": null,
"orientation": "北",
"piclogo": "mjw-images/upload/201803/867JcrA.png",
"price": 1,
"province": "",
"region": "衢江",
"room": 1,
"storey": 6,
"sumfloor": 12,
"title": "测试一下",
"toilet": 1,
"totalprice": 100,
"unit": null,
"uptime": 2018-01-03 18:00:00,
 */
public class MyCollectInfo {
    private long id;//
    private String unit;//
    private String region;//
    private String number;//
    private String intentionPrice;//
    private String collectCount;//
    private String browseCount;//
    private String building;//
    private String title;//
    private String application;//
    private String province;//
    private String city;//
    private String area;//
    private String address;//
    private String name;//
    private String totalprice;//
    private String downpayment;//
    private String price;//
    private String acreage;//
    private String piclogo;//
    private String room;//
    private String hall;//
    private String toilet;//
    private String kitchen;//
    private String balcony;//
    private String orientation;//
    private String storey;//
    private String sumfloor;//
    private int decoration;//装修情况(1毛坯,2简单装修,3 中档装修,4,精装修,5豪华装修//
    private int intentionCount;//
    private String uptime;//
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOnShow() {
        return onShow;
    }

    public void setOnShow(int onShow) {
        this.onShow = onShow;
    }

    private int onShow;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRegion() {
        return region == null ? "" : region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNumber() {
        return number == null ? "" : number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIntentionPrice() {
        return intentionPrice == null ? "" : intentionPrice;
    }

    public void setIntentionPrice(String intentionPrice) {
        this.intentionPrice = intentionPrice;
    }

    public String getCollectCount() {
        return collectCount == null ? "" : collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public String getBrowseCount() {
        return browseCount == null ? "" : browseCount;
    }

    public void setBrowseCount(String browseCount) {
        this.browseCount = browseCount;
    }

    public String getBuilding() {
        return building == null ? "" : building;
    }

    public void setBuilding(String building) {
        this.building = building;
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
