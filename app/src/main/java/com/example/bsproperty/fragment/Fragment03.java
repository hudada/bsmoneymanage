package com.example.bsproperty.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.ui.TypeSelectActivity;
import com.example.bsproperty.utils.AccBeanDaoUtils;
import com.example.bsproperty.utils.NewBeanDaoUtils;
import com.example.bsproperty.utils.TypeBeanDaoUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment03 extends BaseFragment {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    @BindView(R.id.rb_time_01)
    RadioButton rbTime01;
    @BindView(R.id.rb_time_02)
    RadioButton rbTime02;
    @BindView(R.id.tv_date01)
    TextView tvDate01;
    @BindView(R.id.tv_date02)
    TextView tvDate02;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_typemore)
    TextView getTypemore;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    @BindView(R.id.ln_02)
    LinearLayout ln_02;
    private Fragment03.MyAdapter adapter;
    private ArrayList<TypeBean> outtypes = new ArrayList<>();
    private ArrayList<TypeBean> intypes = new ArrayList<>();
    private TypeBean selectType;
    private ArrayList<NewList> mData = new ArrayList<>();
    private  NewBeanDaoUtils utils;
    private  TypeBeanDaoUtils typeBeanDaoUtils;
    private  AccBeanDaoUtils accBeanDaoUtils ;

    public class NewList {
        private String type;
        private String acc;
        private String addrr;
        private int flag;
        private String money;
        private Long time;
        private Long id;

        public NewList(long id,String type, String acc, String addrr, int flag, String money, Long time) {
            this.id=id;
            this.type = type;
            this.acc = acc;
            this.addrr = addrr;
            this.flag = flag;
            this.money = money;
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAcc() {
            return acc;
        }

        public void setAcc(String acc) {
            this.acc = acc;
        }

        public String getAddrr() {
            return addrr;
        }

        public void setAddrr(String addrr) {
            this.addrr = addrr;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTime() {
            SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fo.format(new Date(time));
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @Override
    protected void loadData() {
        outtypes = MyApplication.getInstance().getOuttypes();
        intypes = MyApplication.getInstance().getIntypes();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyApplication.getInstance().getTypeList();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
      utils = new NewBeanDaoUtils(getActivity());
      typeBeanDaoUtils = new TypeBeanDaoUtils(mContext);
      accBeanDaoUtils = new AccBeanDaoUtils(mContext);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_03;
    }


    @OnClick({R.id.rb_time_01, R.id.rb_time_02, R.id.tv_date01, R.id.tv_date02, R.id.tv_typemore, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_time_01:
                ln_02.setVisibility(View.GONE);
                break;
            case R.id.rb_time_02:
                ln_02.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_date01:
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int monthOfYear = 0;
                int dayOfMonth = 1;
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                            tvDate01.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                            tvDate01.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                            tvDate01.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } else {
                            tvDate01.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }
                }, year, monthOfYear, dayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.tv_date02:
                final Calendar c2 = Calendar.getInstance();
                int year2 = c2.get(Calendar.YEAR);
                int monthOfYear2 = c2.get(Calendar.MONTH);
                int dayOfMonth2 = c2.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                            tvDate02.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                            tvDate02.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                            tvDate02.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } else {
                            tvDate02.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }
                }, year2, monthOfYear2, dayOfMonth2);
                datePickerDialog2.show();
                break;
            case R.id.tv_typemore:
                startActivityForResult(new Intent(mContext, TypeSelectActivity.class), 521);
                break;
            case R.id.btn_add:
                getData();
                slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadData();
                        slList.setRefreshing(false);
                    }
                });
                rvList.setLayoutManager(new LinearLayoutManager(mContext));
                adapter = new Fragment03.MyAdapter(mContext, R.layout.item_fr03_list, mData);
                adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, Object item, final int position) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("删除明细")
                                .setMessage("是否删除当前明细？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Long id=mData.get(position).getId();
                                        boolean suc=utils.deleteTest(utils.queryTestById(id));
                                        if (suc){
                                            Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(mContext, "删除失败！", Toast.LENGTH_SHORT).show();
                                        }
                                        getData();
                                        adapter.notifyDataSetChanged(mData);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });
                rvList.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 521:
                    boolean flag = data.getBooleanExtra("flag", false);
                    int pos = data.getIntExtra("pos", 0);
                    if (flag) {
                        tvType.setText("支出-" + outtypes.get(pos).getType());
                        tvType.setTextColor(0xFF68CF6A);
                        selectType = outtypes.get(pos);
                    } else {
                        tvType.setText("收入-" + intypes.get(pos).getType());
                        tvType.setTextColor(0xFFDE3E2C);
                        selectType = intypes.get(pos);
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case 521:
                    selectType = null;
                    tvType.setTextColor(0xff666666);
                    tvType.setText("不限");
                    break;
            }
        }

    }

    public void getData() {
        mData = new ArrayList<>();
        List<NewBean> mbs = new ArrayList<>();
        if (rbTime01.isChecked() && selectType == null) {
            mbs = utils.queryTestByNativeSql("ORDER BY TIME DESC",
                    new String[]{});
            for (NewBean nbs : mbs) {
                TypeBean type = typeBeanDaoUtils.queryTestById(nbs.getTypeId());
                AccBean accBean = accBeanDaoUtils.queryTestById(nbs.getAccId());
                NewList nl = new NewList(nbs.getId(),type.getType(),
                        accBean.getAccount(),
                        nbs.getAddress(),
                        type.getFlag(),
                        "" + nbs.getMoney(), nbs.getTime());
                mData.add(nl);
            }

        } else if (rbTime01.isChecked() && selectType != null) {
            mbs = utils.queryTestByNativeSql("where TYPE_ID = ? ORDER BY TIME DESC",
                    new String[]{selectType.getId() + ""});
            for (NewBean nbs : mbs) {
                AccBean accBean = accBeanDaoUtils.queryTestById(nbs.getAccId());
                NewList nl = new NewList(nbs.getId(),selectType.getType(),
                        accBean.getAccount(),
                        nbs.getAddress(),
                        selectType.getFlag(),
                        "" + nbs.getMoney(), nbs.getTime());
                mData.add(nl);
            }

        } else if (!rbTime01.isChecked() && selectType == null) {
            try {
                mbs = utils.queryTestByNativeSql("where TIME >= ? AND TIME <=? ORDER BY TIME DESC",
                        new String[]{format.parse(tvDate01.getText() + "").getTime() + "",
                                format.parse(tvDate02.getText() + "").getTime() + ""});
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (NewBean nbs : mbs) {
                TypeBean type = typeBeanDaoUtils.queryTestById(nbs.getTypeId());
                AccBean accBean = accBeanDaoUtils.queryTestById(nbs.getAccId());
                NewList nl = new NewList(nbs.getId(),type.getType(),
                        accBean.getAccount(),
                        nbs.getAddress(),
                        type.getFlag(),
                        "" + nbs.getMoney(), nbs.getTime());
                mData.add(nl);
            }
        } else if (!rbTime01.isChecked() && selectType != null) {
            try {
                mbs = utils.queryTestByNativeSql("where TYPE_ID = ? AND TIME >=? AND TIME<=? ORDER BY TIME DESC",
                        new String[]{selectType.getId() + "", format.parse(tvDate01.getText() + "").getTime() + "",
                                format.parse(tvDate02.getText() + "").getTime() + ""});
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (NewBean nbs : mbs) {
                AccBean accBean = accBeanDaoUtils.queryTestById(nbs.getAccId());
                NewList nl = new NewList(nbs.getId(),selectType.getType(),
                        accBean.getAccount(),
                        nbs.getAddress(),
                        selectType.getFlag(),
                        "" + nbs.getMoney(), nbs.getTime());
                mData.add(nl);
            }
        }
    }

    private class MyAdapter extends BaseAdapter<NewList> {

        public MyAdapter(Context context, int layoutId, ArrayList<NewList> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, NewList type, int position) {
            holder.setText(R.id.tv_01, type.getType()).setText(R.id.tv_03, "消费地点：" + type.getAddrr())
                    .setText(R.id.tv_04, "账户：" + type.getAcc())
                    .setText(R.id.tv_05, "消费时间：" + type.getTime());
            if (type.getFlag() == 0) {
                holder.setText(R.id.tv_02, "-" + type.getMoney() + "元");
            } else {
                holder.setText(R.id.tv_02, "+" + type.getMoney() + "元");
            }
        }
    }
}
