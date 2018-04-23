package meijia.com.meijianet.bean;

import java.util.List;
import java.util.ArrayList;
/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/23
 */
public class HouseDetailVo {
    private int status;
    private int collectStatus;
    private String address;
    private float acreage;
    private String addtax;
    private String agreementtax;
    private String application;
    private String area;
    private String balcony;
    private List<String> bannerLi;
    private String browseCount;
    private String building;
    private float buildingtax;
    private String carport;
    private String city;
    private String code;
    private String collectCount;
    private int decoration;
    private float downpayment;
    private int flatcost;
    private String hall;
    private int hasLoft;
    private int hasStoreRoom;
    private String houseLayout;
    private String houseMeno;
    private String houseSize;
    private String intentionCount;
    private float intentionPrice;
    private String kitchen;
    private String landlordname;
    private String landlordphone;
    private float landtax;
    private String limitAge;
    private String loft;
    private String mapurl;
    private float maxloan;
    private String name;
    private String numb;
    private String orientation;
    private String parking;
    private String piclogo;
    private String preDowntime;
    private float price;
    private String province;
    private String region;
    private String requirements;
    private String room;
    private int stick;
    private String storeroom;
    private String storey;
    private String sumfloor;
    private String tax;
    private String title;
    private String toilet;
    private String totalprice;
    private String unit;
    private String uptime;
    private String vrurl;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getAcreage() {
        return acreage;
    }

    public void setAcreage(float acreage) {
        this.acreage = acreage;
    }

    public String getAddtax() {
        return addtax == null ? "" : addtax;
    }

    public void setAddtax(String addtax) {
        this.addtax = addtax;
    }

    public String getAgreementtax() {
        return agreementtax == null ? "" : agreementtax;
    }

    public void setAgreementtax(String agreementtax) {
        this.agreementtax = agreementtax;
    }

    public String getApplication() {
        return application == null ? "" : application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getArea() {
        return area == null ? "" : area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBalcony() {
        return balcony == null ? "" : balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    public List<String> getBannerLi() {
        if (bannerLi == null) {
            return new ArrayList<>();
        }
        return bannerLi;
    }

    public void setBannerLi(List<String> bannerLi) {
        this.bannerLi = bannerLi;
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

    public float getBuildingtax() {
        return buildingtax;
    }

    public void setBuildingtax(float buildingtax) {
        this.buildingtax = buildingtax;
    }

    public String getCarport() {
        return carport == null ? "" : carport;
    }

    public void setCarport(String carport) {
        this.carport = carport;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCollectCount() {
        return collectCount == null ? "" : collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public int getDecoration() {
        return decoration;
    }

    public void setDecoration(int decoration) {
        this.decoration = decoration;
    }

    public float getDownpayment() {
        return downpayment;
    }

    public void setDownpayment(float downpayment) {
        this.downpayment = downpayment;
    }

    public int getFlatcost() {
        return flatcost;
    }

    public void setFlatcost(int flatcost) {
        this.flatcost = flatcost;
    }

    public String getHall() {
        return hall == null ? "" : hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public int getHasLoft() {
        return hasLoft;
    }

    public void setHasLoft(int hasLoft) {
        this.hasLoft = hasLoft;
    }

    public int getHasStoreRoom() {
        return hasStoreRoom;
    }

    public void setHasStoreRoom(int hasStoreRoom) {
        this.hasStoreRoom = hasStoreRoom;
    }

    public String getHouseLayout() {
        return houseLayout == null ? "" : houseLayout;
    }

    public void setHouseLayout(String houseLayout) {
        this.houseLayout = houseLayout;
    }

    public String getHouseMeno() {
        return houseMeno == null ? "" : houseMeno;
    }

    public void setHouseMeno(String houseMeno) {
        this.houseMeno = houseMeno;
    }

    public String getHouseSize() {
        return houseSize == null ? "" : houseSize;
    }

    public void setHouseSize(String houseSize) {
        this.houseSize = houseSize;
    }

    public String getIntentionCount() {
        return intentionCount == null ? "" : intentionCount;
    }

    public void setIntentionCount(String intentionCount) {
        this.intentionCount = intentionCount;
    }

    public float getIntentionPrice() {
        return intentionPrice;
    }

    public void setIntentionPrice(float intentionPrice) {
        this.intentionPrice = intentionPrice;
    }

    public String getKitchen() {
        return kitchen == null ? "" : kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getLandlordname() {
        return landlordname == null ? "" : landlordname;
    }

    public void setLandlordname(String landlordname) {
        this.landlordname = landlordname;
    }

    public String getLandlordphone() {
        return landlordphone == null ? "" : landlordphone;
    }

    public void setLandlordphone(String landlordphone) {
        this.landlordphone = landlordphone;
    }

    public float getLandtax() {
        return landtax;
    }

    public void setLandtax(float landtax) {
        this.landtax = landtax;
    }

    public String getLimitAge() {
        return limitAge == null ? "" : limitAge;
    }

    public void setLimitAge(String limitAge) {
        this.limitAge = limitAge;
    }

    public String getLoft() {
        return loft == null ? "" : loft;
    }

    public void setLoft(String loft) {
        this.loft = loft;
    }

    public String getMapurl() {
        return mapurl == null ? "" : mapurl;
    }

    public void setMapurl(String mapurl) {
        this.mapurl = mapurl;
    }

    public float getMaxloan() {
        return maxloan;
    }

    public void setMaxloan(float maxloan) {
        this.maxloan = maxloan;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumb() {
        return numb == null ? "" : numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public String getOrientation() {
        return orientation == null ? "" : orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getParking() {
        return parking == null ? "" : parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getPiclogo() {
        return piclogo == null ? "" : piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }

    public String getPreDowntime() {
        return preDowntime == null ? "" : preDowntime;
    }

    public void setPreDowntime(String preDowntime) {
        this.preDowntime = preDowntime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegion() {
        return region == null ? "" : region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRequirements() {
        return requirements == null ? "" : requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getRoom() {
        return room == null ? "" : room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getStick() {
        return stick;
    }

    public void setStick(int stick) {
        this.stick = stick;
    }

    public String getStoreroom() {
        return storeroom == null ? "" : storeroom;
    }

    public void setStoreroom(String storeroom) {
        this.storeroom = storeroom;
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

    public String getTax() {
        return tax == null ? "" : tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToilet() {
        return toilet == null ? "" : toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
    }

    public String getTotalprice() {
        return totalprice == null ? "" : totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUptime() {
        return uptime == null ? "" : uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getVrurl() {
        return vrurl == null ? "" : vrurl;
    }

    public void setVrurl(String vrurl) {
        this.vrurl = vrurl;
    }
}
