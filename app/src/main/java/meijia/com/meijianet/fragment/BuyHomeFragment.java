package meijia.com.meijianet.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.CityAdapter;
import meijia.com.meijianet.activity.NewHouseInfo;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.activity.SearchMoreAdapter;
import meijia.com.meijianet.adpter.menu.DropDownMenus;
import meijia.com.meijianet.api.OnItemClickListener;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.ui.HouseDetailActivity;
import meijia.com.meijianet.ui.SearchMoreActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.liushibuju.BaseAdapter;
import meijia.com.srdlibrary.liushibuju.TagLayout;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/4/20.
 */

public class BuyHomeFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, OnItemClickListener {

    private DropDownMenus mDropDownMenu;
    private String headers[] = {"区域", "总价", "厅室", "更多"};
    private List<View> popupViews = new ArrayList<>();

    private String citys[] = {"柯城", "衢江", "巨化", "其它区域"};
    private String ages[] = { "50万以下", "50-80万", "80-100万", "100-120万", "120-150万",
            "150-200万", "200-300万", "300万以上"};
    private String sexs[] = { "一室", "二室", "三室", "四室", "五室", "六室及以上"};
    private String constellations[] = { "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};
    //more
    private String louceng[] = {"1楼", "2楼", "3楼", "4楼", "5楼", "6楼", "7-12楼", "12楼以上"};
    private String chaoxiang[] = {"南", "北", "东", "西", "东南", "东北", "西南", "西北","东西", "南北" };
    private String zhuangxiu[] = {"毛坯", "简单装修", "中档装修", "精装修", "豪华装修"};
    private String paixu[] = { "50以下", "50-70", "70-90", "90-130", "130-150", "150-200", "200-300", "300以上"};
    private String leixing[] = {"单体别墅", "排屋", "多层", "复式", "小高楼", "店面", "写字楼"};

    private int tagPosition = 8;//总价标记
    private int tagPosition2 = 0;//庭室标记
    //more
    private int paixuPosition = 100;
    private int chaoxiangPositon = 100;
    private int leixingPosition = 100;
    private int zhuangxiuPositon = 100;
    private int loucengPosition = 100;
    private TagLayout mTag_paixu;
    private TagLayout mTag_zhuangxiu;
    private TagLayout mTag_chaoxiang;
    private TagLayout mTag_leixing;
    private TagLayout mTag_louceng;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RelativeLayout rlEmpty;
    private RecyclerView rvList;
    private SearchMoreAdapter mAdapter;
    private List<NewHouseInfo> datas = new ArrayList<>();
    private int pageNo = 1;
    public static final int PAGE_SIZE = 10;

    private EditText etSearch;
    private ImageView ivDelete;

    private  boolean isEdixd = false;
    private int quYu = 4;
    private String minPrice = "0";
    private String maxPrice = "0";
    private int room = 0;
    private int hall = 0;
    private int toilet = 0;
    private int morePaixu = 0;
    private int moreChaoxiang = 0;
    private int moreType = 0;
    private int zhuangxiuType = 0;
    private String xiaoquName = "";
    private LinearLayout llParent;
    private String acreageMin ="";
    private String acreageMax="";
    private String chaooo="";
    private int leiii = 0;
    private int zhangggg = 0;
    private String sumfloorMin ="";
    private String sumfloorMax = "";
    private ImageView ivback;
    private int storey=0;
    private String moremim="";
    private String moremam="";
    private String moremixl="";
    private String moremaxl="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search_more,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    @Override
    protected void initView() {
        ivback=(ImageView)view.findViewById(R.id.iv_back);
        ivback.setVisibility(View.GONE);
        llParent=(LinearLayout)view.findViewById(R.id.ll_parent);
        TextView sousuo = (TextView)view. findViewById(R.id.sousuo);
        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String psw = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(psw)){
                    ToastUtil.showShortToast(getActivity(),"小区名称不能为空");
                    return ;
                }
                xiaoquName = psw;
                //隐藏软键盘
                InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()){
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                autoRefresh();
            }
        });
        etSearch = (EditText) view.findViewById(R.id.et_ac_search);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTag_paixu.setItemSelecte(100);
                mTag_zhuangxiu.setItemSelecte(100);
                mTag_chaoxiang.setItemSelecte(100);
                mTag_leixing.setItemSelecte(100);
                mTag_louceng.setItemSelecte(100);
                acreageMax="";
                chaooo="";
                zhangggg=0;
                leiii=0;
                acreageMin="";
                storey=0;
                acreageMax="";
                moremim = "";
                moremam = "";
                moremixl = "";
                moremaxl = "";
            }
        });
        ivDelete = (ImageView) view.findViewById(R.id.iv_ac_search_delete);

        ToolUtil.setInputListener(etSearch, ivDelete);
        mDropDownMenu = (DropDownMenus) view.findViewById(R.id.dropDownMenu);
        View quyuView = LayoutInflater.from(getActivity()).inflate(R.layout.muen_quyu, null);
        TagLayout quyutagLayout = (TagLayout) quyuView.findViewById(R.id.tag_layout);
        TextView quyutvConnmit = (TextView) quyuView.findViewById(R.id.tv_confirm);
        TextView quyutv_buxian = (TextView) quyuView.findViewById(R.id.tv_buxian);
        quyutv_buxian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quyutagLayout.setItemSelecte(4);
                quYu = 4;
                mDropDownMenu.setTabText(quYu == 4 ? headers[0] : citys[quYu]);
                autoRefresh();
                mDropDownMenu.closeMenu();
            }
        });
        quyutvConnmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.setTabText(quYu == 4 ? headers[0] : citys[quYu]);
                autoRefresh();
                mDropDownMenu.closeMenu();
            }
        });
        quyutagLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return citys.length;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView view = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_tag, parent, false);
                view.setText(citys[position]);
                return view;
            }
        });
        quyutagLayout.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                quyutagLayout.setItemSelecte(postion);
                quYu = postion;


            }
        });
        View zongjiaView = LayoutInflater.from(getActivity()).inflate(R.layout.muen_zongjia, null);
        TagLayout tagLayout = (TagLayout) zongjiaView.findViewById(R.id.tag_layout);
        EditText etZuidi = (EditText) zongjiaView.findViewById(R.id.et_zuidi);
        EditText etZuigao = (EditText) zongjiaView.findViewById(R.id.et_zuigao);
        TextView tvConnmit = (TextView) zongjiaView.findViewById(R.id.tv_confirm);
        TextView tv_buxian = (TextView) zongjiaView.findViewById(R.id.tv_buxian);
        tv_buxian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdixd=false;
                tagLayout.setItemSelecte(8);
                tagPosition=8;
                mDropDownMenu.setTabText(tagPosition == 8 ? headers[1] : ages[tagPosition]);
                autoRefresh();
                mDropDownMenu.closeMenu();
            }
        });
        tagLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return ages.length;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView view = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_tag, parent, false);
                view.setText(ages[position]);
                return view;
            }
        });
        tagLayout.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                tagLayout.setItemSelecte(postion);
                tagPosition = postion;
            }
        });

        tvConnmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zuidi = etZuidi.getText().toString().trim();
                String zuigao = etZuigao.getText().toString().trim();
                if (!zuidi.equals("") && !zuigao.equals("")) {
                    if (Float.parseFloat(zuigao) > Float.parseFloat(zuidi)) {
                        isEdixd =true;
                        mDropDownMenu.setTabText(zuidi + "-" + zuigao + "万");
                        etZuidi.setText("");
                        etZuigao.setText("");
                        ToolUtil.closeKeyboard(etZuidi, getActivity());
                        maxPrice = zuigao;
                        minPrice = zuidi;
                    } else {
                        ToastUtil.showShortToast(getActivity(), "最高的价格不能低于最低的价格");
                        return;
                    }
                    tagLayout.setItemSelecte(8);
                    tagPosition=8;
                } else {
                    mDropDownMenu.setTabText(tagPosition == 8 ? headers[1] : ages[tagPosition]);
                }
                autoRefresh();
                mDropDownMenu.closeMenu();

            }
        });

        //init庭室
        View shiView = LayoutInflater.from(getActivity()).inflate(R.layout.muen_shi, null);
        TagLayout tagLayout2 = (TagLayout) shiView.findViewById(R.id.tag_layout);
        EditText etShi = (EditText) shiView.findViewById(R.id.et_shi);
        EditText etTing = (EditText) shiView.findViewById(R.id.et_ting);
        EditText etWei = (EditText) shiView.findViewById(R.id.et_wei);
        TextView tvConnmit2 = (TextView) shiView.findViewById(R.id.tv_confirm);
        TextView tvgengduo = (TextView) shiView.findViewById(R.id.tv_gengduo);
        tvgengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room = 0;
                hall = 0;
                toilet = 0;
                tagLayout2.setItemSelecte(7);
                mDropDownMenu.setTabText(headers[2]);
                autoRefresh();
                mDropDownMenu.closeMenu();
            }
        });
        tagLayout2.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return sexs.length;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView view = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_tag, parent, false);
                view.setText(sexs[position]);
                return view;
            }
        });
        tagLayout2.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                tagLayout2.setItemSelecte(postion);
                tagPosition2 = postion;
                room = postion+1;
                hall = 0;
                toilet = 0;
            }
        });

        tvConnmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shi = etShi.getText().toString().trim();
                String ting = etTing.getText().toString().trim();
                String wei = etWei.getText().toString().trim();
                if (shi.equals("") && ting.equals("") && wei.equals("")) {
                    mDropDownMenu.setTabText(sexs[tagPosition2]);
                } else {
                    if (shi.equals("") || ting.equals("")) {
                        ToastUtil.showShortToast(getActivity(), "室、厅是必填的哦亲");
                        return;
                    } else {
                        if (wei.equals("")) {
                            mDropDownMenu.setTabText(shi + "室" + ting + "厅");
                        } else {
                            mDropDownMenu.setTabText(shi + "室" + ting + "厅" + wei + "卫");
                        }
                        etShi.setText("");
                        etTing.setText("");
                        etWei.setText("");
                        tagLayout2.setItemSelecte(7);
                        room = Integer.parseInt(shi);
                        hall = Integer.parseInt(ting);
                        toilet = Integer.parseInt(wei);
                        ToolUtil.closeKeyboard(etZuidi, getActivity());
                    }

                }
                autoRefresh();
                mDropDownMenu.closeMenu();
            }
        });

        //initMore更多
        View moreView = LayoutInflater.from(getActivity()).inflate(R.layout.muen_more, null);
        EditText moremixm = (EditText) moreView.findViewById(R.id.et_mixm);
        EditText moremaxm = (EditText) moreView.findViewById(R.id.et_maxm);
        EditText moremixlc = (EditText) moreView.findViewById(R.id.mixlc);
        EditText moremaxlc = (EditText) moreView.findViewById(R.id.maxlc);
        mTag_paixu = (TagLayout) moreView.findViewById(R.id.tag_paixu);
        mTag_zhuangxiu = (TagLayout) moreView.findViewById(R.id.tag_zhuangxiu);
        mTag_chaoxiang = (TagLayout) moreView.findViewById(R.id.tag_chaoxiang);
        mTag_leixing = (TagLayout) moreView.findViewById(R.id.tag_leixing);
        mTag_louceng = (TagLayout) moreView.findViewById(R.id.tag_louceng);
        TextView tvReset = (TextView) moreView.findViewById(R.id.tv_menu_reset);
        TextView tvConnmitMore = (TextView) moreView.findViewById(R.id.tv_menu_connmit);

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTag_paixu.setItemSelecte(100);
                mTag_zhuangxiu.setItemSelecte(100);
                mTag_chaoxiang.setItemSelecte(100);
                mTag_leixing.setItemSelecte(100);
                mTag_louceng.setItemSelecte(100);
                acreageMax="";
                chaooo="";
                zhangggg=0;
                leiii=0;
                acreageMin="";
                storey=0;
                acreageMax="";
                moremim = "";
                moremam = "";
                moremixl = "";
                moremaxl = "";
            }
        });

        tvConnmitMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moremim = moremixm.getText().toString().trim();
                moremam = moremaxm.getText().toString().trim();
                moremixl = moremixlc.getText().toString().trim();
                moremaxl = moremaxlc.getText().toString().trim();
                if (!moremim.equals("") && !moremam.equals("")) {
                    if(!moremixl.equals("") && !moremaxl.equals("")){
                        if (Float.parseFloat(moremaxl) > Float.parseFloat(moremixl)) {
                            moremixlc.setText("");
                            moremaxlc.setText("");
                            ToolUtil.closeKeyboard(etZuidi, getActivity());
                            sumfloorMin = moremixl;
                            sumfloorMax = moremaxl;

                        } else {
                            ToastUtil.showShortToast(getActivity(), "最高的价格不能低于最低的价格");
                            return;
                        }
                    }
                    if (Float.parseFloat(moremam) > Float.parseFloat(moremim)) {
                        moremixm.setText("");
                        moremaxm.setText("");
                        ToolUtil.closeKeyboard(etZuidi, getActivity());
                        acreageMin = moremim;
                        acreageMax = moremam;
                    } else {
                        ToastUtil.showShortToast(getActivity(), "最高的价格不能低于最低的价格");
                        return;
                    }
                    autoRefresh();
                    mDropDownMenu.closeMenu();
                }else {
                    if(!moremixl.equals("") && !moremaxl.equals("")){
                        if (Float.parseFloat(moremaxl) > Float.parseFloat(moremixl)) {
                            moremixlc.setText("");
                            moremaxlc.setText("");
                            ToolUtil.closeKeyboard(etZuidi, getActivity());
                            sumfloorMin = moremixl;
                            sumfloorMax = moremaxl;
                        } else {
                            ToastUtil.showShortToast(getActivity(), "最高的价格不能低于最低的价格");
                            return;
                        }
                    }
                    autoRefresh();
                    mDropDownMenu.closeMenu();
                }

            }
        });

        setMyAdapter(mTag_paixu, paixu);
        setMyAdapter(mTag_zhuangxiu, zhuangxiu);
        setMyAdapter(mTag_chaoxiang, chaoxiang);
        setMyAdapter(mTag_leixing, leixing);
        setMyAdapter(mTag_louceng, louceng);
        setOnItemClick(mTag_paixu, 0);
        setOnItemClick(mTag_chaoxiang, 1);
        setOnItemClick(mTag_leixing, 2);
        setOnItemClick(mTag_zhuangxiu, 3);
        setOnItemClick(mTag_louceng, 4);

        //init popupViews
        popupViews.add(quyuView);
        popupViews.add(zongjiaView);
        popupViews.add(shiView);
        popupViews.add(moreView);
        LinearLayout content = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_house_content, null);
        swipeToLoadLayout = (SwipeToLoadLayout) content.findViewById(R.id.refresh_layout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        rvList = (RecyclerView) content.findViewById(R.id.swipe_target);
        rlEmpty = (RelativeLayout) content.findViewById(R.id.rl_ac_chezhu_empty);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SearchMoreAdapter(getActivity(), datas);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, content);
    }

    private void setOnItemClick(TagLayout tagLayout, int type) {
        tagLayout.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                tagLayout.setItemSelecte(postion);
                if (type == 0) {
                    switch (postion) {
                        case 0:
                            acreageMin = "0";
                            acreageMax = "50";
                            break;
                        case 1:
                            acreageMin = "50";
                            acreageMax = "70";
                            break;
                        case 2:
                            acreageMin = "70";
                            acreageMax = "90";
                            break;
                        case 3:
                            acreageMin = "90";
                            acreageMax = "130";
                            break;
                        case 4:
                            acreageMin = "130";
                            acreageMax = "150";
                            break;
                        case 5:
                            acreageMin = "150";
                            acreageMax = "200";
                            break;
                        case 6:
                            acreageMin = "200";
                            acreageMax = "300";
                            break;
                        case 7:
                            acreageMin = "300";
                            acreageMax = "";
                            break;
                    }
                } else if (type == 1) {
                    chaooo = chaoxiang[postion];
                } else if (type == 2) {
                    leiii = postion+1;
                } else if (type == 3) {
                    zhangggg = postion+1;

                } else if (type == 4) {
                    switch (postion) {
                        case 0:
                            storey = 1;
                            break;
                        case 1:
                            storey = 2;
                            break;
                        case 2:
                            storey = 3;
                            break;
                        case 3:
                            storey = 4;
                            break;
                        case 4:
                            storey =5;
                            break;
                        case 5:
                            storey = 6;
                            break;
                        case 6:
                            storey=10;
                            sumfloorMin = "7";
                            sumfloorMax = "12";
                            break;
                        case 7:
                            storey=100;
                            sumfloorMin = "13";
                            break;
                    }


                }
            }
        });
    }

    private void setMyAdapter(TagLayout tagLayout, String[] datas) {
        tagLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return datas.length;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView view = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_tag, parent, false);
                view.setText(datas[position]);
                return view;
            }
        });
    }

    @Override
    protected void initData() {
        llParent.post(new Runnable() {
            @Override
            public void run() {
                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(getActivity()), 0, 0);
            }
        });
        autoRefresh();
    }
    @Override
    protected void initClick() {


    }





    private int floorType = 0;

    private void getDataByNet() {
        //检查网络
        if (!NetworkUtil.checkNet(getActivity())) {
            ToastUtil.showShortToast(getActivity(), "没网啦，请检查网络");
            swipeToLoadLayout.setRefreshing(false);
            return;
        }
        final RequestParams params = new RequestParams(getActivity());
        params.add("pageNo", 1);
        params.add("pageSize", PAGE_SIZE);
        switch (quYu) {
            case 0:
                params.add("region", "柯城");
                break;
            case 1:
                params.add("region", "衢江");
                break;
            case 2:
                params.add("region", "巨化");
                break;
            case 3:
                params.add("region", "其它区域");
                break;
            case 4:

                break;

        }
        Log.d("测试的", "区域: "+quYu);
        switch (tagPosition) {
            case 0:
                minPrice = "0";
                maxPrice = "50";
                break;
            case 1:
                minPrice = "50";
                maxPrice = "80";
                break;
            case 2:
                minPrice = "80";
                maxPrice = "100";
                break;
            case 3:
                minPrice = "100";
                maxPrice = "120";
                break;
            case 4:
                minPrice = "120";
                maxPrice = "150";
                break;
            case 5:
                minPrice = "150";
                maxPrice = "200";
                break;
            case 6:
                minPrice = "200";
                maxPrice = "300";
                break;
            case 7:
                minPrice = "300";
                maxPrice = "";
                break;
            case 8:
                if(isEdixd==true){

                }else {
                    minPrice = "0";
                    maxPrice = "0";
                }
                break;

        }
        Log.d("测试的", "价格: "+tagPosition);
        if (maxPrice.equals("0")) {

        } else {
            params.add("totalpriceMin", minPrice);
            params.add("totalpriceMax", maxPrice);
            Log.d("测试的", "价格: "+minPrice);
            Log.d("测试的", "价格: "+maxPrice);
        }

        if (room > 0) {
            params.add("room", room + "");
            Log.d("测试的", "室: "+room);
        }

        if (hall > 0) {
            params.add("hall", hall + "");
            Log.d("测试的", "厅: "+hall);
        }

        if (toilet > 0) {
            params.add("toilet", toilet + "");
            Log.d("测试的", "卫: "+toilet);
        }

        if(acreageMax.equals("")){

        }else {
            params.add("acreageMin", acreageMin);
            params.add("acreageMax", acreageMax);
            Log.d("测试的", "最大面积: "+acreageMax);
            Log.d("测试的", "最小面积: "+acreageMin);
        }
        if(!chaooo.equals("")){
            params.add("orientation", chaooo);
            Log.d("测试的", "最小面积: "+chaooo);
        }
        if(leiii!=0){
            params.add("application", leiii);
            Log.d("测试的", "类型: "+leiii);
        }
        if(zhangggg!=0){
            params.add("decoration", zhangggg);
            Log.d("测试的", "装修: "+leiii);
        }
        if(acreageMax.equals("")){

        }else {
            params.add("acreageMin", acreageMin);
            params.add("acreageMax", acreageMax);
            Log.d("测试的", "面积: "+acreageMin);
            Log.d("测试的", "面积: "+acreageMax);
        }
        if(storey==0){

        }else if(storey==10){
            if(!sumfloorMin.equals("")&&!sumfloorMax.equals("")){
                params.add("storeyMin",Integer.valueOf(sumfloorMin));
                params.add("storeyMax", Integer.valueOf(sumfloorMax));
                Log.d("测试的", "层数: "+sumfloorMin);
                Log.d("测试的", "层数: "+sumfloorMax);
            }else {

            }
        }else if(storey==100){
            params.add("storeyMin",Integer.valueOf(sumfloorMin));
        }else {
            params.add("storey",storey);
            Log.d("测试的", "层数: "+storey);
        }

        if(!xiaoquName.equals("")){
            params.add("titleOrAddress", xiaoquName);
            Log.d("测试的", "条件: "+acreageMax);
        }
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + SEARCH_HOUSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        datas.clear();
                        List<NewHouseInfo> newHouseInfos = JSON.parseArray(body, NewHouseInfo.class);
                        if (newHouseInfos.size() > 0) {
                            datas.addAll(newHouseInfos);
                            mAdapter.notifyDataSetChanged();
                            pageNo = 1;
                            rlEmpty.setVisibility(View.GONE);
                        } else {
                            swipeToLoadLayout.setRefreshing(false);
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);
                    }

                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setRefreshing(false);
                    }
                });


    }


    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 200);
    }

    private void refresh() {
        getDataByNet();
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMore();
            }
        }, 200);
    }

    private void loadMore() {

        more();
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    private void more() {
        if (!NetworkUtil.checkNet(getActivity())) {
            ToastUtil.showShortToast(getActivity(), "没网了，请检查网络");
            swipeToLoadLayout.setLoadingMore(false);
            return;
        }
        if (datas.isEmpty()) {
            swipeToLoadLayout.setLoadingMore(false);
            return;
        }
        RequestParams params = new RequestParams(getActivity());
//        params.add("flag","true");
        params.add("pageNo", pageNo + 1);
        params.add("pageSize", PAGE_SIZE);
        switch (quYu) {
            case 0:
                params.add("region", "柯城");
                break;
            case 1:
                params.add("region", "衢江");
                break;
            case 2:
                params.add("region", "巨化");
                break;
            case 3:
                params.add("region", "其它区域");
                break;
            case 4:

                break;

        }
        switch (tagPosition) {
            case 0:
                minPrice = "0";
                maxPrice = "50";
                break;
            case 1:
                minPrice = "50";
                maxPrice = "80";
                break;
            case 2:
                minPrice = "80";
                maxPrice = "100";
                break;
            case 3:
                minPrice = "100";
                maxPrice = "120";
                break;
            case 4:
                minPrice = "120";
                maxPrice = "150";
                break;
            case 5:
                minPrice = "150";
                maxPrice = "200";
                break;
            case 6:
                minPrice = "200";
                maxPrice = "300";
                break;
            case 7:
                minPrice = "300";
                maxPrice = "";
                break;
            case 8:
                if(isEdixd==true){

                }else {
                    minPrice = "0";
                    maxPrice = "0";
                }
                break;

        }
        if (maxPrice.equals("0")) {

        } else {
            params.add("totalpriceMin", minPrice);
            params.add("totalpriceMax", maxPrice);
        }
        if (room > 0) {
            params.add("room", room + "");
        }
        if (hall > 0) {
            params.add("hall", hall + "");
        }
        if (toilet > 0) {
            params.add("toilet", toilet + "");
        }
        if(acreageMax.equals("")){

        }else {
            params.add("acreageMin", acreageMin);
            params.add("acreageMax", acreageMax);
        }
        if(!chaooo.equals("")){

            params.add("orientation", chaooo);
        }
        if(leiii!=0){
            params.add("application", leiii);
        }
        if(zhangggg!=0){
            params.add("decoration", zhangggg);
        }
        if(acreageMax.equals("")){

        }else {
            params.add("acreageMin", acreageMin);
            params.add("acreageMax", acreageMax);
        }
        if(storey==0){

        }else if(storey==10){
            if(!sumfloorMin.equals("")&&!sumfloorMax.equals("")){
                params.add("storeyMin",Integer.valueOf(sumfloorMin));
                params.add("storeyMax", Integer.valueOf(sumfloorMax));
                Log.d("测试的", "层数: "+sumfloorMin);
                Log.d("测试的", "层数: "+sumfloorMax);
            }else {

            }
        }else if(storey==100){
            params.add("storeyMin",Integer.valueOf(sumfloorMin));
        }else {
            params.add("storey",storey);
            Log.d("测试的", "层数: "+storey);
        }
        if(!xiaoquName.equals("")){
            params.add("titleOrAddress", xiaoquName);

        }
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL + SEARCH_HOUSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                        List<NewHouseInfo> newHouseInfos = JSON.parseArray(body, NewHouseInfo.class);

                        if (newHouseInfos.size() > 0) {
                            datas.addAll(newHouseInfos);
                            mAdapter.notifyDataSetChanged();
                            pageNo++;
                        } else {
                            ToastUtil.showShortToast(getActivity(), "没有更多了...");
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);
                    }

                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setLoadingMore(false);
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Log.d("sadfasdf", "onItemClick: "+String.valueOf(datas.get(position).getId()));
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra("istatle", "房屋详情");
        intent.putExtra("houseId",String.valueOf(datas.get(position).getId()) );
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}