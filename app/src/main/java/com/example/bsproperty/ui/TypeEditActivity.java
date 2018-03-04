package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.utils.NewBeanDaoUtils;
import com.example.bsproperty.utils.TypeBeanDaoUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TypeEditActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_type)
    EditText tvType;
    @BindView(R.id.btn_moretype)
    Button btnMoretype;
    @BindView(R.id.rb_01)
    RadioButton rb_01;
    @BindView(R.id.rb_02)
    RadioButton rb_02;
    @BindView(R.id.re_01)
    RelativeLayout re_01;
    private Intent forIntent;
    private TypeBean typeBean;
    private TypeBeanDaoUtils typeDao;
    private Long id = -1l;
    private ArrayList<TypeBean> outtypes = new ArrayList<>();
    private ArrayList<TypeBean> intypes = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        typeDao = new TypeBeanDaoUtils(this);
        forIntent = getIntent();
        id = forIntent.getLongExtra("id", -1l);

        if (id == -1l) {
            tvTitle.setText("添加类别");
            btnRight.setText("添加");
            re_01.setVisibility(View.VISIBLE);
            btnMoretype.setVisibility(View.GONE);
            tvType.setHint("请输入账户名称...");
            rb_01.setChecked(true);
            rb_02.setChecked(false);
        } else {
            re_01.setVisibility(View.GONE);
            typeBean = typeDao.queryTestById(id);
            tvTitle.setText("修改类别");
            btnRight.setText("修改");
            btnMoretype.setVisibility(View.VISIBLE);
            tvType.setText(typeBean.getType());
//            if (typeBean.getFlag() == 0) {
//                rb_01.setChecked(true);
//                rb_02.setChecked(false);
//            } else {
//                rb_01.setChecked(false);
//                rb_02.setChecked(true);
//            }
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_type_edit;
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
                if (TextUtils.isEmpty(tvType.getText().toString())) {
                    Toast.makeText(this, "类型名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id == -1l) {
                    TypeBean ty = new TypeBean();
                    ty.setType(tvType.getText().toString());
                    if (rb_01.isChecked()) {
                        ty.setFlag(0);
                    } else {
                        ty.setFlag(1);
                    }
                    if(typeDao.queryTestByNativeSql("where TYPE = ? AND FLAG=?",
                            new String[]{ty.getType(),ty.getFlag()+""}).size()!=0){
                        Toast.makeText(this, "该类型已存在！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean suc = typeDao.insert(ty);
                    if (suc) {
                        Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("flag",rb_01.isChecked());
                        setResult(RESULT_OK,intent);
                        this.finish();
                    } else {
                        Toast.makeText(this, "添加失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(!(typeBean.getType().equals(tvType.getText().toString()))
                            &&typeDao.queryTestByNativeSql("where TYPE = ? AND FLAG=?",
                            new String[]{tvType.getText().toString(),typeBean.getFlag()+""}).size()!=0){
                        Toast.makeText(this, "该类型已存在！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    typeBean.setType(tvType.getText().toString());
                    boolean suc2 = typeDao.updateTest(typeBean);
                    if (suc2) {
                        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("flag",typeBean.getFlag()==0?true:false);
                        setResult(RESULT_OK,intent);
                        this.finish();
                    } else {
                        Toast.makeText(this, "修改失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_moretype:
                MyApplication.getInstance().getTypeList();
                outtypes=MyApplication.getOuttypes();
                intypes=MyApplication.getIntypes();
                if (typeBean.getFlag()==0){
                    if (outtypes.size() <= 1) {
                        Toast.makeText(this, "删除失败！请至少保留一个支出类别！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    if (intypes.size() <= 1) {
                        Toast.makeText(this, "删除失败！请至少保留一个收入类别！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                NewBeanDaoUtils newDao = new NewBeanDaoUtils(this);
                List<NewBean> s = newDao.queryTestByNativeSql("where TYPE_ID = ?",
                        new String[]{id + ""});
                if (s.size() == 0) {
                    boolean suc2 = typeDao.deleteTest(typeBean);
                    if (suc2) {
                        Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("flag",typeBean.getFlag()==0?true:false);
                        setResult(RESULT_OK,intent);
                        this.finish();
                    } else {
                        Toast.makeText(this, "删除失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "当前类别尚有关联明细！请删除明细后重试！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
