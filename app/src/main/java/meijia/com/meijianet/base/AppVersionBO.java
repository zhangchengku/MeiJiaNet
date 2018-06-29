package meijia.com.meijianet.base;

/**
 * Created by Administrator on 2018/5/7.
 */
public class AppVersionBO {


        /**
         * android : {"versionCode":2,"versionName":"1.1.1","updateMessage":"1、\n2、\n","minVersionCode":1,"minVersionName":"1.0","url":"http://mjwtest.happydoit.com/webapp/app/app-release.apk"}
         * apple : 1.1.1
         */

        private AndroidBo android;
        private String apple;

        public AndroidBo getAndroid() {
            return android;
        }

        public void setAndroid(AndroidBo android) {
            this.android = android;
        }

        public String getApple() {
            return apple;
        }

        public void setApple(String apple) {
            this.apple = apple;
        }

        public static class AndroidBo {
            /**
             * versionCode : 2
             * versionName : 1.1.1
             * updateMessage : 1、
             2、

             * minVersionCode : 1
             * minVersionName : 1.0
             * url : http://mjwtest.happydoit.com/webapp/app/app-release.apk
             */

            private int versionCode;
            private String versionName;
            private String updateMessage;
            private int minVersionCode;
            private String minVersionName;
            private String url;

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public String getUpdateMessage() {
                return updateMessage;
            }

            public void setUpdateMessage(String updateMessage) {
                this.updateMessage = updateMessage;
            }

            public int getMinVersionCode() {
                return minVersionCode;
            }

            public void setMinVersionCode(int minVersionCode) {
                this.minVersionCode = minVersionCode;
            }

            public String getMinVersionName() {
                return minVersionName;
            }

            public void setMinVersionName(String minVersionName) {
                this.minVersionName = minVersionName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

