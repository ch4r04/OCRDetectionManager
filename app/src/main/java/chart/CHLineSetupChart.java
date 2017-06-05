package chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.highlight.Highlight;

import activity.XYMarkerView;
import adapters.MyXAxisValueFormatter;
import adapters.MyYAxisValueFormatter;

/**
 * Created by ch4r0n on 2017/5/7.
 */

/**
 * @name 光缆监测管理系统
 * @package_name name：chart
 * @class_name CHLineSetupChart
 * @description 图表显示类 线路设置图表
 * @anthor ch4r0n QQ:609461975
 * @time 2017/5/7 13:37
 * @change
 * @chang 13:37
 */
public class CHLineSetupChart extends LineChart {
    public CHLineSetupChart(Context context) {
        super(context);
        initMyChart();
    }

    public CHLineSetupChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyChart();

    }

    public CHLineSetupChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMyChart();
    }

    private void initMyChart(){
        //设置图表的偏移量
        this.setViewPortOffsets(0, 0, 0, 0);
        //设置图表背景色
        this.setBackgroundColor(Color.WHITE);

        // no description text
        this.getDescription().setEnabled(false);

        // enable touch gestures
        this.setTouchEnabled(true);

        // enable scaling and dragging
        this.setDragEnabled(true);
        this.setScaleEnabled(true);

        //设置高亮状态能否开启
        this.setHighlightPerDragEnabled(false);
        this.setHighlightPerTapEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        this.setPinchZoom(false);

        this.setDrawGridBackground(false);
        this.setMaxHighlightDistance(300);

        //XY轴的描述
        //add XY Description in the chart
        this.setXYDesc("km", "dB");
        this.getXYDesc().setTextColor(Color.BLUE);

        //设置X轴
        XAxis x = this.getXAxis();
        x.setTextColor(Color.RED);
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.BLACK);
        x.setValueFormatter(new MyXAxisValueFormatter());

        YAxis y = this.getAxisLeft();
//        y.setTypeface(mTfLight);
        //设置标签的最大值
//        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.BLACK);
        y.setValueFormatter(new MyYAxisValueFormatter());


        this.getAxisRight().setEnabled(false);

        this.getLegend().setEnabled(true);

    }

    public void setRightTopLegendDes(String tracePointLoc, String mTime, String countDown){
        Legend l = this.getLegend();
        l.setFormSize(12f);// set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(0f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(0f); // set the space between the legend entries on the y-axis

//         set custom labels and colors
        LegendEntry l1 = new LegendEntry("追踪点位置: " + tracePointLoc + " km", Legend.LegendForm.NONE,10f,10f,new DashPathEffect(new float[]{10f,5f},0f),Color.BLACK);
        LegendEntry l2 = new LegendEntry("测量时间: " + mTime + " s", Legend.LegendForm.NONE,10f,10f,new DashPathEffect(new float[]{10f,5f},0f),Color.BLACK);
        LegendEntry l3 = new LegendEntry("倒计时: "+ countDown +" s", Legend.LegendForm.NONE,10f,10f,new DashPathEffect(new float[]{10f,5f},0f),Color.BLACK);
        LegendEntry[] entries = new LegendEntry[]{l1,l2,l3};
        l.setCustom(entries);

        Log.e("Entry", l.getEntries()[2].label);

    }

    public void setDescriptionHighLighter(Context context,float x, int dataSetIndex, int stackIndex){
        this.highlightValue(new Highlight(x, dataSetIndex, stackIndex));
        XYMarkerView mv = new XYMarkerView(context, new MyXAxisValueFormatter());
        mv.setChartView(this);
        this.setMarker(mv);

    }
}
