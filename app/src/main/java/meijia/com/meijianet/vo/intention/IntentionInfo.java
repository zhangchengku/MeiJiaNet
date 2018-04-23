package meijia.com.meijianet.vo.intention;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/30
 */
public class IntentionInfo {
    private long id;
    private String title;
    private String name;
    private String piclogo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPiclogo() {
        return piclogo == null ? "" : piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }
}
