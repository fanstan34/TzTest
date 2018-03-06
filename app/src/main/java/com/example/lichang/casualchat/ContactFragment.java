package com.example.lichang.casualchat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lichang.casualchat.db.CCUser;
import com.example.lichang.casualchat.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.adapter.EMACallSession;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment{


    private ListView listView;
    private List<String> usernames;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact,container,false);
        button = (Button)view.findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddContactActivity.class));
            }
        });
        listView = (ListView)view.findViewById(R.id.lv_contact);


        for (int i=0;i<usernames.indexOf(usernames);i++){
            Map<String,Object> item = new HashMap<String,Object>();
          //  item.put("image",)
        }
        getContactList();
    //    adapter = new ContactAdapter(getContext(),usernames);
    //    listView.setAdapter(new ArrayAdapter<String>(this,R.layout.item_contact,getContactList()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  startActivity(new Intent(getActivity(),ChatActivity.class).putExtra("username",adapter.getItem(position)));

            }
        });
        return view;

    }

    List<String> user = new ArrayList<String>();
    private List<String> getContactList() {
        try {

          user = EMClient.getInstance().contactManager().getAllContactsFromServer();

        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        return user;
    }

}

