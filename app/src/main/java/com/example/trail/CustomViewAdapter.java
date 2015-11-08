package com.example.trail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shambhavi on 11/8/2015.
 */
public class CustomViewAdapter extends PagerAdapter {
    private int[] imageResources = {R.drawable.capture1,R.drawable.capture2,R.drawable.capture3};
    private Context ctx;
    private LayoutInflater layoutInflater;
    public CustomViewAdapter(Context ctx)
    {
        this.ctx = ctx;
    }
    @Override
    public int getCount() {
        return imageResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view==(LinearLayout)o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.swipe1,container,false);                    //container is the view pager here
        ImageView imageView =(ImageView) itemView.findViewById(R.id.image);
        TextView textView =(TextView) itemView.findViewById(R.id.image_count);
        imageView.setImageResource(imageResources[position]);

        textView.setText("Capture : " + position);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
