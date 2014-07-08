package cn.android.common.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * When flag is true, this view pager could not scroll.
 * @author JonyZhang	E-mail:xxpgd2@gmail.com
 * @version CreateTime:2014-04-29
 *
 */
public class MyViewPager extends ViewPager {
	
	private boolean flag;

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (flag) {
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (flag) {
			return false;
		}
		return super.onTouchEvent(arg0);
	}

}
