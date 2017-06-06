package tasks;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.bysj.ch4r0n.ocrdetectionmanager.R;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import activity.LineSetupActivity;
import model.ParserLineSetupData;
import utils.ByteDisposeUtil;
import utils.NetworkUtils;


/**
 * Created by charon on 2017/5/25.
 */

public class LineSetupParserTask extends BaseAsyncTask<byte[],Integer, Boolean> {

    private Context context;

    private byte[] responseData;

    private NumberProgressBar lsProcessBar;


    public LineSetupParserTask(Response.Listener<Boolean> listener, Context context, byte[] responseData) {
        super(listener, context);

        this.context = context;

        this.responseData = responseData;

        lsProcessBar = (NumberProgressBar) ((LineSetupActivity)context).findViewById(R.id.linesetup_progressbar);
    }



    @Override
    protected Response<Boolean> doInBackground(byte[]... params) {

        byte[] data = responseData;
        //获取接收到的数据
        //判断数据头和尾部是否合法
        String header = ByteDisposeUtil.toHex(data,0,4).toString();
        String trail = ByteDisposeUtil.toHex(data,data.length - 4,data.length).toString();
        Log.e("VAILDDATA->",header+trail);
        if (header.equals("ffffeeee") == false || trail.equals("eeeeffff") == false){
            Log.e("JUDGE","this is a unvaild data");
            return Response.success(false);
        }

        List<Entry> entries = new ArrayList<Entry>();
        //获取追踪点
        String TP = Integer.parseInt(ByteDisposeUtil.toHex(data, data.length - 8, data.length - 4).toString(),16) + "";
        //获取数据点
        float key_point = 0.000f;
        Log.e("INTPOINT", NetworkUtils.bytesToInt2(ByteDisposeUtil.byteInRange(data,12,16),0) +"");
        float value_point = 0.0f;
        for (int i = 8;i < data.length - 8; i += 4){
            if (isCancelled()){
               break;
            }
            byte []a = ByteDisposeUtil.byteInRange(data, i, i + 4);
            value_point = (float) (NetworkUtils.bytesToInt2(ByteDisposeUtil.byteInRange(data, i, i + 4),0) / 1000000.0);
            entries.add(new BarEntry(key_point,value_point));
            key_point += 0.001f;
            float mprofloat = i / (data.length - 8.00f) * 100;
            int mproint = Math.round(mprofloat);
            //通过这里传递参数到下面更新progress
            publishProgress(mproint);
        }
        Log.e("LengthOK:",entries.size()+"");

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

        ParserLineSetupData parserLineSetupData = ParserLineSetupData.initWithParserData("",TP,entries,d);
        return Response.success(true);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lsProcessBar.setVisibility(View.VISIBLE);
        //初始化一下 从零开始
        lsProcessBar.setProgress(0);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (isCancelled()){
            return;
        }
        lsProcessBar.setProgress(values[0]);
    }

    @Override
    protected void onCancelled(Response<Boolean> booleanResponse) {
        super.onCancelled(booleanResponse);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Response<Boolean> response) {
        super.onPostExecute(response);
        lsProcessBar.setVisibility(View.GONE);
    }
}
