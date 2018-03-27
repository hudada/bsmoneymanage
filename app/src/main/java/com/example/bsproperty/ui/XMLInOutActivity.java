package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsproperty.R;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.fragment.Fragment03;
import com.example.bsproperty.utils.AccBeanDaoUtils;
import com.example.bsproperty.utils.NewBeanDaoUtils;
import com.example.bsproperty.utils.TypeBeanDaoUtils;
import com.thoughtworks.xstream.XStream;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XMLInOutActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.tv_info)
    EditText tvInfo;
    private Intent intent;
    private String oixml;
    private NewBeanDaoUtils utils;
    private TypeBeanDaoUtils typeBeanDaoUtils;
    private AccBeanDaoUtils accBeanDaoUtils;

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnRight.setText("导入");
        tvTitle.setText("导入/导出记录");
        intent = getIntent();
        oixml = intent.getStringExtra("xml");
        if (!TextUtils.isEmpty(oixml)) {
            tvInfo.setText(oixml);
        }
        utils = new NewBeanDaoUtils(this);
        typeBeanDaoUtils = new TypeBeanDaoUtils(this);
        accBeanDaoUtils = new AccBeanDaoUtils(this);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_xml;
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
            case R.id.btn_right:
                XStream xs = new XStream();
                xs.alias("Data", Fragment03.XmlBean.class);
                xs.alias("item", Fragment03.NewList.class);
                Fragment03.XmlBean x;
                try {
                    x = (Fragment03.XmlBean) xs.fromXML(tvInfo.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(this, "无法解析该XML！请检查后重试！", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Fragment03.NewList n : x.getNewLists()) {
                    try {
                        DecimalFormat df=new DecimalFormat("00.00");
                        n.setMoney(df.format(Double.parseDouble(n.getMoney())));
                        List<TypeBean> ts = typeBeanDaoUtils.queryTestByNativeSql("where TYPE = ? AND flag=?",
                                new String[]{n.getType(), n.getFlag() + ""});
                        List<AccBean> as = accBeanDaoUtils.queryTestByNativeSql("where ACCOUNT = ?",
                                new String[]{n.getAcc()});
                        NewBean newBean = new NewBean();
                        SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Long typeId = 1l;
                        Long accId = 1l;
                        if (ts.size() != 0 && as.size() != 0) {
                            typeId = ts.get(0).getId();
                            accId = as.get(0).getId();
                        } else if (ts.size() == 0 && as.size() != 0) {
                            TypeBean t = new TypeBean();
                            t.setType(n.getType());
                            t.setFlag(n.getFlag());
                            boolean suc = typeBeanDaoUtils.insert(t);
                            if (!suc) {
                                continue;
                            }
                            typeId = typeBeanDaoUtils.queryTestByNativeSql("where TYPE = ? AND flag=?",
                                    new String[]{n.getType(), n.getFlag() + ""}).get(0).getId();
                            accId = as.get(0).getId();
                        } else if (ts.size() != 0 && as.size() == 0) {
                            typeId = ts.get(0).getId();
                            AccBean accBean = new AccBean();
                            accBean.setAccount(n.getAcc());
                            accBean.setMoney(0.00);
                            boolean suc = accBeanDaoUtils.insert(accBean);
                            if (!suc) {
                                continue;
                            }
                            accId = accBeanDaoUtils.queryTestByNativeSql("where ACCOUNT = ?",
                                    new String[]{n.getAcc()}).get(0).getId();
                        } else if (ts.size() == 0 && as.size() == 0) {
                            TypeBean t = new TypeBean();
                            t.setType(n.getType());
                            t.setFlag(n.getFlag());
                            boolean suc = typeBeanDaoUtils.insert(t);
                            if (!suc) {
                                continue;
                            }
                            typeId = typeBeanDaoUtils.queryTestByNativeSql("where TYPE = ? AND flag=?",
                                    new String[]{n.getType(), n.getFlag() + ""}).get(0).getId();
                            AccBean accBean = new AccBean();
                            accBean.setAccount(n.getAcc());
                            accBean.setMoney(0.00);
                            boolean suc2 = accBeanDaoUtils.insert(accBean);
                            if (!suc2) {
                                continue;
                            }
                            accId = accBeanDaoUtils.queryTestByNativeSql("where ACCOUNT = ?",
                                    new String[]{n.getAcc()}).get(0).getId();
                        }

                        newBean.setTime(fo.parse(n.getTime()).getTime());
                        newBean.setMoney(Double.parseDouble(n.getMoney()));
                        newBean.setAddress(n.getAddrr());
                        newBean.setAccId(accId);
                        newBean.setTypeId(typeId);
                        utils.insert(newBean);
                        AccBean toAcc = accBeanDaoUtils.queryTestById(accId);
                        if (n.getFlag() == 0) {
                            toAcc.setMoney(Double.parseDouble(df.format(toAcc.getMoney() - Double.parseDouble(n.getMoney()))));
                        } else {
                            toAcc.setMoney(Double.parseDouble(df.format(toAcc.getMoney() + Double.parseDouble(n.getMoney()))));
                        }
                        accBeanDaoUtils.updateTest(toAcc);
                    } catch (Exception e) {
                        Toast.makeText(this, "id为：" + n.getId() + "数据解析出现错误！可能造成导入失败！", Toast.LENGTH_LONG).show();
                        continue;
                    }
                }
                Toast.makeText(this, "导入完毕！", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                this.finish();
                break;
        }
    }
}
