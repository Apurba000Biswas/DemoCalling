package com.zaynax.democallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            IO.Options options = new IO.Options();
            options.query = "auth=Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwMTRkYjY1M2U2MjMzZDI0ZmQ5NjBiMCIsImlhdCI6MTYxMjc2MjY3MywiZXhwIjoxNjEzMzY3NDczfQ.D8gDs5Glfry7A1XOogWmQAeJeb2QCEVaZndDy40InT6asvL0yuCLHCkAa6ea7u6SDcx0PC6dZ90MX-mBsx0OkRaZsFMdceOgL-OGJy85eMm2j04mdL-C9gFcwnef_dvttu7yr2Tb4zEi7hwcZq67thEgkmNVzRfoFQ1Ya8xmMESuqRmoGXM4e9KdCmt7HgVb_C5gjWQkEKGvnY_O1vqSRdTK37dDcgytOvmHedBQQKy0rN_x7fj3P0kWRe_NfRyw80tfo5_LS0burCw_7oAEdHx0dbP2k6dw83h82Vr6i88_Emhx0_zTAPHxmz8ws69_ZT6zP41p0x7-tFOxdBBipQ";
            mSocket = IO.socket("http://10.10.0.69:9999", options);
        } catch (URISyntaxException e) {
            Log.d("AAA", "*** Exception : " + e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mSocket.connect();


        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("message", onNewMessage);
        mSocket.on("INCOMING_CALL", onIncomingCall);
        mSocket.connect();
        setJitsiMeet();

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    JSONObject data = (JSONObject) args[1];
                    Log.d("AAA", "Called from Socket : " + args[1].toString());
                    //showIncomingCallScreen();
                }
            });
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("AAA", "Connected!");
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("AAA", "Error connecting: " + args[0].toString() );
                }
            });
        }
    };

    private Emitter.Listener onIncomingCall = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    Intent callIntent = new Intent(MainActivity.this, CallViewActivity.class);
//                    startActivity(callIntent);

                    /*
                    * {doctorID: string, roomID:string}
                    * */
                    // flow
                    // show incoming call screen
                    // if user accepts : call <SocketClass>.onCallAccepted() ->launch jitsi activity
                    // if user accepts : call <SocketClass>.onCallRejected() ->launch jitsi activity
//
                    showIncomingCallScreen();
                }
            });
        }
    };

    private void showIncomingCallScreen(){
        Intent callIntent = new Intent(MainActivity.this, CallViewActivity.class);
        startActivity(callIntent);
    }

    public void onCallRejected() {
        this.mSocket.emit("CALL_REJECTED", "JSON -> {roomID:string, doctorID:string}");
    }
    public void onCallAccepted() {
        this.mSocket.emit("CALL_ACCEPTED", "JSON -> {roomID:string, doctorID:string}");
    }

    private void setJitsiMeet(){
        // Initialize default options for Jitsi Meet conferences.
        URL serverURL;
        try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                // When using JaaS, set the obtained JWT here
                //.setToken("MyJWT")
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
    }

}