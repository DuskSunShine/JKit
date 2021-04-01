package com.scy.jkit.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.scy.core.common.JKit;
import com.scy.core.common.LiveBus;
import com.scy.jkit.R;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LiveBus.get().subscribe("BUS")
                .observe(this, o -> {
                    int v= (int) o;
                    JKit.log("接收MainActivity2:"+v);
                });
    }

    private int value=10;

    public void onClick(View view) {
        value++;
        LiveBus.get().sendEvent("BUS",value);
    }
}