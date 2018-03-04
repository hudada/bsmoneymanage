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
import com.example.bsproperty.utils.AccBeanDaoUtils;
import com.example.bsproperty.utils.NewBeanDaoUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccEditActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_acc)
    EditText tvAcc;
    @BindView(R.id.tv_value)
    EditText tvValue;
    @BindView(R.id.btn_moretype)
    Button btnMoretype;
    private Intent forIntent;
    private Long id = -1l;
    private AccBeanDaoUtils accDao;
    AccBean accBean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        accDao = new AccBeanDaoUtils(this);
        forIntent = getIntent();
        id = forIntent.getLongExtra("id", -1l);
        if (id == -1l) {
            tvTitle.setText("添加账户");
            btnRight.setText("添加");
            btnMoretype.setVisibility(View.GONE);
            tvAcc.setHint("请输入账户名称...");
            tvValue.setHint("0.00");
        } else {
            accBean = accDao.queryTestById(id);
            tvTitle.setText("修改账户");
            btnRight.setText("修改");
            btnMoretype.setVisibility(View.VISIBLE);
            tvAcc.setText(accBean.getAccount());
            tvValue.setText(accBean.getMoney() + "");
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_acc_edit;
    }

    @Override
    protected void loadData() {


    }


    @OnClick({R.id.btn_back, R.id.btn_right, R.id.btn_moretype})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                setResult(RESULT_CANCELED);
                break;
            case R.id.btn_right:
                if (TextUtils.isEmpty(tvAcc.getText().toString())) {
                    Toast.makeText(this, "账户名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tvValue.getText().toString())) {
                    Toast.makeText(this, "账户金额不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                DecimalFormat f=new DecimalFormat("0.00");
                if (id == -1l) {
                    if(accDao.queryTestByNativeSql("where ACCOUNT = ?",
                            new String[]{tvAcc.getText().toString()}).size()!=0){
                        Toast.makeText(this, "该账户名称已存在！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AccBean newA = new AccBean();
                    newA.setMoney(Double.parseDouble(f.format(Double.parseDouble(tvValue.getText().toString()))));
                    newA.setAccount(tvAcc.getText().toString());
                    boolean suc = accDao.insert(newA);
                    if (suc) {
                        Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        this.finish();
                    } else {
                        Toast.makeText(this, "添加失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(!(accBean.getAccount().equals(tvAcc.getText().toString()))
                            &&accDao.queryTestByNativeSql("where ACCOUNT = ?",
                            new String[]{tvAcc.getText().toString()}).size()!=0){
                        Toast.makeText(this, "该账户名称已存在！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    accBean.setMoney(Double.parseDouble(f.format(Double.parseDouble(tvValue.getText().toString()))));
                    accBean.setAccount(tvAcc.getText().toString());
                    boolean suc2 = accDao.updateTest(accBean);
                    if (suc2) {
                        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        this.finish();
                    } else {
                        Toast.makeText(this, "修改失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_moretype:
                List<AccBean> a= accDao.queryAll();
                if (a.size()<=1){
                    Toast.makeText(this, "删除失败！请至少保留一个账户！", Toast.LENGTH_SHORT).show();
                    return;
                }
                NewBeanDaoUtils newDao = new NewBeanDaoUtils(this);
                List<NewBean> s = newDao.queryTestByNativeSql("where ACC_ID = ?",
                        new String[]{id + ""});
                if (s.size() == 0) {
                    boolean suc2 = accDao.deleteTest(accBean);
                    if (suc2) {
                        Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        this.finish();
                    } else {
                        Toast.makeText(this, "删除失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "当前账户尚有关联明细！请删除明细后重试！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
