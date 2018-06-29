package meijia.com.meijianet.fragment;

/**
 * Created by Administrator on 2018/5/8.
 */
public class VideoVo {
    private  String pic;
    private  String       url;
    private  int recentlySellStatus;
    private  int gameStatus;

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getRecentlySellStatus() {
        return recentlySellStatus;
    }

    public void setRecentlySellStatus(int recentlySellStatus) {
        this.recentlySellStatus = recentlySellStatus;
    }


    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
