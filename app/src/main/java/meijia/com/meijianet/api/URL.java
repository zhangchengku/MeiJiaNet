package meijia.com.meijianet.api;

/**
 * Created by Administrator on 2017/9/2.
 */

public interface URL {
    String NEW_HOUSE = "/api/house/getNewHouse";
    String SEARCH_HOUSE = "/api/house/searchHouse";
    String TRANSACTIONRECORD = "/api/house/searchRecentlySell";
    String MY_ENTRUST ="/api/member/myConsignation";//我的委托
    String MY_ITENTION = "/api/member/myItention";//意向房源
    String LOGIN = "/api/member/memberLogin";
    String CODE = "/api/member/sendMessage";
    String REGIST = "/api/member/registMember";
    String LOGIN_OUT = "/api/member/logout";
    String FORGEST_PSW = "/api/member/findAndModify";

    String HOUSE_DETAIL = "/api/house/getHouseDetails";
    String VIDEO = "/api/homePage/video";
    String SHOUYELUNBOTU = "/api/carousel/getcarousels";
    String COLLECT_HOUSE = "/api/collect/collectHouse";
    String DELETE_COLLECT = "/api/collect/deleteCollect";
    String MY_COLLECT = "/api/collect/myCollect";//我的收藏
    String MY_BROWSE = "/api/member/browseRecord";
    String DELETE_BROWSE = "/api/member/deleteBrowseRecord";
    String UPDATE_MSG = "/api/member/modifyPersonalData";
    String WX_PAY = "/api/order/wxpay";
    String ALI_PAY = "/api/order/alipay";
    String ORDER_PAY = "/api/submit/order";
    String UPDATA_PSW = "/api/member/modifyByPassword";
    String MY_STATUS = "/api/member/myState";
    String PULL_HOUSE = "/api/member/publicHouse";
    String RECENTLY_SELL = "/api/house/recentlySell";
    String LOGIN_QW = "/api/member/loginByOther";
    String CHECK_PHONE = "/api/member/checkPhoneState";
    String BIND_REGIST = "/api/member/registMemberByPhoneRegister";
    String BING_NOT_REGIST = "/api/member/registMemberByPhoneNoRegister";
    String AUTO_LOGIN = "/api/member/autoLoginByUuid";
    String CANCLE_ORDER = "/api/cancel/order";
    String TUI_KUAN = "/api/order/apprefund";


}
