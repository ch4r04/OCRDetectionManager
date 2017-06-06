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

import activity.OCRCensusActivity;
import model.ParserOCRTestData;
import utils.ByteDisposeUtil;
import utils.NetworkUtils;

/**
 * Created by ch4r0n on 2017/6/1.
 */

public class OCRTestParserTask extends BaseAsyncTask<byte[], Integer, Boolean> {

    private Context context;

    private byte[] responseData;

    private NumberProgressBar lsProcessBar;

    public OCRTestParserTask(Response.Listener<Boolean> listener, Context context, byte[] data) {
        super(listener, context);

        this.context = context;

        this.responseData = data;

        lsProcessBar = (NumberProgressBar) ((OCRCensusActivity)context).findViewById(R.id.ocrcensus_progressbar);

    }

    @Override
    protected Response<Boolean> doInBackground(byte[]... params) {
//        return null;

        byte[] data = responseData;

        String header = ByteDisposeUtil.toHex(data,0,4).toString();
        String trail = ByteDisposeUtil.toHex(data,data.length - 4,data.length).toString();
        Log.e("VAILDDATA->",header+trail);
        if (header.equals("ffffeeee") == false || trail.equals("eeeeffff") == false){
            Log.e("JUDGE","this is a unvaild data");
            return Response.success(false);
        }

        List<Entry> entries = new ArrayList<Entry>();
        //获取追踪点
        int point_count = Integer.parseInt(ByteDisposeUtil.toHex(data, 8, 12).toString(),16);
        //获取数据点
        float key_point = 0.000f;
        for (int i = 8;i < data.length - 4; i += 4){
            if (isCancelled()){
                break;
            }
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
        ParserOCRTestData parserOCRTestData = ParserOCRTestData.initAllAttri(d,point_count);

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
        if (isCancelled())
            return;
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
