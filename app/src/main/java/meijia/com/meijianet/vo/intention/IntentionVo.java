package meijia.com.meijianet.vo.intention;

import java.util.List;

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
         * createTime : 2018-04-28 17:02:41
         * house : {"acreage":3,"address":"666","hall":1,"id":19,"name":"天天小区","piclogo":"http://mjkf.oss-cn-beijing.aliyuncs.com/mjw-images/upload/201804/15nkvv.jpg","room":2,"storey":1,"sumfloor":5,"title":"天天小区大大大","toilet":2,"totalprice":2}
         * ordernum : 44d8eb19ec544620180428170241
         * pay : 0
         */

        private String createTime;
        private HouseBo house;
        private String ordernum;
        private int pay;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public HouseBo getHouse() {
            return house;
        }

        public void setHouse(HouseBo house) {
            this.house = house;
        }

        public String getOrdernum() {
            return ordernum;
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

        public static class HouseBo {
            /**
             * acreage : 3.0
             * address : 666
             * hall : 1
             * id : 19
             * name : 天天小区
             * piclogo : http://mjkf.oss-cn-beijing.aliyuncs.com/mjw-images/upload/201804/15nkvv.jpg
             * room : 2
             * storey : 1
             * sumfloor : 5
             * title : 天天小区大大大
             * toilet : 2
             * totalprice : 2.0
             */

            private double acreage;
            private String address;
            private int hall;
            private int id;
            private String name;
            private String piclogo;
            private int room;
            private int storey;
            private int sumfloor;
            private String title;
            private int toilet;
            private double totalprice;

            public double getAcreage() {
                return acreage;
            }

            public void setAcreage(double acreage) {
                this.acreage = acreage;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getHall() {
                return hall;
            }

            public void setHall(int hall) {
                this.hall = hall;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPiclogo() {
                return piclogo;
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

            public int getStorey() {
                return storey;
            }

            public void setStorey(int storey) {
                this.storey = storey;
            }

            public int getSumfloor() {
                return sumfloor;
            }

            public void setSumfloor(int sumfloor) {
                this.sumfloor = sumfloor;
            }

            public String getTitle() {
                return title;
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

            public double getTotalprice() {
                return totalprice;
            }

            public void setTotalprice(double totalprice) {
                this.totalprice = totalprice;
            }
        }
    }

