package com.example.pappu_pc.testchat;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pappu_pc.testchat.adapter.ChatRoomsAdapter;
import com.example.pappu_pc.testchat.model.ChatRoom;
import com.example.pappu_pc.testchat.model.Message;
import com.example.pappu_pc.testchat.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRooms extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<User> data=new ArrayList<>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Displaying dialog while the chat room is being ready
        dialog = new ProgressDialog(this);
        dialog.setMessage("Opening chat room");
       // dialog.show();
        recyclerView= (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
      // adapter=new ChatRoomsAdapter(getApplicationContext(),data);
       // recyclerView.setAdapter(adapter);
        takeuser();


    }
public void takeuser(){
    String url = "http://appincubatorbd.xyz/chat/all-user-json.php";
    SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
    final String email = sharedPreferences.getString("MyEmail","null");
    StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
           // dialog.cancel();
            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonArray=new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    User user=new User();
                    user.setName(obj.getString("name"));
                    user.setEmail(obj.getString("email"));
                    user.setToken(obj.getString("token"));
                    data.add(user);
                }

                adapter=new ChatRoomsAdapter(data,getApplicationContext());
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
            params.put("post_email",email);




            return params;
        }

    };

    sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(sq);
}
public void send(){
    String url = "http://appincubatorbd.xyz/chat/all-user-json.php";
    SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
    final String email = sharedPreferences.getString("MyEmail","null");
    StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // dialog.cancel();
            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {



        }
    }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("post_email",email);




            return params;
        }

    };

    sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(sq);

}
}
