package cn.android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * List view could scroll with others together
 * @author JonyZhang	E-mail:xxpgd2@gmail.com
 * @version CreateTime:2014-04-29
 *
 */
public class MyListView extends ListView {
	
	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
		super.onMeasure(widthMeasureSpec, expandSpec);
		
	}
}
