package tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.bysj.ch4r0n.ocrdetectionmanager.R;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketClientReceiveDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;

import activity.MYLoginActivity;
import activity.MainActivity;
import es.dmoral.toasty.Toasty;
import socket.CHSocketClient;
import socket.FrameDataSocket;
import tasks.Response.Listener;

public class LoginTask extends BaseAsyncTask<Void, Void, Boolean> implements SocketClientReceiveDelegate,SocketClientDelegate{
	private String deviceInterfaceID;	//连接设备号

	private CHSocketClient chSocketClient;

	private ProgressDialog dialog;

    private Context context;

	
	public LoginTask(Listener<Boolean> listener, Context context, String deviceInterfaceID) {
		super(listener, context);

        chSocketClient = CHSocketClient.getCHSocketClient();

		this.deviceInterfaceID = deviceInterfaceID;

        this.context = context;

		dialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.login));

		//注册回调 服务器响应
		chSocketClient.getSocketClient().registerSocketClientReceiveDelegate(this);

		chSocketClient.getSocketClient().registerSocketClientDelegate(this);


	}




    @Override
	public Response<Boolean> doInBackground(Void... params) {

		System.out.println("doing login");
		if (context != null ) {
			//进行连接设备操作
            chSocketClient.getSocketClient().connect();

			//如果已经建立了连接 就直接发送请求设备标识符连接
			if (chSocketClient.getSocketClient().isConnected()){
				chSocketClient.getSocketClient().sendData(FrameDataSocket.sendToConnectRequest(deviceInterfaceID));
			}

			return Response.success(true);
		} else {
			Exception e = new Exception("my exception");
			return Response.error(e);
		}
	}


	@Override
	protected void onCancelled() {
		super.onCancelled();
		dismissDialog();
	}

	public void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void dismissDialogAndCancel() {
		dismissDialog();
		cancel(false);
	}

    @Override
    public void onConnected(SocketClient client) {
        dismissDialog();
		//建立连接之后 直接发送设备符请求连接
		chSocketClient.getSocketClient().sendData(FrameDataSocket.sendToConnectRequest(deviceInterfaceID));
    }

    @Override
    public void onDisconnected(SocketClient client) {
        dismissDialog();
        Toasty.error((MYLoginActivity)context,"连接失败 服务器未响应").show();
    }

    @Override
	public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
		dismissDialog();
		if (responsePacket.getMessage().equals("OK_CONNECT") ){
			//接收到服务端发送过来的确认字符串
			Toasty.success((MYLoginActivity)context,"连接设备成功").show();
			((MYLoginActivity)context).startActivity(new Intent(context, MainActivity.class));
			((MYLoginActivity)context).finish();
			chSocketClient.getSocketClient().removeSocketClientDelegate(this);
			chSocketClient.getSocketClient().removeSocketClientReceiveDelegate(this);
		}else {
			Toasty.error(((MYLoginActivity)context),"连接设备失败，设备码不正确").show();
		}

	}

	@Override
	public void onHeartBeat(SocketClient socketClient) {

	}
}