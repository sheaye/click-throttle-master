package com.sheaye.sample.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sheaye.sample.R;

import throttle.BindClick;

public class MainActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @BindClick({R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView3})
    public void onClick(View view) {
        Toast.makeText(this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
    }

}
