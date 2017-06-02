package tasks;

import android.app.ProgressDialog;
import android.content.Context;

import com.bysj.ch4r0n.ocrdetectionmanager.R;

import socket.CHSocketClient;
import tasks.Response.Listener;

public class LoginTask extends BaseAsyncTask<Void, Void, Boolean> {
	private String deviceInterfaceID;	//连接设备号

	private String socketURL;

	private int socketPort;

	private ProgressDialog dialog;

	
	public LoginTask(Listener<Boolean> listener, Context context, String deviceInterfaceID, String socketURL, int socketPort) {
		super(listener, context);
		
		this.deviceInterfaceID = deviceInterfaceID;

		this.socketURL = socketURL;

		this.socketPort = socketPort;

		dialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.login));
//        Toast.makeText(context, "登录中...", Toast.LENGTH_LONG).show();
	}


	@Override
	public Response<Boolean> doInBackground(Void... params) {
		Context context = getContext();

		System.out.println("doing login");
		if (context != null) {
			//进行连接设备操作
			return Response.success(true);
		} else {
			Exception e = new Exception("my exception");
			return Response.error(e);
		}
	}

	@Override
	protected void onPostExecute(Response<Boolean> response) {
		dialog.dismiss();

		super.onPostExecute(response);
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
}