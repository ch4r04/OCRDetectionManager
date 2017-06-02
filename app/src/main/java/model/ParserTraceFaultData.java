package model;

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
 * Created by ch4r0n on 2017/5/14.
 */

public class ParserTraceFaultData {


    /**
     * 用于图形展示
     */
    private LineData lineData;

    /**
     * 追踪点位置
     */
    private int TNDP;



    private static ParserTraceFaultData instance = null;

    /**
     * 构造方法私有化
     */
    private ParserTraceFaultData(){}

    private ParserTraceFaultData(LineData ld, int tndp){
        this.lineData = ld;
        this.TNDP = tndp;
    }
    public static synchronized ParserTraceFaultData initParserTraceFaultData(byte[] data){
        if (instance == null)
            instance = new ParserTraceFaultData(data);
        return instance;
    }

    public static synchronized ParserTraceFaultData initAllAttri(LineData d, int tndp){
       if (instance == null)
           instance = new ParserTraceFaultData(d,tndp);
        return instance;

    }

    public static ParserTraceFaultData getInstance() {
        return instance;
    }

    /**
     * 返回数据格式 ffffeeee81000006 TNDP + point + eeeeffff
     * @param data
     */
    private ParserTraceFaultData(byte[] data){

        String header = ByteDisposeUtil.toHex(data,0,4).toString();
        String trail = ByteDisposeUtil.toHex(data,data.length - 4,data.length).toString();
        Log.e("VAILDDATA->",header+trail);
        if (header.equals("ffffeeee") == false || trail.equals("eeeeffff") == false){
            Log.e("JUDGE","this is a unvaild data");
            return;
        }

        List<Entry> entries = new ArrayList<Entry>();
        //获取追踪点
        int point_count = Integer.parseInt(ByteDisposeUtil.toHex(data, 8, 12).toString(),16);
        this.TNDP = point_count;
        //获取数据点
        float key_point = 0.000f;
        for (int i = 8;i < data.length - 4; i += 4){
            float value_point = (float) (NetworkUtils.bytesToInt2(ByteDisposeUtil.byteInRange(data, i, i + 4),0) / 1000000.0);
            entries.add(new BarEntry(key_point,value_point));
            key_point += 0.001f;
        }

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

    public LineData getLineData() {
        return lineData;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public int getTNDP() {
        return TNDP;
    }

    public void setTNDP(int TNDP) {
        this.TNDP = TNDP;
    }
}
