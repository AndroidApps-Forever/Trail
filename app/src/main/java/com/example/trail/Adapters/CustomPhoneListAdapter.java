package com.example.trail.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trail.R;

import java.util.ArrayList;

/**
 * Created by Shrabanti on 04-11-2015.
 */
public class CustomPhoneListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> phoneNo;

    public CustomPhoneListAdapter(Activity context, ArrayList<String> name, ArrayList<String> phoneNo) {
        super(context, R.layout.contacts_single_row, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.name=name;
        this.phoneNo=phoneNo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contacts_single_row, parent, false);

        TextView tv1 = (TextView) rowView.findViewById(R.id.textView4);
        TextView tv2 = (TextView) rowView.findViewById(R.id.textView5);

        if(name.get(position).equals(phoneNo.get(position)))
        {
            tv1.setText(name.get(position));
        }else
        {
            tv1.setText(name.get(position));
            tv2.setText(phoneNo.get(position));
        }
        return rowView;
    }
}
