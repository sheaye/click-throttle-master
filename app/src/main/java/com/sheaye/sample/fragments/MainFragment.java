package com.sheaye.sample.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sheaye.sample.R;
import com.sheaye.sample.adapters.ListAdapter;

import throttle.BindClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BasicFragment {


    protected ListView mListView;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.m_list_view);
        ListAdapter adapter = new ListAdapter();
        mListView.setAdapter(adapter);
    }

    @BindClick(R.id.hello_blank_text)
    public void onClick(View view){
        Toast.makeText(getContext(), "hello_blank_text", Toast.LENGTH_SHORT).show();
    }
}
