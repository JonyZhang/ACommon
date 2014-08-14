
package cn.android.common.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @description SwipeListView adapter reference
 *
 */
public class SwipeAdapter extends BaseAdapter {
    /**
     * 上下文对�?     */
    private Context mContext = null;

    /**
     * 
     */
    private int mRightWidth = 0;

    /**
     * 单击事件监听�?     */
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    /**
     * @param mainActivity
     */
    public SwipeAdapter(Context ctx, int rightWidth, IOnItemRightClickListener l) {
        mContext = ctx;
        mRightWidth = rightWidth;
        mListener = l;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int thisPosition = position;
        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
//            holder = new ViewHolder();
//            holder.item_left = (View)convertView.findViewById(R.id.item_left);
//            holder.item_right = (View)convertView.findViewById(R.id.item_right);
//            holder.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
//            holder.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
//            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder)convertView.getTag();
        }
//        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT);
//        holder.item_left.setLayoutParams(lp1);
//        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
//        holder.item_right.setLayoutParams(lp2);
//        holder.item_left_txt.setText("item " + thisPosition);
//        holder.item_right_txt.setText("delete " + thisPosition);
//        holder.item_right.setOnClickListener(new OnClickListener() {
//            @Override-
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onRightClick(v, thisPosition);
//                }
//            }
//        });
        return convertView;
    }

    private class ViewHolder {
        View item_left;

        View item_right;

        TextView item_left_txt;

        TextView item_right_txt;
    }
}
