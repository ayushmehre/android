package com.retindode.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment());

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.item_1:
                    loadFragment(new HomeFragment());
                    break;
                case R.id.item_2:
                    loadFragment(new ProductsFragment());
                    break;
                case R.id.item_3:
                    loadFragment(new SupportFragment());
                    break;
            }
            return true;
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public void openApplication(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String saved = prefs.getString("saved", null);


        if (saved!=null) {
            //ContainerActivity.setFragment(new ApplicationsFragment(), "My Applications");
            startActivity(new Intent(this, BookingActivity.class));
            progressDialog.dismiss();
        }else{
            loadFragment(new ProductsFragment());
            Toast.makeText(this, "No Previously saved application found", Toast.LENGTH_LONG).show();
            bottomNavigationView.setSelectedItemId(R.id.item_2);
            progressDialog.dismiss();
        }
    }

    public void openLoans(View view) {
        ContainerActivity.setFragment(new LoanFragment(), "My Active Loans");
        startActivity(new Intent(this, ContainerActivity.class));
    }
}
