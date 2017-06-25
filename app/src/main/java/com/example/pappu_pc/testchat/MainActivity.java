package com.example.pappu_pc.testchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String token,email,name;
    EditText edt_email,edt_name;
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        edt_email= (EditText) findViewById(R.id.email);
        edt_name= (EditText) findViewById(R.id.name);
        btn_start= (Button) findViewById(R.id.start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=edt_email.getText().toString();
                name=edt_name.getText().toString();
                if(!email.isEmpty()&&!name.isEmpty()){
                    storeToken(email,name);
                }
                else {
                    Toast.makeText(MainActivity.this,"Fill email and Nmae",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    void storeToken(final String email,final String name) {
        // Tag used to cancel the request
       // String tag_string_req = "req_login";
        String url = "http://appincubatorbd.xyz/chat/users.php";
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        token=sharedPreferences.getString("token","null");
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                if(response.equals("s")){
                    Intent intent=new Intent(MainActivity.this,ChatRooms.class);
                    editor.putString("MyEmail",email);
                    editor.putString("MyName",name);
                    editor.commit();
                    startActivity(intent);
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
                params.put("post_email", email);
                params.put("post_token", token);
                params.put("post_name", name);


                return params;
            }

        };

        sq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sq);
    }
}
