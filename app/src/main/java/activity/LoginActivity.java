package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bysj.ch4r0n.ocrdetectionmanager.R;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketClientReceiveDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

import socket.CHSocketClient;
import socket.FrameDataSocket;
import tasks.LoginTask;
import utils.CHToast;

public class LoginActivity extends BaseActivity implements View.OnClickListener,SocketClientDelegate,SocketClientReceiveDelegate{
    private static final int REQUEST_CODE_LOGIN = 1;

    private EditText deviceInterfaceIDText;


    CHSocketClient chSocketClient = CHSocketClient.getCHSocketClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        deviceInterfaceIDText = (EditText) findViewById(R.id.et_device_id);
        //设置键盘回弹响应
        findViewById(R.id.app_rootview).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

        chSocketClient.getSocketClient().registerSocketClientDelegate(this);
        chSocketClient.getSocketClient().registerSocketClientReceiveDelegate(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击屏幕空白 则回弹键盘
            case R.id.app_rootview:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
            //点击登录
            case R.id.btn_login:
                //创建一个连接
                if (chSocketClient.getSocketClient().isConnected()){
                    chSocketClient.getSocketClient().sendData(FrameDataSocket.sendToConnectRequest(deviceInterfaceIDText.getText().toString()));
                }
                chSocketClient.getSocketClient().connect();

                break;

        }
    }

    @Override
    public void onConnected(SocketClient client) {
        Log.e("GAO","连接成功");
        chSocketClient.getSocketClient().sendData(FrameDataSocket.sendToConnectRequest(deviceInterfaceIDText.getText().toString()));

    }

    @Override
    public void onDisconnected(SocketClient client) {
        CHToast.showShort(getApplicationContext(),"连接已断开");
        Log.e("GAO","连接失败");

    }

    @Override
    public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
        if (responsePacket.getMessage().equals("OK_CONNECT") ){
            //接收到服务端发送过来的确认字符串
            CHToast.showShort(this,"连接设备成功");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            chSocketClient.getSocketClient().removeSocketClientDelegate(this);
            chSocketClient.getSocketClient().removeSocketClientReceiveDelegate(this);
        }
    }

    @Override
    public void onHeartBeat(SocketClient socketClient) {

    }

}