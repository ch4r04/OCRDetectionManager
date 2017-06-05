package activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.ch4r0n.ocrdetectionmanager.R;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientReceiveDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

import chart.CHOCRCensusChart;
import es.dmoral.toasty.Toasty;
import helper.CommonHelper;
import model.LineSetupModel;
import model.ParserLineSetupData;
import model.ParserOCRTestData;
import socket.CHSocketClient;
import socket.FrameDataSocket;
import tasks.OCRTestParserTask;
import tasks.Response;
import utils.CHToast;

/**
 * Created by xingr on 2017/5/1.
 */

public class OCRCensusActivity extends BaseActivity implements View.OnClickListener ,SocketClientReceiveDelegate, Response.Listener<Boolean> {

    /**
     * 是否启动标志
     */
    private boolean isStart = false;

    private Toast tipMsgToast;

    private ParserLineSetupData parserLineSetupData;

    private CHOCRCensusChart mOcrCensusChart;

    private int START_OCR_CENSUS = 0;

    CHSocketClient chSocketClient = CHSocketClient.getCHSocketClient();
    /**
     * 温馨提示:\n操作光线请按照 弯曲(保持1s)-恢复(保持1s)的方式循环进行
     */
    private TextView mTextView3;
    /**
     * 线路设置
     */
    private Button mBtnLinesetup;
    /**
     * 普查测试
     */
    private Button mBtnCensustest;
    /**
     * 追踪关联
     */
    private Button mBtnGotoTraceFault;
    /**
     * 帮助
     */
    private Button mBtnHelp;
    /**
     * 主页
     */
    private Button mBtnHomepage;

    /**
     * 停止测试
     */
    private Button mBtnStopCensusTest;

    OCRTestParserTask ocrTestParserTask;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO:OnCreate Method has been created, run FindViewById again to generate code
        setContentView(R.layout.activity_ocr_census);
        initView();

    }

    public void initView() {
        mOcrCensusChart = (CHOCRCensusChart) findViewById(R.id.ocr_census_chart);

        mTextView3 = (TextView) findViewById(R.id.textView3);
        mBtnLinesetup = (Button) findViewById(R.id.btn_linesetup);
        mBtnLinesetup.setOnClickListener(this);
        mBtnCensustest = (Button) findViewById(R.id.btn_censustest);
        mBtnCensustest.setOnClickListener(this);
        mBtnGotoTraceFault = (Button) findViewById(R.id.btn_goto_tracefault);
        mBtnGotoTraceFault.setOnClickListener(this);
        mBtnHelp = (Button) findViewById(R.id.btn_help);
        mBtnHelp.setOnClickListener(this);
        mBtnHomepage = (Button) findViewById(R.id.btn_homepage);
        mBtnHomepage.setOnClickListener(this);
        mBtnStopCensusTest = (Button) findViewById(R.id.btn_stopcensustest);
        mBtnStopCensusTest.setOnClickListener(this);



        if (parserLineSetupData.getInstance() != null){
//            Toasty.success(this,"已经完成线路设置，可以进行测试!").show();
            chSocketClient.getSocketClient().registerSocketClientReceiveDelegate(this);
            START_OCR_CENSUS = 1;
            mOcrCensusChart.setZeroData();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_linesetup:
                startActivityForResult(new Intent(this, LineSetupActivity.class), CommonHelper.ACTIVITY_GOIN_LINESETUP);
                chSocketClient.getSocketClient().removeSocketClientReceiveDelegate(this);
                break;
            case R.id.btn_censustest:
                //未设置线路
                if (START_OCR_CENSUS == 0){
                    CHToast.showShort(this, "未设置线路 无法普查测试");
                    isStart = false;
                    break;
                }
                //开始进行普查测试
                Toasty.info(getApplicationContext(),"开始进行普查测试 请手动操作光缆").show();
                //发送数据到服务端
                chSocketClient.getSocketClient().sendData(FrameDataSocket.sendStartCensusTest());
                isStart = true;
                break;
            case R.id.btn_goto_tracefault:
                //跳转到故障追踪界面
                startActivity(new Intent(this, TraceActivity.class));
                finish();
                break;
            case R.id.btn_help:
                break;
            case R.id.btn_homepage:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_stopcensustest:
                if (isStart){
                    Toasty.success(getApplicationContext(),"结束普查测试 等待结果").show();
                    Log.e("on","正在测试");
                    //发送结束指令
                    chSocketClient.getSocketClient().sendData(FrameDataSocket.sendStopCensusTest());
                    isStart = false;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        chSocketClient.getSocketClient().registerSocketClientReceiveDelegate(this);
        //得到确认的设置信息
        //设置图表
        System.out.println("-------------------------------" + requestCode + " " + resultCode);
        if (resultCode == CommonHelper.RESULT_TRACE_CONFIRM) {
            Log.e("common", "this is trace confim");
            START_OCR_CENSUS = 1;
            mOcrCensusChart.setZeroData();

        }
    }

    @Override
    public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
        Toasty.success(this,"获取数据完毕 等待解析").show();
        ocrTestParserTask = new OCRTestParserTask(this,this,responsePacket.getData());
        ocrTestParserTask.execute();

    }

    @Override
    public void onHeartBeat(SocketClient socketClient) {
    }


    @Override
    public void onResponse(Boolean result) {

        if (result){
            ParserOCRTestData testData = ParserOCRTestData.getInstance();
            mOcrCensusChart.setData(testData.getLineData());
            mOcrCensusChart.animateX(3000);
            mOcrCensusChart.invalidate();
        }

    }

    @Override
    public void onErrorResponse(Exception exception) {

    }

    @Override
    public void finish() {
        super.finish();
        chSocketClient.getSocketClient().removeSocketClientReceiveDelegate(this);
    }


}
