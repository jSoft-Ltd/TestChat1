package com.example.pappu_pc.testchat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pappu_pc.testchat.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Messages extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String str_chat_email,str_chat_token;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Message> messages;
    private Button buttonSend;
    private EditText editTextMessage;
    TextView t;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messege);
        editTextMessage= (EditText) findViewById(R.id.editTextMessage);
        buttonSend= (Button) findViewById(R.id.buttonSend);
        str_chat_email=getIntent().getStringExtra("chat_email");
        str_chat_token=getIntent().getStringExtra("chat_token");
        //Displaying dialog while the chat room is being ready
        dialog = new ProgressDialog(this);
        dialog.setMessage("Opening chat room");
        dialog.show();
        //Initializing recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        messages = new ArrayList<>();
        fetchMessages();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    //Getting message data
                    String name = intent.getStringExtra("name");
                    String message = intent.getStringExtra("message");

                    //processing the message to add it in current thread
                    processMessage(name, message,"7/12/2017");
                }
            }
        };
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storemessage();
                send();
            }
        });


    }
    private void processMessage(String from, String message, String time) {
        Message messagObject = new Message();
        messagObject.setMessage(message);
        messagObject.setFrom(from);
        messagObject.setTime(time);
        messages.add(messagObject);
        adapter.notifyDataSetChanged();
        scrollToBottom();
    }
    public void send(){
        String url = "http://appincubatorbd.xyz/chat/push-notification.php";
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String email = sharedPreferences.getString("MyEmail","null");
        final String message=editTextMessage.getText().toString();
        editTextMessage.setText("");
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("message",message);
                params.put("token",str_chat_token);
                params.put("title",email);
                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);

    }
    public void storemessage(){
        String url = "http://appincubatorbd.xyz/chat/messages.php";
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String email = sharedPreferences.getString("MyEmail","null");
        final String message=editTextMessage.getText().toString();
        final String time=getTimeStamp();
        Message m=new Message();
        m.setTime(time);
        m.setMessage(message);
        m.setTo(str_chat_email);
        m.setFrom(email);
        messages.add(m);
        adapter.notifyDataSetChanged();
        scrollToBottom();
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_to",str_chat_email);
                params.put("post_from",email);
                params.put("post_mgs",message);
                params.put("post_time",time);
                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);

    }
    public void fetchMessages() {
        String url = "http://appincubatorbd.xyz/chat/messages-json.php";
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final String email = sharedPreferences.getString("MyEmail","null");
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String message = obj.getString("message");
                        String to = obj.getString("to");
                        String from = obj.getString("from");
                        Message messagObject = new Message();
                        messagObject.setMessage(message);
                        messagObject.setTo(to);
                        messagObject.setFrom(from);
                        messagObject.setTime(obj.getString("created_at"));
                        messages.add(messagObject);
                    }

                    adapter = new ThreadAdapter(getApplicationContext(), messages,str_chat_email);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_to",str_chat_email);
                params.put("post_from",email);



                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);


    }
    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
    }
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }


}
