package cn.android.common.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import cn.android.common.R;


public class DialogUtils {
	public static final String TAG = "DialogUtils";
	
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
		showProgressDialog(context, "请稍候...");
	}
	
	public static Dialog showCustomDialog(Context context, int dialogStyle, int contenView, int gravity, boolean touchOutSide, LayoutParams params) {
		if (context == null) {
			LogUtils.i(TAG, "Custom dialog new instance, but context is null");
			return null;
		}
		customDialog = new Dialog(context, dialogStyle);
		View view = LayoutInflater.from(context).inflate(contenView, null);
		customDialog.setContentView(view, params);
		customDialog.setCanceledOnTouchOutside(touchOutSide);
		Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(gravity);
		customDialog.show();
		return customDialog;
	}
	
	public static void showCustomDialog(Context context, int dialogStyle, View view, int gravity, boolean touchOutSide, LayoutParams params) {
		if (context == null) {
			LogUtils.i(TAG, "Custom dialog new instance, but context is null");
			return ;
		}
		customDialog = new Dialog(context, dialogStyle);
		customDialog.setContentView(view, params);
		customDialog.setCanceledOnTouchOutside(touchOutSide);
		Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(gravity);
		customDialog.show();
	}
	
	public static Dialog showCustomDialog(Context context, int dialogStyle, int contenView) {
		if (context == null) {
			LogUtils.i(TAG, "Custom dialog new instance, but context is null");
			return null;
		}
		customDialog = new Dialog(context, dialogStyle);
		customDialog.setContentView(contenView);
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.show();
		return customDialog;
	}
	
	public static void showCustomWaitDialog (Context context) {
		showCustomDialog(context, R.style.DialogTranslucent, R.layout.dialog_custom_progress);
	}
	
	public static void dismiss() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		if (customDialog != null) {
			customDialog.dismiss();
			customDialog = null;
		}
	}

}
