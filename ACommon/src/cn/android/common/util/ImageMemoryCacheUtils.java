package cn.android.common.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageMemoryCacheUtils {
	
	private LruCache<String, Bitmap> memoryCache;
	
	public ImageMemoryCacheUtils() {
		super();
		initMemoryCache();
	}

	private void initMemoryCache() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		memoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            return ImageUtils.bitmapToByte(bitmap).length / 1024;
	        }
		};
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemoryCache(key) == null) {
	    	memoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemoryCache(String key) {
	    return memoryCache.get(key);
	}
	
}
