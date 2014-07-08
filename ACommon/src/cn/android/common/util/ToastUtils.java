package cn.android.common.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	
	public static void shortToast(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}
	
	public static void longToast(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

}
