package activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import socket.CHSocketClient;
import socket.FrameDataSocket;

/**
 * Created by ch4r0n on 2017/5/10.
 */

public class BaseActivity extends Activity {
    public final static String BORADCAST_ACTION_EXIT = "com.ch4r0n.exit";//关闭活动的广播action名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在当前的activity中注册广播
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BORADCAST_ACTION_EXIT);//为BroadcastReceiver指定一个action，即要监听的消息名字
        registerReceiver(mBoradcastReceiver,filter); //动态注册监听  静态的话 在AndroidManifest.xml中定义
    }
    private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BORADCAST_ACTION_EXIT)){//发来关闭action的广播
                finish();
            }
        }
    };
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mBoradcastReceiver); //取消监听
    }
    //返回按钮 退出系统
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请确认退出系统？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(BORADCAST_ACTION_EXIT);
                        sendBroadcast(intent);//发送退出系统广播  每个接收器都会收到 调动finish（）关闭activity
//                        CHSocketClient.getCHSocketClient().disConnectedSocket();
                        CHSocketClient.getCHSocketClient().getSocketClient().sendData(FrameDataSocket.sendDisconnectRequest());
                        CHSocketClient.getCHSocketClient().getSocketClient().disconnect();
                        finish();
                        ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE);
                        am.restartPackage(getPackageName());

                    }
                })
                .setNegativeButton("按错了...", null)
                .show();
    }
}
