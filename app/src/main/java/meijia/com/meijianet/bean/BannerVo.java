package meijia.com.meijianet.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class BannerVo {
        /**
         * address : http://www.baidu.com
         * btime : 2018-04-20 11:01:06
         * etime : 2018-05-10 11:01:08
         * id : 8
         * orders : 4
         * pictureroot : http://mjkf.oss-cn-beijing.aliyuncs.com/mjw-images/upload/201804/566Hpbk.png
         * remarks : dsadf
         * showplat : 2
         * status : 1
         */

        private String address;
        private String btime;
        private String etime;
        private int id;
        private int orders;
        private String pictureroot;
        private String remarks;
        private int showplat;
        private int status;
    private Integer needLogin;

    public Integer getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(Integer needLogin) {
        this.needLogin = needLogin;
    }

    public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBtime() {
            return btime;
        }

        public void setBtime(String btime) {
            this.btime = btime;
        }

        public String getEtime() {
            return etime;
        }

        public void setEtime(String etime) {
            this.etime = etime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }

        public String getPictureroot() {
            return pictureroot;
        }

        public void setPictureroot(String pictureroot) {
            this.pictureroot = pictureroot;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getShowplat() {
            return showplat;
        }

        public void setShowplat(int showplat) {
            this.showplat = showplat;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

