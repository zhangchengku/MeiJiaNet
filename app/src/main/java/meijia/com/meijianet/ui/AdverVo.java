package meijia.com.meijianet.ui;

/**
 * Created by Administrator on 2018/5/14.
 */
public class AdverVo {

    /**
     * advertisement : http://mjkf.oss-cn-beijing.aliyuncs.com/mjw-images/upload/201805/148wMPR.png
     * redirectUrl : www.baidu.com
     * startStatus : 0
     */

    private String advertisement;
    private String redirectUrl;
    private int startStatus;
    private int advSeconds;

    public int getAdvSeconds() {
        return advSeconds;
    }

    public void setAdvSeconds(int advSeconds) {
        this.advSeconds = advSeconds;
    }

    public String getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(String advertisement) {
        this.advertisement = advertisement;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public int getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(int startStatus) {
        this.startStatus = startStatus;
    }
}
