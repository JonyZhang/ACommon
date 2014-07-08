package cn.android.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageSdcardCacheUtils {
	
	public static final String TAG = "ImageSdcardCacheUtils";
	
	private LruCache<String, String> sdcardCache;
	
	public ImageSdcardCacheUtils() {
		super();
		initSdcardCache();
	}

	private void initSdcardCache() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		sdcardCache = new LruCache<String, String>(cacheSize);
	}

	public void addBitmapToSdcardCache(String key, String path, Bitmap bitmap) throws FileNotFoundException, IOException {
		synchronized (bitmap) {
			if (getBitmapFromSdcardCache(key) == null) {
				if (CommonUtils.isExternalStorageWritable()) {
					FileUtils.writeBitmap(path, bitmap);
					sdcardCache.put(key, path);
				}
			}
			notifyAll();
		}
	}

	public Bitmap getBitmapFromSdcardCache(String key) throws FileNotFoundException, IOException {
	    return getBitmapFromSdcardCache(key, ImageUtils.LOW_SAMPLE_SIZE);
	}
	
	public Bitmap getBitmapFromSdcardCache(String key, int sampleSize) throws FileNotFoundException, IOException {
	    return ImageUtils.byteToBitmap(FileUtils.readFile(sdcardCache.get(key)), sampleSize);
	}
	
	public void clearSdcardCache(String directory) {
		File[] files = FileUtils.listFiles(directory);
		if (files == null) {
			LogUtils.i(TAG, "Clear sdcard cache, but directory is no file.");
			return;
		}
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}
	
}
