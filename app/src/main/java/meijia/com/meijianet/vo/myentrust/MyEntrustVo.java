package meijia.com.meijianet.vo.myentrust;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/21
 */
public class MyEntrustVo {
    /**
     * {
     "acreage": null,
     "address": "余杭良渚",
     "building": null,
     "consignationDate": null,
     "consignationStatus": 0,
     "contactname": "都大局观吧",
     "contactphone": "18795369362",
     "employee": {
     "code": "369",
     "id": 14,
     "introduce": "136",
     "nickname": "美女客服",
     "phone": "13958623652",
     "professional": 5
     },
     "hall": null,
     "name": "花园村社区",
     "number": null,
     "piclogo": null,
     "price": 100,
     "room": null,
     "title": null,
     "toilet": null
     */

    private String acreage;
    private String address;
    private int building;//冻
    private String consignationDate;
    private int consignationStatus;
    private String contactname;
    private String contactphone;
    private MyEntrustInfo employee;
    private int hall;//厅
    private String name;
    private String number;//房源编号
    private String piclogo;
    private int room;
    private String title;
    private int toilet;
    private String price;

    public String getAcreage() {
        return acreage == null ? "" : acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public String getConsignationDate() {
        return consignationDate == null ? "" : consignationDate;
    }

    public void setConsignationDate(String consignationDate) {
        this.consignationDate = consignationDate;
    }

    public int getConsignationStatus() {
        return consignationStatus;
    }

    public void setConsignationStatus(int consignationStatus) {
        this.consignationStatus = consignationStatus;
    }

    public String getContactname() {
        return contactname == null ? "" : contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactphone() {
        return contactphone == null ? "" : contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public MyEntrustInfo getEmployee() {
        return employee;
    }

    public void setEmployee(MyEntrustInfo employee) {
        this.employee = employee;
    }

    public int getHall() {
        return hall;
    }

    public void setHall(int hall) {
        this.hall = hall;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number == null ? "" : number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPiclogo() {
        return piclogo == null ? "" : piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getToilet() {
        return toilet;
    }

    public void setToilet(int toilet) {
        this.toilet = toilet;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
