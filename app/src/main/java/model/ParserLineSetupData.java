package model;

/**
 * Created by ch4r0n on 2017/5/2.
 */

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import utils.ByteDisposeUtil;
import utils.NetworkUtils;

/**
 * @name ParserLineSetupData.java
 * @package model
 * @class name：ParserLineSetupData
 * @class 故障追踪解析过后的数据
 * @anthor ch4r0n QQ:609461975
 * @time 2017/5/2 下午9:30
 */
public class ParserLineSetupData {

    private String startPoint; //起始点

    private String tracingPoint; //追踪点

    private List<Entry> entryList;  //点数据

    private LineData lineData;      //点数据的坐标点形式 用来设置图表

    private static ParserLineSetupData instance;

    private ParserLineSetupData(){}

    private ParserLineSetupData(byte[] data){
        Log.e("data->", Integer.parseInt(ByteDisposeUtil.toHex(data,8,12).toString(),16)+"");
        //判断数据头和尾部是否合法
        String header = ByteDisposeUtil.toHex(data,0,4).toString();
        String trail = ByteDisposeUtil.toHex(data,data.length - 4,data.length).toString();
        Log.e("VAILDDATA->",header+trail);
        if (header.equals("ffffeeee") == false || trail.equals("eeeeffff") == false){
            Log.e("JUDGE","this is a unvaild data");
            return;
        }

        List<Entry> entries = new ArrayList<Entry>();
        //获取追踪点
        String TP = Integer.parseInt(ByteDisposeUtil.toHex(data, data.length - 8, data.length - 4).toString(),16) + "";
        //设置追踪点
        this.tracingPoint = TP;
        //获取数据点
        float key_point = 0.000f;
        Log.e("INTPOINT",NetworkUtils.bytesToInt2(ByteDisposeUtil.byteInRange(data,12,16),0) +"");
        float value_point = 0.0f;
        for (int i = 8;i < data.length - 8; i += 4){
            byte []a = ByteDisposeUtil.byteInRange(data, i, i + 4);
            value_point = (float) (NetworkUtils.bytesToInt2(ByteDisposeUtil.byteInRange(data, i, i + 4),0) / 1000000.0);
            entries.add(new BarEntry(key_point,value_point));
            key_point += 0.001f;
        }
        Log.e("LengthOK:",entries.size()+"");
        this.entryList = entries;

        //设置LineData
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        LineDataSet ds2 = new LineDataSet(entries,"追踪点位置: 15.223 km");
        ds2.setMode(LineDataSet.Mode.LINEAR);
        ds2.setCubicIntensity(0.2f);
        //set1.setDrawFilled(true);
        ds2.setDrawCircles(false);
        ds2.setLineWidth(1.8f);
        ds2.setCircleRadius(4f);
        ds2.setCircleColor(Color.BLUE);
        ds2.setHighLightColor(Color.rgb(244, 117, 117));
        ds2.setColor(Color.BLUE);
        ds2.setDrawValues(false);
        sets.add(ds2);
        LineData d = new LineData(sets);
        this.lineData = d;

    }
    private ParserLineSetupData(String spoint, String tracePoint, List<Entry> entries, LineData lineData){
        this.startPoint = spoint;
        this.tracingPoint = tracePoint;
        this.entryList = entries;
        this.lineData = lineData;
    }

    public static synchronized ParserLineSetupData initWithParserData(String spoint, String tracePoint, List<Entry> entries, LineData lineData){
        if (instance == null)
            instance = new ParserLineSetupData(spoint,tracePoint,entries,lineData);
        return instance;
    }

    public static synchronized ParserLineSetupData initParserLineSetupDataFromData(byte[] data){
        if (instance == null)
            instance = new ParserLineSetupData(data);
        return instance;
    }


    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getTracingPoint() {
        return tracingPoint;
    }

    public void setTracingPoint(String tracingPoint) {
        this.tracingPoint = tracingPoint;
    }

    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    public static ParserLineSetupData getInstance() {
        return instance;
    }

    public LineData getLineData() {
        return lineData;
    }
    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }
}
