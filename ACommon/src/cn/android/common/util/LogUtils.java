package cn.android.common.util;

public class LogUtils {

	public static final boolean debug = true;

	public static void d(String msg) {
		d("debug", msg);
	}

	public static void d(String tag, String msg) {
		if (debug) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Object... args) {
		d(tag, String.format(msg, args));
	}

	public static void i(String msg) {
		i("debug", msg);
	}
	
	public static void i(String tag, String msg) {
		if (debug) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Object... args) {
		i(tag, String.format(msg, args));
	}
	
}
