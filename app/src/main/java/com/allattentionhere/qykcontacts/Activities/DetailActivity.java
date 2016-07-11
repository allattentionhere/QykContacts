package com.allattentionhere.qykcontacts.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.allattentionhere.qykcontacts.Helper.DbHandler;
import com.allattentionhere.qykcontacts.Helper.MyApplication;
import com.allattentionhere.qykcontacts.Helper.ReminderService;
import com.allattentionhere.qykcontacts.Model.Contacts;
import com.allattentionhere.qykcontacts.R;
import com.google.gson.Gson;

import java.util.Calendar;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_remind;
    ImageButton imgbtn_back;
    ImageView img_favorite;
    ImageView img_profile;
    Contacts mContact;
    String id;
    TextView txt_id, txt_name, txt_email, txt_gender, txt_mobile, txt_home, txt_office, txt_address;
    boolean hasChangedFav = false;
    boolean hasClickedRemind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent().hasExtra("id") && getIntent().hasExtra("contact_string")) {
            id = getIntent().getStringExtra("id");
            mContact = new Gson().fromJson(getIntent().getStringExtra("contact_string"), Contacts.class);
        } else {
            finish();
        }

        initLayout();
        setListener();

    }

    private void setListener() {
        btn_remind.setOnClickListener(this);
        imgbtn_back.setOnClickListener(this);
        img_favorite.setOnClickListener(this);
    }

    private void initLayout() {
        img_favorite = (ImageView) findViewById(R.id.img_favorite);
        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_gender = (TextView) findViewById(R.id.txt_gender);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        txt_home = (TextView) findViewById(R.id.txt_home);
        txt_office = (TextView) findViewById(R.id.txt_office);
        txt_address = (TextView) findViewById(R.id.txt_address);
        btn_remind = (Button) findViewById(R.id.btn_remind);
        imgbtn_back = (ImageButton) findViewById(R.id.imgbtn_back);
        img_profile = (ImageView) findViewById(R.id.img_profile);

        if (mContact.getIsToBeReminded().equalsIgnoreCase("true")) {
            btn_remind.setEnabled(false);
            btn_remind.setBackgroundColor(getResources().getColor(R.color.background));
            btn_remind.setTextColor(getResources().getColor(R.color.lightText));
        } else {
            btn_remind.setEnabled(true);
            btn_remind.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btn_remind.setTextColor(getResources().getColor(android.R.color.white));
        }
        txt_id.setText("ID: " + mContact.getId());
        txt_email.setText("EMAIL: " + mContact.getEmail());
        txt_name.setText("NAME: " + mContact.getName());
        txt_gender.setText("GENDER: " + mContact.getGender());
        txt_mobile.setText("MOB: " + mContact.getPhone().getMobile());
        txt_home.setText("HOME: " + mContact.getPhone().getHome());
        txt_office.setText("OFFICE: " + mContact.getPhone().getOffice());
        txt_address.setText("ADDRESS: " + mContact.getAddress());

        MyApplication.imageLoader.displayImage(mContact.getThumbnailUrl(), img_profile);

        final Drawable back = getResources().getDrawable(R.drawable.back);
        back.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        imgbtn_back.setBackground(back);


        if (mContact.getIsFavorite().equalsIgnoreCase("true")) {
            img_favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_filled));
        } else {
            img_favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_remind:
                hasClickedRemind = true;
                mContact.setIsToBeReminded("true");
                DbHandler.updateContact(id, new Gson().toJson(mContact));
                Log.d("k9_resume", "x=" + new Gson().toJson(mContact));
                Calendar cal_alarm = Calendar.getInstance();
                Intent notificationIntent = new Intent(this, ReminderService.class);
                notificationIntent.putExtra("name", mContact.getName());
                notificationIntent.putExtra("id", mContact.getId());
                notificationIntent.putExtra("string_contact", new Gson().toJson(mContact));
                PendingIntent contentIntent = PendingIntent.getService(this, (Math.abs((int) cal_alarm.getTimeInMillis()) % 10000), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                am.cancel(contentIntent);
                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (30 * 60 * 1000), contentIntent);

                btn_remind.setEnabled(false);
                btn_remind.setBackgroundColor(getResources().getColor(R.color.background));
                btn_remind.setTextColor(getResources().getColor(R.color.lightText));


                break;
            case R.id.imgbtn_back:
                if (hasChangedFav || hasClickedRemind) setResult(RESULT_OK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    supportFinishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.img_favorite:
                Log.d("k9_click", "clicked");
                if (hasChangedFav) {
                    hasChangedFav = false;
                } else {
                    hasChangedFav = true;
                }
                if (mContact.getIsFavorite().equalsIgnoreCase("true")) {
                    mContact.setIsFavorite("false");
                    img_favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_empty));
                } else {
                    mContact.setIsFavorite("true");
                    img_favorite.setImageDrawable(getResources().getDrawable(R.drawable.star_filled));
                }
                DbHandler.updateContact(id, new Gson().toJson(mContact));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (hasChangedFav || hasClickedRemind) setResult(RESULT_OK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("k9_resume", "called: " + mContact.getIsToBeReminded());
        if (mContact.getIsToBeReminded().equalsIgnoreCase("true")) {
            btn_remind.setEnabled(false);
            btn_remind.setBackgroundColor(getResources().getColor(R.color.background));
            btn_remind.setTextColor(getResources().getColor(R.color.lightText));
        } else {
            btn_remind.setEnabled(true);
            btn_remind.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btn_remind.setTextColor(getResources().getColor(android.R.color.white));
        }
    }
}
