package com.zaynax.democallingapp;


import android.os.Bundle;

//import org.jitsi.meet.sdk.JitsiMeetActivity;

import org.jitsi.meet.sdk.JitsiMeetActivity;

import androidx.appcompat.app.AppCompatActivity;

public class CallViewActivity extends JitsiMeetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_view);
    }
}