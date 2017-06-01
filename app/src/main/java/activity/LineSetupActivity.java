package activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.ch4r0n.ocrdetectionmanager.R;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientReceiveDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

import java.util.ArrayList;
import java.util.List;

import chart.CHLineSetupChart;
import es.dmoral.toasty.Toasty;
import helper.CommonHelper;
import model.LineSetupModel;
import model.ParserLineSetupData;
import socket.CHSocketClient;
import socket.FrameDataSocket;
import tasks.LineSetupParserTask;
import tasks.Response;
import utils.ByteDisposeUtil;
import utils.CHToast;
import utils.NetworkUtils;

/**
 * Created by xingr on 2017/5/1.
 */

public class LineSetupActivity extends BaseActivity implements OnChartValueSelectedListener,View.OnClickListener,TextWatcher,SocketClientReceiveDelegate, Response.Listener<Boolean>{

    ParserLineSetupData parserLineSetupData;

    LineSetupModel lineSetupModel;

    private CHLineSetupChart mChart;

    //添加子线程任务 异步进行
    LineSetupParserTask linesetupTask;

    CHSocketClient chSocketClient;

    //获取所有控件
    TextView wavelengthTView;
    EditText refractiveIndexEdit;
    Spinner distanceSpinner;
    Spinner pulseWidthSpinner;
    Spinner mtimeSpinner;
    EditText trackpointLocEdit;
    Button sureBtn;
    Button recoverBtn;
    Button confimBtn;
    Button cancelBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linesetup);

        //控件绑定
        wavelengthTView = (TextView) findViewById(R.id.tview_wavelength);
        refractiveIndexEdit = (EditText) findViewById(R.id.edit_refractive_index);
        distanceSpinner = (Spinner) findViewById(R.id.sp_distance);
        pulseWidthSpinner = (Spinner) findViewById(R.id.sp_pulse_width);
        mtimeSpinner = (Spinner) findViewById(R.id.sp_mtime);
        trackpointLocEdit = (EditText) findViewById(R.id.edit_trackpointloc);
        sureBtn = (Button) findViewById(R.id.btn_sure);
        recoverBtn = (Button) findViewById(R.id.btn_recover);
        confimBtn = (Button) findViewById(R.id.btn_confimsetting);
        cancelBtn = (Button) findViewById(R.id.btn_cancelsetting);
        //设置监听
        sureBtn.setOnClickListener(this);
        recoverBtn.setOnClickListener(this);
        confimBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        refractiveIndexEdit.addTextChangedListener(this);


        //初始化和设置mchart
        mChart = (CHLineSetupChart) findViewById(R.id.line_setup_chart);
        mChart.setOnChartValueSelectedListener(this);

        chSocketClient = CHSocketClient.getCHSocketClient();
        chSocketClient.getSocketClient().registerSocketClientReceiveDelegate(this);


    }



    @Override
    protected void onResume() {

        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        super.onResume();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //开始获取模板
            case R.id.btn_sure: {

                String str_wavelength = wavelengthTView.getText().toString();
                String str_distance = distanceSpinner.getSelectedItem().toString();
                String str_pulseWidth = pulseWidthSpinner.getSelectedItem().toString();
                String str_mtime = mtimeSpinner.getSelectedItem().toString();

                //获取信息
                int int_wavelength = Integer.parseInt(str_wavelength.substring(0, str_wavelength.indexOf(" ")));
                float fl_refractive = Float.parseFloat(refractiveIndexEdit.getText().toString());
                int int_gi = (int)(fl_refractive * 10000);
                int int_distance = Integer.parseInt(str_distance.substring(0, str_distance.indexOf(" ")));
                int int_pulseWidth = Integer.parseInt(str_pulseWidth.substring(0, str_pulseWidth.indexOf(" ")));
                int int_mtime = Integer.parseInt(str_mtime.substring(0, str_mtime.indexOf(" ")));

                lineSetupModel = LineSetupModel.initWithAllContribute(int_wavelength,fl_refractive,int_distance,int_pulseWidth,int_mtime,0);

                chSocketClient.getSocketClient().sendData(FrameDataSocket.sendLineSetupData(int_distance,int_wavelength,int_pulseWidth,int_mtime,int_gi));
                Toasty.info(this,"正在获取数据").show();
                break;
            }
            //恢复默认设置
            case R.id.btn_recover:{
                refractiveIndexEdit.setText("1.4685");
                distanceSpinner.setSelection(0,true);
                pulseWidthSpinner.setSelection(0,true);
                mtimeSpinner.setSelection(2,true);
                break;
            }
            //确认设置 返回上一界面
            case R.id.btn_confimsetting:
            {
                //字符安全检查
                if (trackpointLocEdit.getText().toString().isEmpty()){
                    Toast.makeText(this,"未获取模板 不能确认数据！",Toast.LENGTH_SHORT).show();
                    break;
                }
                setResult(CommonHelper.RESULT_TRACE_CONFIRM);
                finish();
                break;
            }
            case R.id.btn_cancelsetting:
                setResult(CommonHelper.RESULT_TRACE_CANCEL);
                finish();
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().isEmpty()){
           return;
        }

        String sequenceStr = s.toString();
        if (sequenceStr.endsWith(".")){
            sequenceStr = sequenceStr.substring(0,sequenceStr.length() - 1);
        }
        Log.e("str",sequenceStr);
        float fText = Float.parseFloat(sequenceStr);
        if (fText > 1.9999f){
            refractiveIndexEdit.setText("1.9999");
        }
        if (fText < 1.0000f){
            refractiveIndexEdit.setText("1.0000");
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
        linesetupTask = new LineSetupParserTask(this, this, responsePacket.getData());
        linesetupTask.execute();
    }

    @Override
    public void onHeartBeat(SocketClient socketClient) {

    }

    @Override
    public void finish() {
        super.finish();
        chSocketClient.getSocketClient().removeSocketClientReceiveDelegate(this);
    }

    @Override
    public void onResponse(Boolean result) {
        if (result == true){
            //更新图表
            parserLineSetupData = ParserLineSetupData.getInstance();
            Toasty.success(getApplicationContext(),"数据解析成功").show();
            lineSetupModel.setTracePointLoc(Integer.parseInt(parserLineSetupData.getTracingPoint()));
            trackpointLocEdit.setText(parserLineSetupData.getTracingPoint());

            //使用该数据 初始化一个图表
            mChart.setData(parserLineSetupData.getLineData());
            //添加追踪点 在得到初始化数据之后显示高亮
            mChart.setDescriptionHighLighter(getApplicationContext(),Integer.parseInt(parserLineSetupData.getTracingPoint().toString()) / 1000.0f,0,0);
            //添加右上角描述
            mChart.setRightTopLegendDes(Integer.parseInt(parserLineSetupData.getTracingPoint().toString()) / 1000.0f + "", lineSetupModel.getmTime()+"",lineSetupModel.getmTime()+"");
            mChart.animateX(2000);
            mChart.invalidate();
        }
    }

    @Override
    public void onErrorResponse(Exception exception) {

    }

}
