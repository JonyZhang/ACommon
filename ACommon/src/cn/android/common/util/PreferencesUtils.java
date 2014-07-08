package cn.android.common.util;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * @author Jony Zhang
 *
 */
public class PreferencesUtils{
	
	public static final String PREFERENCES_NAME = "main_preferences";
	
	public static void putString(Context context, String key, String value) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		preference.edit().putString(key, value).commit();
	}
	
	public static String getString(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return preference.getString(key, null);
	}
	
	public static void putInt (Context context, String key, int value) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		preference.edit().putInt(key, value).commit();
	}
	
	public static int getInt(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return preference.getInt(key, 0);
	}
	
	public static void putLong (Context context, String key, long value) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		preference.edit().putLong(key, value).commit();
	}
	
	public static long getLong(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return preference.getLong(key, 0);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		preference.edit().putBoolean(key, value).commit();
	}
	
	public static boolean getBoolean(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return preference.getBoolean(key, false);
	}
	
	public static void remove(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		preference.edit().remove(key).commit();
	}
	
}
