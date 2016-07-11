package com.allattentionhere.qykcontacts.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allattentionhere.qykcontacts.Adapter.ContactsAdapter;
import com.allattentionhere.qykcontacts.Helper.Datacallback;
import com.allattentionhere.qykcontacts.Helper.DbHandler;
import com.allattentionhere.qykcontacts.Helper.HttpRequestHelper;
import com.allattentionhere.qykcontacts.Helper.MyApplication;
import com.allattentionhere.qykcontacts.Model.Contacts;
import com.allattentionhere.qykcontacts.Model.MyResponse;
import com.allattentionhere.qykcontacts.R;
import com.google.gson.Gson;

import org.json.JSONObject;


public class ContactsFragment extends android.support.v4.app.Fragment implements Datacallback {

    ListView list_contacts;
    ProgressBar pb;
    AppCompatButton btn_retry;
    ContactsAdapter mContactsAdapter;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        list_contacts = (ListView) v.findViewById(R.id.list_contacts);

        btn_retry = (AppCompatButton) v.findViewById(R.id.btn_retry);
        pb = (ProgressBar) v.findViewById(R.id.pb);

        LinearLayout viewHeader = new LinearLayout(getActivity());
        viewHeader.setOrientation(LinearLayout.HORIZONTAL);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (8 * MyApplication.metrics.density));
        viewHeader.setLayoutParams(lp);

        list_contacts.addHeaderView(viewHeader);
        list_contacts.addFooterView(viewHeader);

        Contacts[] mC = DbHandler.fetchContactsList();
        if (mC.length > 0) {
            mContactsAdapter = new ContactsAdapter(getActivity(), mC, true);
            list_contacts.setAdapter(mContactsAdapter);
            pb.setVisibility(View.GONE);
            btn_retry.setVisibility(View.GONE);
            list_contacts.setVisibility(View.VISIBLE);
        } else {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //make network call
                new HttpRequestHelper().MakeJsonGetRequest("/contacts.php", null, this, getActivity());
            } else {
                pb.setVisibility(View.GONE);
                btn_retry.setVisibility(View.VISIBLE);
                list_contacts.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Not connected to Internet", Toast.LENGTH_SHORT).show();
            }
        }

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    //make network call
                    new HttpRequestHelper().MakeJsonGetRequest("/contacts.php", null, ContactsFragment.this, getActivity());
                } else {
                    pb.setVisibility(View.GONE);
                    btn_retry.setVisibility(View.VISIBLE);
                    list_contacts.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Not connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onSuccess(JSONObject success, String uri) {
        pb.setVisibility(View.GONE);
        btn_retry.setVisibility(View.GONE);
        list_contacts.setVisibility(View.VISIBLE);
        Log.d("k9_resp", "succ=" + success.toString());
        final MyResponse resp = new Gson().fromJson(success.toString(), MyResponse.class);
        for (int i = 0; i < resp.getContacts().length; i++) {
//            Log.d("k9_item", resp.getContacts()[i].getId() + " | " + resp.getContacts()[i].getName());
            resp.getContacts()[i].setIsFavorite("false");
            resp.getContacts()[i].setIsToBeReminded("false");
            DbHandler.addContactToDb(resp.getContacts()[i].getId(), new Gson().toJson(resp.getContacts()[i]));
        }

        mContactsAdapter = new ContactsAdapter(getActivity(), DbHandler.fetchContactsList(), true);
        list_contacts.setAdapter(mContactsAdapter);
    }

    @Override
    public void onFailure(JSONObject failure, String uri) {
        Log.d("k9_resp", "fail=" + failure.toString());
        pb.setVisibility(View.GONE);
        btn_retry.setVisibility(View.VISIBLE);
        list_contacts.setVisibility(View.GONE);
    }

}
