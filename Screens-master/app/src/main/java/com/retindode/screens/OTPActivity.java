package com.retindode.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aquery.AQuery;
import com.aquery.listener.QueryNetworkListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.retindode.screens.NewAccountActivity.numberString;

public class OTPActivity extends AppCompatActivity {

    @NonNull
    @BindView(R.id.number)
    TextView number;

    @NonNull
    @BindView(R.id.otp)
    TextView otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        ButterKnife.bind(this);
        number.setText(numberString + "");
    }


    public void openNextScreen(View view) {

        if (otp.getText().toString().isEmpty() || otp.getText().toString().length() < 4) {
            otp.setError("Invalid");
        }

        Map<String, String> params = new HashMap<>();
        //params.put("name", name);
        //params.put("email", email);
        params.put("otp", otp.getText().toString());
        params.put("user_id", NewAccountActivity.user_id);
        new AQuery(this).ajax("https://credin.in/app/api/registration/otp-verify")
                .postForm(params).showLoading().response(new QueryNetworkListener() {
            @Override
            public void response(String s, Throwable throwable) {
                Log.d("RESPONSE", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("success")) {
                       signup();
                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

       // startActivity(new Intent(this, WelcomeActivity.class));
    }

    private void signup() {
        Map<String, String> params = new HashMap<>();
        //params.put("name", name);
        //params.put("email", email);
        params.put("password", NewAccountActivity.password);
        params.put("name", NewAccountActivity.nameString);
        params.put("email", NewAccountActivity.emailString);
        params.put("user_id", NewAccountActivity.user_id);
        new AQuery(this).ajax("https://credin.in/app/api/registration/password-insert")
                .postForm(params).showLoading().response(new QueryNetworkListener() {
            @Override
            public void response(String s, Throwable throwable) {
                Log.d("RESPONSE", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getBoolean("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String name = data.getString("name");
                        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                        preferences.edit().putString("userObj", data.toString()).apply();
                        Intent intent = new Intent(OTPActivity.this, WelcomeActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
