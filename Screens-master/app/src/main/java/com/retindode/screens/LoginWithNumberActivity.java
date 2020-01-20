package com.retindode.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aquery.AQuery;
import com.aquery.listener.QueryNetworkListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginWithNumberActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.number)
    EditText number;
    @Nullable
    @BindView(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_number);
        ButterKnife.bind(this);
    }

    public void login(View view) {
        String number = this.number.getText().toString();
        String password = this.password.getText().toString();


        if (number.isEmpty()) {
            this.number.setError("Required");
            return;
        }
        if (password.isEmpty()) {
            this.password.setError("Required");
            return;
        }


        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("mobile", number);
        new AQuery(this).ajax("https://credin.in/app/api/user/login")
                .post(params).showLoading().response(new QueryNetworkListener() {
            @Override
            public void response(String s, Throwable throwable) {
                Log.d("RESPONSE", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("success")) {
                        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                        JSONObject data = jsonObject.getJSONObject("data");
                        preferences.edit().putString("userObj", data.toString()).apply();
                        startActivity(new Intent(LoginWithNumberActivity.this, MainActivity.class));
                        finishAffinity();
                    } else {
                        Toast.makeText(LoginWithNumberActivity.this,
                                jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
