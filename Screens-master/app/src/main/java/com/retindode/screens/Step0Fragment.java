package com.retindode.screens;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class Step0Fragment extends Fragment {

    @NonNull
    @BindView(R.id.loan_amount)
    EditText loan_amount;

    @NonNull
    @BindView(R.id.tenure)
    EditText tenure;


    public Step0Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_step0, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setOptions(tenure, new CharSequence[]{
                "15 day - Payday",
                "30 day - Payday"
//                "3 month - Short-term Personal Loan",
//                "6 month - Short-term Personal Loan",
//                "Education Fee Creditline",
//                "Digital Creditline",
//                "18 month - Long-term Personal Loan",
//                "24 month - Long-term Personal Loan"
        }, getActivity());

        update();

        if(BookingActivity.autofill){
            autofill();
        }
    }

    private void autofill() {
        loan_amount.setText("25000");
        tenure.setText("15 day - Payday");
    }

    static void setOptions(EditText editText, CharSequence[] items, Activity activity) {
        editText.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setItems(items, (dialog, which) -> {
                editText.setText(items[which]);
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        editText.setFocusable(false);
    }

    public boolean validate() {
        boolean b;
        b = Step1Fragment.setAsRequired(loan_amount, tenure);

        if (tenure.getText().toString().equalsIgnoreCase("15 day - Payday")) {
            //loan amount =>3000
            //maximum loan amount = income * (40 / 100);
        }
        if (tenure.getText().toString().equalsIgnoreCase("30 day - Payday")) {
            // loan amount =>5000
            //  maximum loan amount = income * (60 / 100);
        }
        return b;
    }

    @Override
    public void onResume() {
        super.onResume();
        //update();
    }

    public String getProduct() {
        return tenure.getText().toString();
    }

    public String getAmount() {
        return loan_amount.getText().toString();
    }

    public void update() {
        if (BookingActivity.bookingJSONObject != null) {
            try {
                loan_amount.setText(BookingActivity.bookingJSONObject.getString("amount"));
                tenure.setText(BookingActivity.bookingJSONObject.getString("product"));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                BookingActivity.pager.setCurrentItem(0);
            }
        }
    }
}
