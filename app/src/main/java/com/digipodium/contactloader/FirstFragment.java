package com.digipodium.contactloader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.digipodium.contactloader.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {
    static final int REQUEST_SELECT_PHONE_NUMBER_1 = 1;
    static final int REQUEST_SELECT_PHONE_NUMBER_2 = 2;

    private FragmentFirstBinding bind;
    private String[] contact1, contact2;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentFirstBinding.bind(view);
        bind.btnContact1.setOnClickListener(view1 -> {
            selectContact(1);
        });
        bind.btnContact2.setOnClickListener(view1 -> {
            selectContact(2);
        });

    }

    public void selectContact(int contactNum) {
        // Start an activity for the user to pick a phone number from contacts
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            switch (contactNum) {
                case 1:
                    startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER_1);
                    break;
                case 2:
                    startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER_2);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_PHONE_NUMBER_1:
                contact1 = loadContact(resultCode, data, bind.textSelCon1);
                break;
            case REQUEST_SELECT_PHONE_NUMBER_2:
                contact2 = loadContact(resultCode, data, bind.textSelCon2);
                break;
        }
    }

    private String[] loadContact(int resultCode, Intent data, TextView textView) {
        if (resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = cursor.getString(numberIndex);
                textView.setText(String.format("Name : %s\nNumber : %s", name, number));
                return new String[]{name, number};
            }
        }
        return null;
    }
}