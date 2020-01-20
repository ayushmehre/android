package com.retindode.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @NonNull
    @BindView(R.id.name)
    TextView nameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name", "");
        nameTV.setText("Hello " + name);
    }


    public void openNextScreen(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
