package cn.android.common.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import cn.android.common.R;

/**
 * Common util
 * @author JonyZhang	E-mail:xxpgd2@gmail.com
 * @version CreateTime:2014-04-29
 *
 */
public class CommonUtils {
	public static final String TAG = "CommonUtils";

	/**
	 * Whether a string is the mobile phone number 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNumber(String mobile) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * Whether a string is the email
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * Open url by browser
	 * 
	 * @param context
	 * @param url
	 */
	public static void openUrl(Context context, String url) {
		if (TextUtils.isEmpty(url)) {
			ToastUtils.shortToast(context, context.getResources().getString(R.string.open_url_fail));
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (url.startsWith("http")) {
			intent.setData(Uri.parse(url));
		} else {
			intent.setData(Uri.parse("http://" + url));
		}
		context.startActivity(intent);
	}

	public static final double EARTH_RADIUS = 6378.137;

	public static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * �����ͼ��������ʵ�ʾ���
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		// s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static String numberFormat(String distance) {
		DecimalFormat df = new DecimalFormat();
		int d1 = Integer.parseInt(distance);
		double d2 = (d1 * 1.0) / 1000;
		if (d2 < 1) {
			df.applyPattern("0.0 km");
		} else {
			df.applyPattern("#.0 km");
		}
		return df.format(d2);
	}

	public static String numberFormat(double distance) {
		DecimalFormat df = new DecimalFormat();
		if (distance < 1) {
			df.applyPattern("0.0 km");
		} else {
			df.applyPattern("#.0 km");
		}
		return df.format(distance);
	}

	/**
	 * Take a picture
	 * @param activity
	 * @param requestCode
	 * @param tempPath
	 * @return
	 * @throws IOException
	 */
	public static File takePhoto(Activity activity, int requestCode, String tempPath)
			throws IOException {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { 
			ToastUtils.longToast(activity, activity.getResources().getString(R.string.no_sdcard));
			LogUtils.i(TAG, "Sdcard is not exists.");
			return null;
		}
		String fileName = TimeUtils.getTime("yyyyMMddHHmmss", new Date());
		File tempPhotoDir = new File(tempPath);
		if (!tempPhotoDir.exists()) {
			tempPhotoDir.mkdirs();
		}
		File file = File.createTempFile(fileName, ".jpg", tempPhotoDir);
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.longToast(activity, activity.getResources().getString(R.string.no_camera));
			LogUtils.i(TAG, "Take photo catch an exception, info = " + e.getMessage());
		} 
		return file;
	}

	/**
	 * Choose a picture from android system
	 * @param activity
	 * @param requestCode
	 */
	public static void choosePhoto(Activity activity, int requestCode) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, requestCode);
	}
	
	/** Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * Take a screen shot
	 * 
	 * @param activity
	 * @return bitmap
	 */
	public static Bitmap takeScreenShot(Activity activity) {
		Bitmap bitmap = null;
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		bitmap = view.getDrawingCache();

		Rect frame = new Rect();
		view.getWindowVisibleDisplayFrame(frame);
		int stautsHeight = frame.top;

		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();
		bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height - stautsHeight);
		return bitmap;
	}
	
	/**
	 * Get device IMEI id
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}
	
	public static void setScreenOrientation(FragmentActivity activity) {
		if (isTablet(activity)) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		} else {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
	/**
	 * Judge a device is a phone or a tablet
	 * 
	 * @return true is a tablet, false otherwise.
	 */
	public static boolean isTablet(Context context) {
//		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
//	    Display display = wm.getDefaultDisplay();  
//	    float screenWidth = display.getWidth();  
//	    float screenHeight = display.getHeight();  
//	    DisplayMetrics dm = new DisplayMetrics();  
//	    display.getMetrics(dm);
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}

}
