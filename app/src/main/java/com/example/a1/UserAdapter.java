package com.example.a1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserItem> {

    public UserAdapter(Context context, List<UserItem> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserItem user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvEmail.setText("Email: " + user.getEmail());
        tvStatus.setText("Статус: " + user.getStatus());

        return convertView;
    }
}
