package activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bysj.ch4r0n.ocrdetectionmanager.R;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientReceiveDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

import chart.CHTraceLineChart;
import helper.CommonHelper;
import model.LineSetupModel;
import model.ParserDataJudge;
import model.ParserGetTemplateData;
import model.ParserLineSetupData;
import model.ParserTraceFaultData;
import socket.CHSocketClient;
import socket.FrameDataSocket;
import utils.CHToast;

/**
 * Created by xingr on 2017/4/30.
 */

public class TraceActivity extends BaseActivity implements View.OnClickListener,SocketClientReceiveDelegate {


    /**
     * socket请求
     */
    CHSocketClient chSocketClient = CHSocketClient.getCHSocketClient();

    /**
     * 线路设置存储对象
     */
    LineSetupModel lineSetupModel;

    ParserLineSetupData parserLineSetupData;
    /**
     *  获取到的迹线设置
     */
    ParserGetTemplateData parserGetTemplateData;

    private CHTraceLineChart mTracingFaultChart;

    /**
     * 能否获取模板 标志
     */
    private int GET_TEMPLATE_FLAG = 0;

    /**
     * 能否开始故障追踪 标志
     */
    private int START_TRACE_FAULT = 0;
    /**
     * Y放大
     */
    private Button mBtnYzoomin;
    /**
     * Y缩小
     */
    private Button mBtnYzoomout;
    /**
     * X放大
     */
    private Button mBtnXzoomin;
    /**
     * X缩小
     */
    private Button mBtnZoomout;
    /**
     * XY原始
     */
    private Button mBtnXyfit;
    /**
     * 弯曲点与原点距离
     */
    private TextView mTextView2;
    private TextView mTvOrgDistance;
    /**
     * 弯曲点与追踪点距离
     */
    private TextView mTextView4;
    private TextView mTvTracepointDistance;
    /**
     * 线路设置
     */
    private Button mBtnLinesetup;
    /**
     * 模板获取
     */
    private Button mBtnGettemplate;
    /**
     * 故障追踪
     */
    private Button mBtnStarttrace;
    /**
     * 帮助
     */
    private Button mBtnHelp;
    /**
     * 主页
     */
    private Button mBtnHomepage;
    private LinearLayout mTraceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracingfault);
        initView();
    }


    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击线路设置按钮
            case R.id.btn_linesetup:
                startActivityForResult(new Intent(this, LineSetupActivity.class), CommonHelper.ACTIVITY_GOIN_LINESETUP);
                chSocketClient.getSocketClient().removeSocketClientReceiveDelegate(this);
                break;
            //点击获取模板
            case R.id.btn_gettemplate:
                //未设置线路 无法获取模板
                if (GET_TEMPLATE_FLAG == 0){
                    CHToast.showShort(this, "未设置线路 无法获取模板");
                    break;
                }
                chSocketClient.getSocketClient().sendData(FrameDataSocket.sendGetTemplate());
                //....
                break;
            case R.id.btn_starttrace:
                //未设置线路 无法追踪线路
                if (START_TRACE_FAULT == 0){
                    CHToast.showShort(this, "未设置线路 无法故障追踪");
                    break;
                }
                chSocketClient.getSocketClient().sendData(FrameDataSocket.sendStartTraceFault());
                break;
            case R.id.btn_help:

                break;
            case R.id.btn_homepage:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_yzoomin:
                mTracingFaultChart.zoom(1f,2f,13.240f,0f);
//                mTracingFaultChart.moveViewToX(13.240f);
                break;
            case R.id.btn_yzoomout:
                mTracingFaultChart.zoom(1f,0.5f,13.240f,0f);
                break;
            case R.id.btn_xzoomin:
                mTracingFaultChart.zoom(2f,1f,13.240f,0f);
                break;
            case R.id.btn_zoomout:
                mTracingFaultChart.zoom(0.5f,1f,13.240f,0f);
                break;
            case R.id.btn_xyfit:

                mTracingFaultChart.fitScreen();
                break;
            case R.id.tv_org_distance:
                break;
            case R.id.tv_tracepoint_distance:
                break;
            case R.id.tracing_fault_chart:
                break;
            case R.id.textView2:
                break;
            case R.id.textView4:
                break;
            case R.id.trace_layout:
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
            GET_TEMPLATE_FLAG = 1;
            START_TRACE_FAULT = 1;
            parserLineSetupData = ParserLineSetupData.getInstance();
            mTracingFaultChart.setData(parserLineSetupData.getLineData());
            mTracingFaultChart.setRightTopLegendDes(String.valueOf(LineSetupModel.getInstance().getTracePointLoc()/ 1000.0), String.valueOf(LineSetupModel.getInstance().getmTime()), String.valueOf(LineSetupModel.getInstance().getmTime()));
            mTracingFaultChart.animateX(2000);
            mTracingFaultChart.invalidate();
        }
    }


    private void initView() {
        mTracingFaultChart = (CHTraceLineChart) findViewById(R.id.tracing_fault_chart);
        mTracingFaultChart.setOnClickListener(this);
        mBtnYzoomin = (Button) findViewById(R.id.btn_yzoomin);
        mBtnYzoomin.setOnClickListener(this);
        mBtnYzoomout = (Button) findViewById(R.id.btn_yzoomout);
        mBtnYzoomout.setOnClickListener(this);
        mBtnXzoomin = (Button) findViewById(R.id.btn_xzoomin);
        mBtnXzoomin.setOnClickListener(this);
        mBtnZoomout = (Button) findViewById(R.id.btn_zoomout);
        mBtnZoomout.setOnClickListener(this);
        mBtnXyfit = (Button) findViewById(R.id.btn_xyfit);
        mBtnXyfit.setOnClickListener(this);
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView2.setOnClickListener(this);
        mTvOrgDistance = (TextView) findViewById(R.id.tv_org_distance);
        mTvOrgDistance.setOnClickListener(this);
        mTextView4 = (TextView) findViewById(R.id.textView4);
        mTextView4.setOnClickListener(this);
        mTvTracepointDistance = (TextView) findViewById(R.id.tv_tracepoint_distance);
        mTvTracepointDistance.setOnClickListener(this);
        mBtnLinesetup = (Button) findViewById(R.id.btn_linesetup);
        mBtnLinesetup.setOnClickListener(this);
        mBtnGettemplate = (Button) findViewById(R.id.btn_gettemplate);
        mBtnGettemplate.setOnClickListener(this);
        mBtnStarttrace = (Button) findViewById(R.id.btn_starttrace);
        mBtnStarttrace.setOnClickListener(this);
        mBtnHelp = (Button) findViewById(R.id.btn_help);
        mBtnHelp.setOnClickListener(this);
        mBtnHomepage = (Button) findViewById(R.id.btn_homepage);
        mBtnHomepage.setOnClickListener(this);
        mTraceLayout = (LinearLayout) findViewById(R.id.trace_layout);
        mTraceLayout.setOnClickListener(this);

        chSocketClient.getSocketClient().registerSocketClientReceiveDelegate(this);
    }


    @Override
    public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {

//        ParserDataJudge.parserData(responsePacket.getData());
        int resultFlag = ParserDataJudge.parserData(responsePacket.getData());
        if ( resultFlag == 1){
            //进行获取模板操作
            ParserGetTemplateData parserGetTemplateData = ParserGetTemplateData.initParserGetTemplateData(responsePacket.getData());

            mTracingFaultChart.setData(parserGetTemplateData.getLineData());
            //添加右上角描述
            mTracingFaultChart.setRightTopLegendDes(String.valueOf(LineSetupModel.getInstance().getTracePointLoc()/ 1000.0), String.valueOf(LineSetupModel.getInstance().getmTime()), String.valueOf(LineSetupModel.getInstance().getmTime()));
            mTracingFaultChart.animateX(2000);
            mTracingFaultChart.invalidate();

        }else if (resultFlag == 2){
            //进行追踪错误操作
            //进行获取模板操作
            ParserTraceFaultData parserTraceFaultData = ParserTraceFaultData.initParserTraceFaultData(responsePacket.getData());

            mTracingFaultChart.setData(parserTraceFaultData.getLineData());
            //添加右上角描述
//            mTracingFaultChart.setRightTopLegendDes(String.valueOf(LineSetupModel.getInstance().getTracePointLoc()/ 1000.0), String.valueOf(LineSetupModel.getInstance().getmTime()), String.valueOf(LineSetupModel.getInstance().getmTime()));
            mTracingFaultChart.animateX(3000);
            mTracingFaultChart.invalidate();

        }

    }

    @Override
    public void onHeartBeat(SocketClient socketClient) {

    }



    @Override
    public void finish() {
        super.finish();
        chSocketClient.getSocketClient().removeSocketClientReceiveDelegate(this);
    }
}
