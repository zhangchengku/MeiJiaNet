/*
 * Created by 岱青海蓝信息系统(北京)有限公司 on 17-7-14 下午3:21
 * Copyright (c) 2017. All rights reserved.
 */

package meijia.com.meijianet.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import meijia.com.meijianet.R;


/**
 * Created by mac on 16/11/29.
 * 拨打电话确认 dialog
 */
public class CommonDialog extends Dialog {

    private Context context;
    private View mView;

    public CommonDialog(Context context, int layoutId) {
        super(context, R.style.CustomDialog);
        this.context = context;
        mView = LayoutInflater.from(context).inflate(layoutId,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public View getView(){
        if (mView!=null){
            return mView;
        }
        return null;
    }

    private void initView() {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.dialog_call_confirm_cancel, null);
//        setContentView(view);
//
//        TextView tv_phone_number = (TextView) view.findViewById(R.id.tv_phone_number);
//        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
//        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//
//        tv_phone_number.setText(phoneNumber);
//        btn_confirm.setOnClickListener(this);
//        btn_cancel.setOnClickListener(this);
//
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = dm.widthPixels ;
        window.setAttributes(lp);
    }


}
