package cn.android.common.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import cn.android.common.R;


public class DialogUtils {
	public static final String TAG = "ProgressDialogUtils";
	
	private static ProgressDialog progressDialog;
	private static Dialog customDialog;
	
	public static void showProgressDialog(Context context, String message) {
		if (context == null) {
			LogUtils.i(TAG, "Progress dialog new instance, but context is null");
			return;
		}
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}
	
	public static void showProgressDialog(Context context) {
		showProgressDialog(context, context.getResources().getString(R.string.please_wait));
	}
	
	public static void showCustomDialog(Context context, int dialogStyle, int contenView) {
		if (context == null) {
			LogUtils.i(TAG, "Custom dialog new instance, but context is null");
			return;
		}
		customDialog = new Dialog(context, dialogStyle);
		customDialog.setContentView(contenView);
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.show();
	}
	
	public static void dismiss() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
			customDialog = null;
		}
	}

}
