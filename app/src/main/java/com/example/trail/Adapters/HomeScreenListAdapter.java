package com.example.trail.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trail.R;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by Anjali on 24-11-2015.
 */
public class HomeScreenListAdapter extends ArrayAdapter {

    private String mUserId;
    private List<ParseUser> users;
    private ViewHolder holder;

    public HomeScreenListAdapter(Context context, String userId, List<ParseUser> users) {
        super(context, 0, users);
        this.mUserId = userId;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //System.out.println(convertView);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.user_item, parent, false);
            holder = new ViewHolder();
            holder.imageLeft = (ImageView)convertView.findViewById(R.id.ivProfilePicture);
            holder.userName = (TextView)convertView.findViewById(R.id.tvUsername);
            System.out.println();
            //holder.messagePreview = (TextView)convertView.findViewById(R.id.tvMessage);
            convertView.setTag(holder);
        }

        //final Message message = (Message)getItem(position);
        holder = (ViewHolder)convertView.getTag();
        String name = users.get(position).getUsername();
        //System.out.println("Why? " + name);
        final boolean isMe = users.get(position).getObjectId().equals(mUserId);

        if (!isMe) {
            System.out.println("here its taking time");
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.userName.setText(name);
            holder.userName.setGravity(Gravity.TOP | Gravity.LEFT);
            //holder.messagePreview.setGravity(Gravity.BOTTOM | Gravity.LEFT);
            final ImageView profileView = holder.imageLeft;
            Picasso.with(getContext()).load(getProfileUrl(users.get(position).getObjectId())).into(profileView);
        }
        return convertView;
    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    final class ViewHolder {
        public ImageView imageLeft;
        public TextView userName;
    }


    /*@ParseClassName("User")
    public class User extends ParseUser {

        public int getPhoneNumebr(){ return getInt("phoneNumber");}

        //public void setNumber(int number){ put("number", phoneNumber);}
    }*/
}
