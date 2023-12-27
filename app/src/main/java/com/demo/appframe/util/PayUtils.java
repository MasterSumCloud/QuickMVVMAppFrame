package com.demo.appframe.util;

import android.app.Activity;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.demo.appframe.R;
import com.demo.appframe.core.Alipay;
import com.demo.appframe.wxapi.Wx;

public class PayUtils {

    private Activity mActivity;
    public static final String TYPE_ALIPAY = "ali";
    public static final String TYPE_WX = "wx";

    public PayUtils(Activity activity) {
        this.mActivity = activity;
    }

    public void payMoney(String json, String payTypeIn) {
        String payType = payTypeIn;
        GeneralUtil.INSTANCE.onUMengClickEvent(mActivity, "wait_pay_counts");
        if (mActivity != null && !TextUtils.isEmpty(json)) {
            pay(payType, json);
        }
    }

    private void pay(String payType, String json) {
        // 微信支付
        if (payType.equals(TYPE_WX)) {
            if (!isWeixinAvilible()) {
                Tos.INSTANCE.showToastShort(mActivity.getString(R.string.wechat_not_install));
            } else {
                Wx.getInstance().startPay(mActivity, json, mCallback);
            }
            // 支付宝支付
        } else if (payType.equals(TYPE_ALIPAY)) {
            Alipay.getInstance().startPay(mActivity, json, mCallback);
        }
    }

    private IPayUtilCallback mCallback;

    public interface IPayUtilCallback {
        void onPaySuccess();

        void onPayFailure();
    }

    public void setIPayUtilCallback(IPayUtilCallback callback) {
        this.mCallback = callback;
    }

    public boolean isWeixinAvilible() {
        return AppUtils.isAppInstalled("com.tencent.mm");
    }
}