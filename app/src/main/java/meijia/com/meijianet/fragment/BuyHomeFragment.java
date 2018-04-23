package meijia.com.meijianet.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import meijia.com.meijianet.ui.HouseDetailActivity;
import meijia.com.meijianet.ui.SearchMoreActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.liushibuju.BaseAdapter;
import meijia.com.srdlibrary.liushibuju.TagLayout;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/4/20.
 */

public class BuyHomeFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, OnItemClickListener, TextView.OnEditorActionListener {
    private DropDownMenus mDropDownMenu;
    private String headers[] = {"区域", "总价", "厅室", "更多"};
    private List<View> popupViews = new ArrayList<>();

    private String citys[] = {"不限", "柯城", "衢江", "巨化", "其他区域"};
    private String ages[] = {"不限", "50万以下", "50-80万", "80-100万", "100-120万", "120-150万",
            "150-200万", "200-300万", "300万以上"};
    private String sexs[] = {"不限", "1室", "2室", "3室", "4室", "5室", "6室"};
    private String constellations[] = {"不限", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};
    //more
    private String louceng[] = {"1楼", "2楼", "3楼", "4楼", "5楼", "6楼"};
    private String chaoxiang[] = {"东", "南", "西", "北", "东南", "东北", "西南", "西北"};
    private String zhuangxiu[] = {"毛坯", "简单装修", "中档装修", "精装修", "豪华装修"};
    private String paixu[] = {"默认排序", "总价从高到低", "总价从低到高", "面积从大到小", "面积从小到大"};
    private String leixing[] = {"单体别墅", "排屋", "多层", "复式", "小高楼", "店面", "写字楼"};

    private int tagPosition = 0;//总价标记
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


    private int quYu = 0;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy_home, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    @Override
    protected void initView() {
        etSearch = (EditText) view.findViewById(R.id.et_ac_search);
        ivDelete = (ImageView) view.findViewById(R.id.iv_ac_search_delete);
        llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
        ToolUtil.setInputListener(etSearch, ivDelete);
        mDropDownMenu = (DropDownMenus) view.findViewById(R.id.dropDownMenu);
        RecyclerView recyclerView = new RecyclerView(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setBackgroundColor(Color.WHITE);
        recyclerView.setPadding(0, 0, 0, 50);
        recyclerView.setLayoutManager(manager);
        CityAdapter adapter = new CityAdapter(getActivity(), Arrays.asList(citys));
        recyclerView.setAdapter(adapter);
        View zongjiaView = LayoutInflater.from(getActivity()).inflate(R.layout.muen_zongjia, null);
        TagLayout tagLayout = (TagLayout) zongjiaView.findViewById(R.id.tag_layout);
        EditText etZuidi = (EditText) zongjiaView.findViewById(R.id.et_zuidi);
        EditText etZuigao = (EditText) zongjiaView.findViewById(R.id.et_zuigao);
        TextView tvConnmit = (TextView) zongjiaView.findViewById(R.id.tv_confirm);
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
        tagLayout.setItemSelecte(0);
        tagLayout.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                tagLayout.setItemSelecte(postion);
                tagPosition = postion;

                switch (postion) {
                    case 0:
                        minPrice = "0";
                        maxPrice = "10";
                        break;
                    case 1:
                        minPrice = "100";
                        maxPrice = "150";
                        break;
                    case 2:
                        minPrice = "150";
                        maxPrice = "160";
                        break;
                    case 3:
                        minPrice = "10";
                        maxPrice = "200";
                        break;
                    case 4:
                        minPrice = "50";
                        maxPrice = "90";
                        break;
                    case 5:
                        minPrice = "300";
                        maxPrice = "350";
                        break;
                    case 6:
                        minPrice = "350";
                        maxPrice = "500";
                        break;
                    case 7:
                        minPrice = "200";
                        maxPrice = "300";
                        break;
                    case 8:
                        minPrice = "200";
                        maxPrice = "210";
                        break;

                }
            }
        });

        tvConnmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zuidi = etZuidi.getText().toString().trim();
                String zuigao = etZuigao.getText().toString().trim();

                if (!zuidi.equals("") && !zuigao.equals("")) {

                    if (Float.parseFloat(zuigao) > Float.parseFloat(zuidi)) {
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

                } else {

                    mDropDownMenu.setTabText(tagPosition == 0 ? headers[1] : ages[tagPosition]);
                }
//                if (!maxPrice.equals("0")){
//                    autoRefresh();
//                }
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
        tagLayout2.setItemSelecte(0);
        tagLayout2.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                tagLayout2.setItemSelecte(postion);
                tagPosition2 = postion;
                room = postion+2;
            }
        });

        tvConnmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shi = etShi.getText().toString().trim();
                String ting = etTing.getText().toString().trim();
                String wei = etWei.getText().toString().trim();
                if (shi.equals("") && ting.equals("") && wei.equals("")) {
                    if (tagPosition2 != 0) {
                        mDropDownMenu.setTabText(tagPosition2 == 0 ? headers[2] : sexs[tagPosition2]);
                    }else {
                        mDropDownMenu.setTabText(headers[2]);
                    }
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

        //initMore
        View moreView = LayoutInflater.from(getActivity()).inflate(R.layout.muen_more, null);
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
                mTag_paixu.setItemSelecte(0);
                mTag_zhuangxiu.setItemSelecte(100);
                mTag_chaoxiang.setItemSelecte(100);
                mTag_leixing.setItemSelecte(100);
                mTag_louceng.setItemSelecte(100);
            }
        });

        tvConnmitMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRefresh();
                mDropDownMenu.closeMenu();
            }
        });

        setMyAdapter(mTag_paixu, paixu);
        setMyAdapter(mTag_zhuangxiu, zhuangxiu);
        setMyAdapter(mTag_chaoxiang, chaoxiang);
        setMyAdapter(mTag_leixing, leixing);
        setMyAdapter(mTag_louceng, louceng);
        mTag_paixu.setItemSelecte(0);
        setOnItemClick(mTag_paixu, 0);
        setOnItemClick(mTag_chaoxiang, 1);
        setOnItemClick(mTag_leixing, 2);
        setOnItemClick(mTag_zhuangxiu, 3);
        setOnItemClick(mTag_louceng, 4);

        //init popupViews
        popupViews.add(recyclerView);
        popupViews.add(zongjiaView);
        popupViews.add(shiView);
        popupViews.add(moreView);

        //区域点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                adapter.setCheckItem(position);
                quYu = position;
                mDropDownMenu.setTabText(position == 0 ? headers[0] : citys[position]);
                autoRefresh();
                mDropDownMenu.closeMenu();
            }
        });

        //init context view

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
                    paixuPosition = postion;
                    morePaixu = postion;
                } else if (type == 1) {
                    chaoxiangPositon = postion;
                    moreChaoxiang = postion;
                } else if (type == 2) {
                    leixingPosition = postion;
                    moreType = postion;
                } else if (type == 3) {
                    zhuangxiuPositon = postion;
                    zhuangxiuType = postion;
                } else if (type == 4) {
                    loucengPosition = postion;
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
        etSearch.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
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
//        params.add("flag","true");
        params.add("pageNo", 1);
        params.add("pageSize", PAGE_SIZE);
        switch (quYu) {
            case 0:

                break;
            case 1:
                params.add("region", "衢江");
                break;
            case 2:
                params.add("region", "巨化");
                break;
            case 3:
                params.add("region", "柯城");
                break;
            case 4:
                params.add("region", "其他区域");
                break;

        }
        if (Float.parseFloat(maxPrice) <= 0) {

        } else {
            params.add("totalpriceMin", 0+"");
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
        if (morePaixu != 0) {
            switch (morePaixu) {
                case 1:
                    params.add("priceAsc", "0");
                    break;
                case 2:
                    params.add("priceAsc", "1");
                    break;
                case 3:
                    params.add("aceareaAsc", "0");
                    break;
                case 4:
                    params.add("aceareaAsc", "1");
                    break;
            }
        }
        if (moreChaoxiang != 0) {
            switch (moreChaoxiang) {
                case 8:
                    params.add("orientation", "东");
                    break;
                case 7:
                    params.add("orientation", "南");
                    break;
                case 6:
                    params.add("orientation", "西");
                    break;
                case 5:
                    params.add("orientation", "北");
                    break;
                case 4:
                    params.add("orientation", "东南");
                    break;
                case 3:
                    params.add("orientation", "东北");
                    break;
                case 2:
                    params.add("orientation", "西南");
                    break;
                case 1:
                    params.add("orientation", "西北");
                    break;
            }
        }
        if (moreType != 0) {
            params.add("application", "" + (moreType+1));
        }
        if (zhuangxiuType != 0) {
            params.add("decoration", "" + zhuangxiuType);
        }
        if (floorType != 0) {
            params.add("storey", "" + floorType);
        }
        if (!xiaoquName.equals("")){
            params.add("name",xiaoquName);
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
    private void refresh() {
        getDataByNet();
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
        intent.putExtra("id", datas.get(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void onLoadMore() {
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

                break;
            case 1:
                params.add("region", "柯城");
                break;
            case 2:
                params.add("region", "衢江");
                break;
            case 3:
                params.add("region", "巨化");
                break;
            case 4:
                params.add("region", "其他区域");
                break;

        }
        if (Float.parseFloat(maxPrice) <= 0) {

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
        if (morePaixu != 0) {
            switch (morePaixu) {
                case 1:
                    params.add("priceAsc", "1");
                    break;
                case 2:
                    params.add("priceAsc", "1");
                    break;
                case 3:
                    params.add("aceareaAsc", "1");
                    break;
                case 4:
                    params.add("aceareaAsc", "1");
                    break;
            }
        }
        if (moreChaoxiang != 0) {
            switch (moreChaoxiang) {
                case 8:
                    params.add("orientation", "东");
                    break;
                case 7:
                    params.add("orientation", "南");
                    break;
                case 6:
                    params.add("orientation", "西");
                    break;
                case 5:
                    params.add("orientation", "北");
                    break;
                case 4:
                    params.add("orientation", "东南");
                    break;
                case 3:
                    params.add("orientation", "东北");
                    break;
                case 2:
                    params.add("orientation", "西南");
                    break;
                case 1:
                    params.add("orientation", "西北");
                    break;
            }
        }
        if (moreType != 0) {
            params.add("application", "" + moreType);
        }
        if (zhuangxiuType != 0) {
            params.add("decoration", "" + zhuangxiuType);
        }
        if (floorType != 0) {
            params.add("storey", "" + floorType);
        }

        if (!xiaoquName.equals("")){
            params.add("name",xiaoquName);
        }
        OkHttpUtils
                .get()
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
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 200);
    }

}
