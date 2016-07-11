package com.allattentionhere.qykcontacts.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.allattentionhere.qykcontacts.Adapter.ContactsAdapter;
import com.allattentionhere.qykcontacts.Helper.DbHandler;
import com.allattentionhere.qykcontacts.Model.Contacts;
import com.allattentionhere.qykcontacts.R;




public class FavoritesFragment extends android.support.v4.app.Fragment  {

    LinearLayout ll_empty;
    ListView list_contacts;
    ContactsAdapter mContactsAdapter;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        ll_empty=(LinearLayout)v.findViewById(R.id.ll_empty);
        list_contacts = (ListView) v.findViewById(R.id.list_contacts);
        list_contacts.setEmptyView(ll_empty);

        Contacts[] mC = DbHandler.fetchFavoriteContacts();
        if (mC.length > 0) {
             mContactsAdapter = new ContactsAdapter(getActivity(), mC,false);
            list_contacts.setAdapter(mContactsAdapter);
        }
        return v;
    }


}
