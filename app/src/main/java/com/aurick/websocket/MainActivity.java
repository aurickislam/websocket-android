package com.aurick.websocket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aurick.websocket.service.WebsocketClient;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView textView;

    private WebsocketClient c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            @Override
            public void run() {
                initWebsocket();
            }
        }.start();

        final EditText editText = findViewById(R.id.message_input);

        final Button button = findViewById(R.id.broadcast);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("Button Click");
                sentMessage(editText.getText().toString());
            }
        });

        textView = findViewById(R.id.message_box);
    }

    void initWebsocket() {
        try {
            c = new WebsocketClient(new URI("ws://192.168.88.2:8999/broadcast"));
            c.connect();
            c.addMessageHandler(new WebsocketClient.MessageHandler() {
                @Override
                public void handleMessage(String message) {
                    System.out.println("received: " + message);
                    textView.setText(message);
                }
            });


            /*c.connectBlocking();
            c.send("hi");
            Thread.sleep(20000);
            c.closeBlocking();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sentMessage(String message) {
        c.send(message);
        /*try {
            c.reconnectBlocking();
            c.send("Broadcasting from Android");
            c.closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}