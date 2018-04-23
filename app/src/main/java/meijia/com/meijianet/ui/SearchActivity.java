package meijia.com.meijianet.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.SearchRefreshAdapter;
import meijia.com.meijianet.activity.SearchRemoveAdapter;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.liushibuju.TagLayout;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class SearchActivity extends BaseActivity implements View.OnKeyListener {
    private TextView tvFinish;
    private EditText etSearch;
//    private ImageView ivRefresh;
    private ImageView ivRemove;
    private TagLayout mRefreshTag,mRemoveTag;
    private LinearLayout llHistory;
    private List<String> mItems = new ArrayList<>(); ;
    private SearchRefreshAdapter mAdapter;
    private SearchRemoveAdapter mRemoveAdapter;
    private Animation mRotate;
    private boolean isRefresh;
    private List<String> listByShare;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_search);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        llHistory = (LinearLayout) findViewById(R.id.ll_history);
//        ivRefresh = (ImageView) findViewById(R.id.iv_ac_search_refresh);
        ivRemove = (ImageView) findViewById(R.id.iv_ac_search_remove);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        etSearch = (EditText) findViewById(R.id.et_ac_search);
        mRemoveTag = (TagLayout) findViewById(R.id.tl_ac_search_remove);
        mRefreshTag = (TagLayout) findViewById(R.id.tl_ac_search_refresh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> list = SharePreUtil.getListByShare(SearchActivity.this);
        listByShare.clear();
        listByShare.addAll(list);
        //配置清除tag
        if (listByShare.size()<=0){
            llHistory.setVisibility(View.GONE);
        }else {
            llHistory.setVisibility(View.VISIBLE);
            mRemoveAdapter = new SearchRemoveAdapter(this, listByShare);
            mRemoveTag.setAdapter(mRemoveAdapter);
            mRemoveTag.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
                @Override
                public void onChildClick(View view, int postion) {
                    String text = ((TextView) view).getText().toString().trim();
                    searchHome(text);
                }
            });
        }
    }

    @Override
    protected void initData() {
        listByShare = SharePreUtil.getListByShare(SearchActivity.this);
        // 标签 后台获取 很多的实现方式  加List<String> 的集合
        mAdapter = new SearchRefreshAdapter(this,mItems);
        mItems.add("东迹三巷");
        mItems.add("新屋里小区");
        mItems.add("阳光新屋里");
        mItems.add("巨化");
        mItems.add("金鑫家园");
        //配置更新tag
        mRefreshTag.setAdapter(mAdapter);
        mRefreshTag.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                String text = ((TextView) view).getText().toString().trim();
                boolean isTrue = false;
                int p = 100;
                if (listByShare!=null){
                    if (listByShare.size()>=10){
                        for (int i = 0; i < listByShare.size(); i++) {
                            if (listByShare.get(i).equals(text)){
                                isTrue = true;
                                p = i;
                            }
                        }
                        if (!isTrue){
                            listByShare.remove(0);
                            listByShare.add(text);
                        }
                    }else if (listByShare.size()<=0){
                        listByShare.add(text);
                    }else {
                        for (int i = 0; i < listByShare.size(); i++) {
                            if (listByShare.get(i).equals(text)){
                                isTrue = true;
                                p = i;
                            }
                        }
                        if (!isTrue){
                            listByShare.add(text);
                        }
                    }
                    SharePreUtil.putListToShare(SearchActivity.this,listByShare);
                }
                searchHome(text);
            }
        });

        //配置清除tag
        if (listByShare.size()<=0){
            llHistory.setVisibility(View.GONE);
        }else {
            llHistory.setVisibility(View.VISIBLE);
            mRemoveAdapter = new SearchRemoveAdapter(this, listByShare);
            mRemoveTag.setAdapter(mRemoveAdapter);
            mRemoveTag.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
                @Override
                public void onChildClick(View view, int postion) {
                    String text = ((TextView) view).getText().toString().trim();
                    searchHome(text);
                }
            });
        }

    }

    @Override
    protected void initClick() {
        tvFinish.setOnClickListener(this);
        etSearch.setOnKeyListener(this);
        ivRemove.setOnClickListener(this);
//        ivRefresh.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_cancle:
                    finish();
                    break;
                case R.id.iv_ac_search_remove://清空历史记录
                    if (listByShare!=null){
                        listByShare.clear();
                        SharePreUtil.putListToShare(SearchActivity.this,listByShare);
                    }
                    llHistory.setVisibility(View.GONE);
                    break;
                case R.id.tv_finish://搜索
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void searchHome(String trim) {
        etSearch.setText("");
        Intent intent = new Intent(SearchActivity.this, SearchDetailActivity.class);
        intent.putExtra("city",trim);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            // 对应逻辑操作
            String trim = etSearch.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
                ToastUtil.showShortToast(SearchActivity.this,"搜索内容不能饿为空");
                return false;
            }
            //隐藏软键盘
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            boolean isTrue = false;
            int p = 100;
            if (listByShare!=null){
                if (listByShare.size()>=10){
                    for (int i = 0; i < listByShare.size(); i++) {
                        if (listByShare.get(i).equals(trim)){
                            isTrue = true;
                            p = i;
                        }
                    }
                    if (!isTrue){
                        listByShare.remove(0);
                        listByShare.add(trim);
                    }
                }else if (listByShare.size()<=0){
                    listByShare.add(trim);
                }else {
                    for (int i = 0; i < listByShare.size(); i++) {
                        if (listByShare.get(i).equals(trim)){
                            isTrue = true;
                            p = i;
                        }
                    }
                    if (!isTrue){
                        listByShare.add(trim);
                    }
                }
                SharePreUtil.putListToShare(SearchActivity.this,listByShare);
            }
            searchHome(trim);
            return true;
        }
        return false;
    }
}
