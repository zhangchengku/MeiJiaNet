package meijia.com.meijianet.bean;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/7/14.
 * <p>
 * 分享的bean
 */

public class ShareBO implements Serializable {


    /**
     * content : 让子弹飞
     * id : 58
     * picture :
     * shareurl : null
     * title : null
     */

    private String content;
    private String id;
    private String picture;
    private String shareurl;
    private String title;

    public ShareBO() {
    }

    public ShareBO(String content, String picture, String shareurl, String title) {
        setContent(content);
        setId(id);
        setPicture(picture);
        setShareurl(shareurl);
        setTitle(title);
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
