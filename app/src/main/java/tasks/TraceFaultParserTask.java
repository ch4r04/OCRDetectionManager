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
import activity.TraceActivity;
import model.ParserLineSetupData;
import model.ParserTraceFaultData;
import utils.ByteDisposeUtil;
import utils.NetworkUtils;

/**
 * Created by ch4r0n on 2017/6/1.
 */

public class TraceFaultParserTask extends BaseAsyncTask<byte[],Integer, Integer> {

    private Context context;

    private byte[] responseData;

    private NumberProgressBar lsProcessBar;


    public TraceFaultParserTask(TraceActivity listener, Context context, byte[] responseData) {
        super(listener, context);

        this.context = context;

        this.responseData = responseData;

        lsProcessBar = (NumberProgressBar) ((TraceActivity)context).findViewById(R.id.tracefault_progressbar);
    }

    @Override
    protected Response<Integer> doInBackground(byte[]... params) {

        byte[] data = responseData;
        //获取接收到的数据
        //判断数据头和尾部是否合法
        String header = ByteDisposeUtil.toHex(data,0,4).toString();
        String trail = ByteDisposeUtil.toHex(data,data.length - 4,data.length).toString();
        Log.e("VAILDDATA->",header+trail);
        if (header.equals("ffffeeee") == false || trail.equals("eeeeffff") == false){
            Log.e("JUDGE","this is a unvaild data");
            return Response.success(-1);
        }

        List<Entry> entries = new ArrayList<Entry>();
        //获取追踪点
        int point_count = Integer.parseInt(ByteDisposeUtil.toHex(data, 8, 12).toString(),16);
        //获取数据点
        float key_point = 0.000f;
        for (int i = 8;i < data.length - 4; i += 4){
            float value_point = (float) (NetworkUtils.bytesToInt2(ByteDisposeUtil.byteInRange(data, i, i + 4),0) / 1000000.0);
            entries.add(new BarEntry(key_point,value_point));
            key_point += 0.001f;
            //计算进度 更新进度条
            float mprofloat = i / (data.length - 4.00f) * 100;
            int mproint = Math.round(mprofloat);
            publishProgress(mproint);
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
        //设置回调参数
        ParserTraceFaultData pd = ParserTraceFaultData.initAllAttri(d,point_count);
        //返回 1 是标识进行的操作是故障追踪 本功能与模板获取在同一activity，回调时根据该标识判断
        // 2 是标识进行模板获取
        return Response.success(1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lsProcessBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        lsProcessBar.setProgress(values[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Response<Integer> response) {
        super.onPostExecute(response);
        lsProcessBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCancelled(Response<Integer> integerResponse) {
        super.onCancelled(integerResponse);
    }
}
