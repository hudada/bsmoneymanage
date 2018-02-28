package com.example.bsproperty.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsproperty.R;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.ui.TypeSelectActivity;
import com.example.bsproperty.utils.NewBeanDaoUtils;
import com.example.bsproperty.utils.SpUtils;
import com.example.bsproperty.utils.TypeBeanDaoUtils;
import com.example.bsproperty.view.ModifyItemDialog;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment02 extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BindView(R.id.pc_chart)
    PieChart mChart;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.tv_now)
    TextView tv_now;
    @BindView(R.id.btn_load)
    Button btn_load;
    private NewBeanDaoUtils newDao;
    private TypeBeanDaoUtils typeDao;
    private ArrayList<NewBean> monthAll = new ArrayList<>();
    private Double nowValue = 0.0;
    protected String[] mParties = new String[]{
            "给美特买吃的", "给喔特买本本", "吃的", "喝的"
    };

    @Override
    protected void loadData() {
        tvValue.setText(SpUtils.getBudget(mContext) + "");
        getNowValue();
//        getFlagTypeData(0);
        getFlagTypeData(1);
        getTimeData(0);
        setData();

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        newDao = new NewBeanDaoUtils(mContext);
        typeDao = new TypeBeanDaoUtils(mContext);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });


        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setXOffset(0f);

        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(12f);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_02;
    }

    private void setData() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        entries.add(new PieEntry(35f,
                mParties[0]));
        entries.add(new PieEntry(22f,
                mParties[1]));
        entries.add(new PieEntry(28f,
                mParties[2]));
        entries.add(new PieEntry(15f,
                mParties[3]));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.rgb(192, 255, 140));
        colors.add(Color.rgb(255, 247, 140));
        colors.add(Color.rgb(255, 208, 140));
        colors.add(Color.rgb(140, 234, 255));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @OnClick({R.id.tv_value, R.id.btn_load})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_value:
                new ModifyItemDialog(mContext)
                        .setTitle("预算设置")
                        .setMessage("请输入本月预算：")
                        .setCancelClick("取消", null)
                        .setOkClick("确认", new ModifyItemDialog.OnOkClickListener() {
                                    @Override
                                    public void onOkClick(String etStr) {
                                        if (!TextUtils.isEmpty(etStr)) {
                                            SpUtils.setBudget(mContext, Double.parseDouble(etStr));
                                            Toast.makeText(mContext, "预算设置成功！", Toast.LENGTH_SHORT).show();
                                            tvValue.setText(etStr);
                                        } else {
                                            Toast.makeText(mContext, "请输入正确的预算金额！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        ).show();
                break;
            case R.id.btn_load:
                startActivityForResult(new Intent(mContext, TypeSelectActivity.class), 521);
                break;
        }
    }

    public void getNowValue() {
        nowValue=0.0;
        monthAll=new ArrayList<>();
        //1号-31号
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR) - 1900;
        int dayCount = c.get(Calendar.DATE);

        int monthOfYear = c.get(Calendar.MONTH);
        Date date1 = new Date(year, monthOfYear, 1, 0, 0, 0);
        Date date2 = new Date(year, monthOfYear, dayCount, 23, 59, 59);
        Log.e("test", "dayCount:" + dayCount + "**monthOfYear:" + monthOfYear);
        List<NewBean> all = newDao.queryAll();
        for (NewBean n : all) {
            if (n.getTime() >= date1.getTime() && n.getTime() <= date2.getTime()) {
                monthAll.add(n);
            }
        }
        List<TypeBean> t = typeDao.queryAll();
        ArrayList<Long> ints = new ArrayList<>();
        for (TypeBean n : t) {
            if (n.getFlag() == 0) {
                ints.add(n.getId());
            }
        }
        for (NewBean n : monthAll) {
            if (ints.contains(n.getTypeId())) {
                nowValue += n.getMoney();
            }
        }
        tv_now.setText(nowValue + "");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (newDao != null && typeDao != null) {
            getNowValue();
        }
    }

    public ArrayList<ArrayList<NewBean>> getFlagTypeData(int flag) {
        List<TypeBean> t = typeDao.queryAll();
        ArrayList<Long> ints = new ArrayList<>();
        List<NewBean> da = newDao.queryAll();
        ArrayList<NewBean> da2 = new ArrayList<>();
        ArrayList<NewBean> lla;
        ArrayList<ArrayList<NewBean>> datalist = new ArrayList<>();
        for (TypeBean n : t) {
            if (n.getFlag() == flag) {
                ints.add(n.getId());
            }
        }

        for (NewBean n : da) {
            if (ints.contains(n.getTypeId())) {
                da2.add(n);
            }
        }

        for (Long l : ints) {
            lla = new ArrayList<>();
            for (NewBean n : da2) {
                if (l == n.getTypeId()) {
                    lla.add(n);
                }
            }
            datalist.add(lla);
        }
        return datalist;
    }


    public ArrayList<ArrayList<NewBean>>  getTimeData(int flag) {
        List<TypeBean> t = typeDao.queryAll();
        ArrayList<Long> ints = new ArrayList<>();
        List<NewBean> da = newDao.queryAll();
        ArrayList<NewBean> da2 = new ArrayList<>();
        ArrayList<ArrayList<NewBean>> datalist = new ArrayList<>();
        ArrayList<NewBean> lla;
        for (TypeBean n : t) {
            if (n.getFlag() == flag) {
                ints.add(n.getId());
            }
        }

        for (NewBean n : da) {
            if (ints.contains(n.getTypeId())) {
                da2.add(n);
            }
        }
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR) - 1900;
        for (int month=0;month<=11;month++){
            lla=new ArrayList<>();
            Date date1 = new Date(year, month, 1, 0, 0, 0);
            Date date2 = new Date(year, month+1, 1, 0, 0, 0);
            lla = new ArrayList<>();
            for (NewBean n : da2) {
                if (n.getTime() >= date1.getTime() && n.getTime() <date2.getTime()) {
                    lla.add(n);
                }
            }
            datalist.add(lla);
        }

    return  datalist;
    }
}
