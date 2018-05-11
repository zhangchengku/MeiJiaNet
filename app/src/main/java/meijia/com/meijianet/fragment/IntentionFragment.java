package meijia.com.meijianet.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;

import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;

import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.vo.intention.IntentionVo;
import meijia.com.srdlibrary.commondialog.CommonDialog;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/2/5
 */
public class IntentionFragment extends BaseFragment {

    private String type = "";
    private RecyclerView rvList;
    private List<IntentionVo> datas = new ArrayList<>();
    private LinearLayoutManager mManager;
//    private IntentionFragmentAdapter mAdapter;
    private RelativeLayout rlEmpty;
    private CommonDialog mDialog;

    private String ordernum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_fragment,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        rvList = (RecyclerView) view.findViewById(R.id.rv_fm_intention_list);
        rlEmpty = (RelativeLayout) view.findViewById(R.id.rl_ac_chezhu_empty);
    }


    @Override
    protected void initData() {
//        type = getArguments().getString("type");
//        mAdapter = new IntentionFragmentAdapter(getActivity(),datas);
//        mManager = new LinearLayoutManager(getActivity());
//        rvList.setLayoutManager(mManager);
//        rvList.setAdapter(mAdapter);
//        getDataByNet();
    }

    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("tag", "onResume: "+type );
    }

    private void tuikuan(String ordernum) {

    }

    private void cancleOrder(String ordernum) {
        if (!NetworkUtil.checkNet(getActivity())){
            ToastUtil.showShortToast(getActivity(),"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(getActivity(),false);
        OkHttpUtils.post()
                .tag(getActivity())
                .url(BaseURL.BASE_URL+CANCLE_ORDER)
                .addParams("ordernum",ordernum)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        getDataByNet();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(),returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void showMyDialog(String ordernum) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pay_layout, null);
        view.findViewById(R.id.tv_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ordernum!=null && !ordernum.equals("")){
                    wechatPay(ordernum);
                }
                mDialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_ali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ordernum!=null && !ordernum.equals("")){
                    aliPay(ordernum);
                }
                mDialog.dismiss();
            }
        });
        mDialog = new CommonDialog(getActivity(),view,0, Gravity.BOTTOM);
        mDialog.show();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (IntentionFragment.this.ordernum !=null && !IntentionFragment.this.ordernum.equals("")){
                    IntentionFragment.this.ordernum = "";
                }
            }
        });
    }

    private void getDataByNet() {
        //检查网络
        if (!NetworkUtil.checkNet(getActivity())){
            ToastUtil.showShortToast(getActivity(),"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(getActivity(),false);
        OkHttpUtils.post()
                .tag(getActivity())
                .url(BaseURL.BASE_URL + MY_ITENTION)
                .addParams("type",type)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        List<IntentionVo> intentionVos = JSON.parseArray(body, IntentionVo.class);
                        if (intentionVos!=null && intentionVos.size()>0){
                            datas.clear();
                            datas.addAll(intentionVos);
//                            mAdapter.notifyDataSetChanged();
                            rlEmpty.setVisibility(View.GONE);
                        }else {
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });


    }

    //    //微信支付
    private void wechatPay(String orderNumber) {
        if (!NetworkUtil.checkNet(getActivity())){
            ToastUtil.showShortToast(getActivity(),"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(getActivity(),false);
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL + WX_PAY)
                .addParams("orderNum", orderNumber)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    //支付宝支付
    private void aliPay(String orderNumber) {
        if (!NetworkUtil.checkNet(getActivity())){
            ToastUtil.showShortToast(getActivity(),"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(getActivity(),false);
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL + ALI_PAY)
                .addParams("orderNum",orderNumber)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        try {
                            JSONObject object = new JSONObject(body);
                            String singnedPayStr = object.getString("alipay");
                            if (singnedPayStr.equals("")) {
                                ToastUtil.showShortToast(getActivity(), "无法支付");
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

}
