package activity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.bysj.ch4r0n.ocrdetectionmanager.R;

/**
 * Created by ch4r0n on 2017/4/21.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Button btnTrace;
    private Button btnOCRCensus;
    private Button btnOTDR;
    private Button btnSetting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTrace = (Button) findViewById(R.id.btn_tracing_fault);
        btnOCRCensus = (Button) findViewById(R.id.btn_ocr_census);
        btnOTDR = (Button) findViewById(R.id.btn_OTDR);
        btnSetting = (Button) findViewById(R.id.btn_setting);

        btnTrace.setOnClickListener(this);
        btnOCRCensus.setOnClickListener(this);
        btnOTDR.setOnClickListener(this);
        btnSetting.setOnClickListener(this);


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
    public void onClick(View v) {

        switch (v.getId()){
            //故障追踪
            case R.id.btn_tracing_fault:
                startActivityForResult(new Intent(this, TraceActivity.class),0);
                finish();
                //注销回调
                break;
            //光缆普查
            case R.id.btn_ocr_census:
                startActivityForResult(new Intent(this,OCRCensusActivity.class),0);
                finish();
                break;
            //OTDR
            case R.id.btn_OTDR:
                startActivityForResult(new Intent(this, CubicLineChartActivity.class),0);
                finish();
                break;
            //系统设置
            case R.id.btn_setting:
                break;
        }
    }

}
