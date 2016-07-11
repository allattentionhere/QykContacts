package com.allattentionhere.qykcontacts.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allattentionhere.qykcontacts.Activities.DetailActivity;
import com.allattentionhere.qykcontacts.Helper.MyApplication;
import com.allattentionhere.qykcontacts.Model.Contacts;
import com.allattentionhere.qykcontacts.R;
import com.google.gson.Gson;


public class ContactsAdapter extends BaseAdapter {

    private Activity _activity;
    private Contacts[] contacts;
    private boolean isSetListener;

    public ContactsAdapter(Activity _activity, Contacts[] contacts, boolean isSetListener) {
        this._activity = _activity;
        this.contacts = contacts;
        this.isSetListener = isSetListener;
    }

    @Override
    public int getCount() {
        return this.contacts.length;
    }

    @Override
    public Object getItem(int position) {
        return contacts[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return contacts.length;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ContactsHolder mContactsHolder;
        if (convertView == null) {
            mContactsHolder = new ContactsHolder();
            LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_contact, null, false);
            mContactsHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            mContactsHolder.txt_gender = (TextView) convertView.findViewById(R.id.txt_gender);
            mContactsHolder.txt_email = (TextView) convertView.findViewById(R.id.txt_email);
            mContactsHolder.img_contact = (ImageView) convertView.findViewById(R.id.img_contact);
            mContactsHolder.card_view = (CardView) convertView.findViewById(R.id.card_view);
            mContactsHolder.img_fav = (ImageView) convertView.findViewById(R.id.img_fav);
            convertView.setTag(mContactsHolder);
        } else {
            mContactsHolder = (ContactsHolder) convertView.getTag();
        }
        mContactsHolder.txt_name.setText(contacts[position].getName());
        mContactsHolder.txt_email.setText(contacts[position].getEmail());
        mContactsHolder.txt_gender.setText(contacts[position].getGender());

        MyApplication.imageLoader.displayImage(contacts[position].getThumbnailUrl(), mContactsHolder.img_contact);

        if (contacts[position].getIsFavorite().equalsIgnoreCase("true")) {
            mContactsHolder.img_fav.setVisibility(View.VISIBLE);
        } else {
            mContactsHolder.img_fav.setVisibility(View.GONE);
        }
        if (isSetListener) {
            mContactsHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(_activity, DetailActivity.class);
                    i.putExtra("id", contacts[position].getId());
                    i.putExtra("contact_string", new Gson().toJson(contacts[position]));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(_activity, (View) mContactsHolder.img_contact, "contact");
                        _activity.startActivityForResult(i,100, options.toBundle());
                    } else {
                        _activity.startActivityForResult(i,100);
                    }
                }
            });
        } else {
            mContactsHolder.card_view.setClickable(false);
            mContactsHolder.card_view.setEnabled(false);
        }
        return convertView;
    }

    public class ContactsHolder {
        private ImageView img_contact;
        private TextView txt_name;
        private TextView txt_gender;
        private TextView txt_email;
        private CardView card_view;
        private ImageView img_fav;
    }

}
