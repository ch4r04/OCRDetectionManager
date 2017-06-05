package activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bysj.ch4r0n.ocrdetectionmanager.R;

import es.dmoral.toasty.Toasty;
import tasks.LoginTask;
import tasks.Response;
import utils.ValidatorUtils;

/**
 * Created by ch4r0n on 2017/6/5.
 */

public class MYLoginActivity extends Activity implements View.OnClickListener, Response.Listener<Boolean> {


    private ImageView mIv;
    private ImageView mZoomImgs;
    private ImageView mView1;
    private ImageView mIvSdNoPic;
    /**
     * 光缆监测通信系统
     */
    private TextView mTextView;
    private ImageView mImageView1;
    /**
     * 请输入接口号以进行连接
     */
    private EditText mEtDeviceId;
    private Button mBtnHistoryAccount;
    private RelativeLayout mRelativeLayout1;
    /**
     * 连　　接
     */
    private Button mBtnLogin;
    private LinearLayout mLlsub03;
    private RelativeLayout mRlsub02;
    private LinearLayout mLinearLayout1;
    private FrameLayout mAppRootview;

    LoginTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO:OnCreate Method has been created, run FindViewById again to generate code
        setContentView(R.layout.activity_login);
        initView();
    }

    public void initView() {
        mIv = (ImageView) findViewById(R.id.iv);
        mZoomImgs = (ImageView) findViewById(R.id.zoomImgs);
        mView1 = (ImageView) findViewById(R.id.view1);
        mIvSdNoPic = (ImageView) findViewById(R.id.iv_sd_no_pic);
        mTextView = (TextView) findViewById(R.id.textView);
        mImageView1 = (ImageView) findViewById(R.id.imageView1);
        mEtDeviceId = (EditText) findViewById(R.id.et_device_id);
        mBtnHistoryAccount = (Button) findViewById(R.id.btn_history_account);
        mRelativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mLlsub03 = (LinearLayout) findViewById(R.id.llsub03);
        mRlsub02 = (RelativeLayout) findViewById(R.id.rlsub02);
        mLinearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        mAppRootview = (FrameLayout) findViewById(R.id.app_rootview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (ValidatorUtils.isVaildDevice(mEtDeviceId.getText().toString()) == false){
                    Toasty.warning(this, "输入不合法 请检查参数").show();
                    break;
                }
                task = new LoginTask(this,this,mEtDeviceId.getText().toString());
                task.execute();
                break;
        }
    }

    @Override
    public void onResponse(Boolean result) {

    }

    @Override
    public void onErrorResponse(Exception exception) {

    }
}
