package com.zaynax.democallingapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.Polling;
import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            IO.Options options = new IO.Options();
            options.query = "auth:" +
                    "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwMGU4N2M2MzExNWI0YTYyZTZlNjE2NCIsImlhdCI6MTYxMjY3NzY4OCwiZXhwIjoxNjEzMjgyNDg4fQ.rVCe0VQNJKLNzqeInuLAG_sTSUQpbQoNzANivZkzlUvDKJxD8QoqrL4iGKvuIPMvD6P3i914i-eig-IPHwC2AzEJ9mTJYhyx4BNAompOa4_21RuRF89-l8WzI5RE11_jAB5TSkZ6ETeGVJi0z-RHBAGIIgAjEs4erdFpwlO9b223B13N6cgSm-e0KeD-r6dzk_oX_wkOlbnOmJRQcJjvXIFb3VwcdwusPcBPd1moX7qUH-2L1l5wK1wwtlZRaA6De-ANgTQk2B1nxXAvB2jWyp5x8ICkcBGi5woV3nQ_LbpwbHvcH8D5fkftOxkCooB1WeImUVDUFZwi8j1XUt5OFw";
            mSocket = IO.socket("http://server:9999/realtime", options);

            Log.d("AAA", "Creating Socket");

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
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.connect();

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getParent().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("AAA", "Called from Socket");

                }
            });
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getParent().runOnUiThread(new Runnable() {
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
            getParent().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("AAA", "Error connecting");
                }
            });
        }
    };

}