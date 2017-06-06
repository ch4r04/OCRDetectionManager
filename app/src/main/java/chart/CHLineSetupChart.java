package chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.highlight.Highlight;

import adapters.XYMarkerView;
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

        //设置pieChart图表的描述
        this.getDescription().setEnabled(false);

        //设置是否可以触摸，如为false，则不能拖动，缩放等
        this.setTouchEnabled(true);

        //设置是否可以拖拽，缩放
        this.setDragEnabled(true);
        this.setScaleEnabled(true);

        //设置高亮状态能否开启
        this.setHighlightPerDragEnabled(false);
        this.setHighlightPerTapEnabled(false);

        // 设置能否扩大缩小
        this.setPinchZoom(false);

        //是否启用网格背景
        this.setDrawGridBackground(false);
        //设置dp中的最大高光距离。图表上的点击距离比该距离更远的条目不会触发高亮
        this.setMaxHighlightDistance(300);

        //XY轴的单位描述
        this.setXYDesc("km", "dB");
        //XY轴单位描述的字体颜色
        this.getXYDesc().setTextColor(Color.BLUE);

        //设置X轴样式
        XAxis x = this.getXAxis();
        x.setTextColor(Color.RED);
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.BLACK);
        //X轴的单位显示样式
        x.setValueFormatter(new MyXAxisValueFormatter());

        YAxis y = this.getAxisLeft();
//        y.setTypeface(mTfLight);
        //设置标签的最大值
//        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.BLACK);
        //Y轴的单位显示样式
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



    }

    public void setDescriptionHighLighter(Context context,float x, int dataSetIndex, int stackIndex){
        this.highlightValue(new Highlight(x, dataSetIndex, stackIndex));
        XYMarkerView mv = new XYMarkerView(context, new MyXAxisValueFormatter());
        mv.setChartView(this);
        this.setMarker(mv);

    }
}
