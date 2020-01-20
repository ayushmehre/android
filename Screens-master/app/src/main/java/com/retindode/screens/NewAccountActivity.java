package com.retindode.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aquery.AQuery;
import com.aquery.listener.QueryNetworkListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewAccountActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.number)
    EditText number;
    @Nullable
    @BindView(R.id.password)
    EditText passwordEdit;
    @Nullable
    @BindView(R.id.name)
    EditText name;
    @Nullable
    @BindView(R.id.email)
    EditText email;
    public static String numberString = "";
    public static String user_id = "0";
    public static String nameString = "";
    public static String emailString = "";
    public static String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        ButterKnife.bind(this);
    }


    public void openNextScreen(View view) {
        nameString = this.name.getText().toString();
        numberString = this.number.getText().toString();
        emailString = this.email.getText().toString();
        password = this.passwordEdit.getText().toString();


        if (nameString.isEmpty()) {
            this.name.setError("Required");
            return;
        }
        if (numberString.isEmpty()) {
            this.number.setError("Required");
            return;
        }
        if (emailString.isEmpty()) {
            this.email.setError("Required");
            return;
        }
        if (password.isEmpty()) {
            this.passwordEdit.setError("Required");
            return;
        }


        Map<String, String> params = new HashMap<>();
        //params.put("name", name);
        //params.put("email", email);
        params.put("mobile", numberString);
        new AQuery(this).ajax("https://credin.in/app/api/registration/otp")
                .postForm(params).showLoading().response(new QueryNetworkListener() {
            @Override
            public void response(String s, Throwable throwable) {
                Log.d("RESPONSE", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("success")) {
                        user_id = jsonObject.getString("user_id");
                        startActivity(new Intent(NewAccountActivity.this, OTPActivity.class));
                    } else {
                        Toast.makeText(NewAccountActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void exit(View view) {
        finish();
    }
}
