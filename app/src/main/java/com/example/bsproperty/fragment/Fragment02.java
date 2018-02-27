package com.example.bsproperty.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.bsproperty.R;
import com.example.bsproperty.bean.Test;
import com.example.bsproperty.utils.TestDaoUtils;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment02 extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BindView(R.id.pc_chart)
    PieChart mChart;

    protected String[] mParties = new String[]{
            "给美特买吃的", "给喔特买本本", "吃的", "喝的"
    };
    TestDaoUtils testDaoUtils;

    @Override
    protected void loadData() {
        setData();

        testDaoUtils = new TestDaoUtils(getActivity());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
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
                switch ((int) h.getX()) {
                    case 0:
                        testDaoUtils.insertTest(new Test(null, "Google",
                                "http://7xi8d6.48096_n.jpg"));
                        break;
                    case 1:
                        List<Test> testList = testDaoUtils.queryAllTest();
                        for (Test test : testList) {
                            Log.i("hdd", test.toString());
                        }
                        break;
                }
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
}
