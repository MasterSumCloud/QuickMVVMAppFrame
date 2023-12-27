package com.demo.appframe.wxapi;

import android.content.Context;

import com.blankj.utilcode.util.GsonUtils;
import com.demo.appframe.beans.BuyResponseBackBean;
import com.demo.appframe.core.AppConfig;
import com.demo.appframe.util.PayUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class Wx {

    public static final String APP_ID = AppConfig.INSTANCE.getWECHAT_APPID();
    private static final String MCH_ID = AppConfig.INSTANCE.getWECHAT_MCH_ID();
    private IWXAPI api;
    private static Wx instance;
    public static PayUtils.IPayUtilCallback mCallback;

    public static Wx getInstance() {
        if (instance == null)
            instance = new Wx();
        return instance;
    }

    public void startPay(Context context, String json, PayUtils.IPayUtilCallback callback) {
        mCallback = callback;
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        BuyResponseBackBean config = GsonUtils.fromJson(json, BuyResponseBackBean.class);
        PayReq req = new PayReq();
        req.appId = APP_ID;
        req.partnerId = MCH_ID;
        req.prepayId = config.getPrepayid();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = config.getNoncestr();
        req.timeStamp = config.getTimestamp() + "";
        req.sign = config.getSign();
        api.sendReq(req);
    }

}
