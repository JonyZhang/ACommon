package cn.android.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;

/**
 * ImageUtils
 * <ul>
 * convert between Bitmap, byte array, Drawable
 * <li>{@link #bitmapToByte(Bitmap)}</li>
 * <li>{@link #bitmapToDrawable(Bitmap)}</li>
 * <li>{@link #byteToBitmap(byte[])}</li>
 * <li>{@link #byteToDrawable(byte[])}</li>
 * <li>{@link #drawableToBitmap(Drawable)}</li>
 * <li>{@link #drawableToByte(Drawable)}</li>
 * </ul>
 * <ul>
 * get image
 * <li>{@link #getInputStreamFromUrl(String, int)}</li>
 * <li>{@link #getBitmapFromUrl(String, int)}</li>
 * <li>{@link #getDrawableFromUrl(String, int)}</li>
 * </ul>
 * <ul>
 * scale image
 * <li>{@link #scaleImageTo(Bitmap, int, int)}</li>
 * <li>{@link #scaleImage(Bitmap, float, float)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-6-27
 */
public class ImageUtils {

    public static final String TAG = "ImageUtils";
    
    public static final int LOW_SAMPLE_SIZE = 64 * 64;
    public static final int MIDDLE_SAMPLE_SIZE = 128 * 128;
    public static final int HIGH_SAMPLE_SIZE = 256 * 256;
    public static final int XHIGH_SAMPLE_SIZE = 512 * 512;

	/**
     * convert Bitmap to byte array
     * 
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }
    
    /**
     * convert byte array to Bitmap by sample size
     * 
     */
    public static Bitmap byteToBitmap(byte[] data, int sampleSize) {
    	if ((data == null || data.length == 0)) {
			return null;
		}
    	BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, sampleSize);  
        opts.inJustDecodeBounds = false;  
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        return  bitmap;
    }

    /**
     * convert Drawable to Bitmap
     * 
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable)d).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * convert Drawable to byte array
     * 
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * get input stream from network by imageurl, you need to close inputStream yourself
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis) throws MalformedURLException, IOException{
    	LogUtils.d(TAG, "getInputStreamFromUrl : imageUrl = " + imageUrl);
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if (readTimeOutMillis > 0) {
                con.setReadTimeout(readTimeOutMillis);
            }
            stream = con.getInputStream();
        } catch (MalformedURLException e) {
            closeInputStream(stream);
        } catch (IOException e) {
            closeInputStream(stream);
        }
        return stream;
    }

    /**
     * get drawable by imageUrl
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @return
     */
    public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis) throws MalformedURLException, IOException{
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis);
        if (stream == null) {
        	LogUtils.i(TAG, "get bitmap stream is null");
			return null;
		}
        Drawable d = Drawable.createFromStream(stream, "src");
        closeInputStream(stream);
        return d;
    }

    /**
     * get Bitmap by imageUrl
     * 
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut) throws MalformedURLException, IOException{
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut);
        if (stream == null) {
        	LogUtils.i(TAG, "get bitmap stream is null");
			return null;
		}
        Bitmap b = BitmapFactory.decodeStream(stream);
        closeInputStream(stream);
        return b;
    }
    
    /**
     * get Bitmap by imageUrl and change bitmap sample size
     * 
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut, int sampleSize) throws MalformedURLException, IOException, OutOfMemoryError{
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut);
        return getBitmapFromInputStream(stream, sampleSize);
    }
    
    public static Bitmap getBitmapFromInputStream (InputStream stream, int sampleSize) throws IOException {
    	if (stream == null) {
        	LogUtils.i(TAG, "get bitmap stream is null");
			return null;
		}
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;  
        byte[] data = inputStream2ByteArray(stream);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, sampleSize);  
        opts.inJustDecodeBounds = false;  
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        closeInputStream(stream);
		return bitmap;
    }
    
    public static Bitmap getBitmapFromFile (String path, int sampleSize) throws IOException {
    	if (TextUtils.isEmpty(path)) {
    		LogUtils.i(TAG, "get bitmap from file is null");
			return null;
		}
    	File file = new File(path);
    	if (!file.exists()) {
    		LogUtils.i(TAG, "get bitmap from file not exists.");
    		return null;
		}
    	FileInputStream inputStream = new FileInputStream(file);
    	return getBitmapFromInputStream(inputStream, sampleSize);
    }


    /**
     * scale image
     * 
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float)newWidth / org.getWidth(), (float)newHeight / org.getHeight());
    }

    /**
     * scale image
     * 
     * @param org
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

    /**
     * close inputStream
     * 
     * @param s
     */
    private static void closeInputStream(InputStream s) {
        if (s == null) {
            return;
        }

        try {
            s.close();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }
	
	/**
	 * Convert input stream to byte array
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] inputStream2ByteArray(InputStream inputStream) throws IOException {  
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[1024];  
        int len = 0;  
        while ( (len = inputStream.read(buff)) != -1) {  
            outputStream.write(buff, 0, len);  
        }  
        inputStream.close();  
        outputStream.close();  
        return outputStream.toByteArray();  
    }
	
	/**
	 * Calculate a bitmap current sample size
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * Calculate a bitmap initial sample size
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	/**
	 * Rotate a bitmap
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// ��תͼƬ ����
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// �����µ�ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	/**
	 * Rotate a bitmap
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(String path, Bitmap bitmap) {
		// ��תͼƬ ����
		Matrix matrix = new Matrix();
		matrix.postRotate(readPictureDegree(path));
		// �����µ�ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	/**
	 * Read a picture degree
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
