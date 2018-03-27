package com.example.bsproperty.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.utils.AccBeanDaoUtils;
import com.example.bsproperty.utils.NewBeanDaoUtils;
import com.example.bsproperty.utils.TypeBeanDaoUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wdxc1 on 2018/2/25.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
 
            public SMSBroadcastReceiver() {
        super();
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                String sender = smsMessage.getDisplayOriginatingAddress();
                if(MyApplication.getInstance().getNumList().contains(sender)){
                    try{
                        NewBeanDaoUtils newDao = new NewBeanDaoUtils(context);
                        AccBeanDaoUtils accBeanDaoUtils = new AccBeanDaoUtils(context);
                        TypeBeanDaoUtils typeBeanDaoUtils = new TypeBeanDaoUtils(context);

                        List<AccBean> beans1 = accBeanDaoUtils.queryTestByNativeSql("where account = ?", new String[]{"支付宝"});
                        AccBean zfb;
                        if (beans1.size() > 0) {
                            zfb = beans1.get(0);
                        } else {
                            AccBean a2 = new AccBean();
                            a2.setAccount("支付宝");
                            a2.setMoney(0);
                            accBeanDaoUtils.insert(a2);
                            List<AccBean> beans2 = accBeanDaoUtils.queryTestByNativeSql("where account = ?", new String[]{"支付宝"});
                            zfb = beans2.get(0);
                        }

                        NewBean newBean = new NewBean();
                        newBean.setAccId(zfb.getId());
                        newBean.setAddress("支付宝");
                        DecimalFormat fd = new DecimalFormat("00.00");
                        //短信内容
                        String content = smsMessage.getDisplayMessageBody();
                        long date = smsMessage.getTimestampMillis();

                        String[] daifu = content.split("代付");
                        String money;
                        if (daifu.length > 1) {
                            money = daifu[1].split("元")[0];
                        } else {
                            String[] fukuan = content.split("付款");
                            money = fukuan[1].split("元")[0];
                        }

                        newBean.setMoney(Double.parseDouble(fd.format(Double.parseDouble(money))));
                        List<TypeBean> typeBeans = typeBeanDaoUtils.queryTestByNativeSql("where type = ? and flag = 0", new String[]{"支付宝"});
                        TypeBean typeBean;
                        if (typeBeans.size() > 0){
                            typeBean = typeBeans.get(0);
                        }else{
                            TypeBean t7 = new TypeBean();
                            t7.setFlag(0);
                            t7.setType("支付宝");
                            typeBeanDaoUtils.insert(t7);
                            List<TypeBean> typeBeans1 = typeBeanDaoUtils.queryTestByNativeSql("where type = ? and flag = 0", new String[]{"支付宝"});
                            typeBean = typeBeans1.get(0);
                        }
                        newBean.setTypeId(typeBean.getId());
                        newBean.setTime(date);
                        boolean suc = newDao.insert(newBean);

                        if (suc) {
                            AccBean acc = accBeanDaoUtils.queryTestById(newBean.getAccId());
                            acc.setMoney(Double.parseDouble(fd.format(acc.getMoney() - newBean.getMoney())));
                            boolean suc2 = accBeanDaoUtils.updateTest(acc);
                        }
                    }catch (Exception e){

                    }
                }

                abortBroadcast();
            }
        }
    }
}
