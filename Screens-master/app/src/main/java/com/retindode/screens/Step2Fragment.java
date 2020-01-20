package com.retindode.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
public class Step2Fragment extends Fragment {

    @NonNull
    @BindView(R.id.type_of_profession)
    EditText type_of_profession;

    @NonNull
    @BindView(R.id.employer_name)
    EditText employer_name;

    @NonNull
    @BindView(R.id.designation)
    EditText designation;

    @NonNull
    @BindView(R.id.duration)
    EditText duration;

    @NonNull
    @BindView(R.id.total_exp)
    EditText total_exp;

    @NonNull
    @BindView(R.id.income)
    EditText income;

    @NonNull
    @BindView(R.id.salary)
    EditText salary;

    @NonNull
    @BindView(R.id.active_loans)
    EditText active_loans;

    @NonNull
    @BindView(R.id.active_emi_amount)
    EditText active_emi_amount;

    @NonNull
    @BindView(R.id.cb1)
    CheckBox cb1;

    @NonNull
    @BindView(R.id.cb2)
    CheckBox cb2;


    public Step2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_step2, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Step0Fragment.setOptions(type_of_profession, new CharSequence[]{
                "Salaried",
                "Self Employed",
                "Freelancer"
        }, getActivity());

        Step0Fragment.setOptions(salary, new CharSequence[]{
                "Cheque",
                "Cash",
                "Bank transfer"
        }, getActivity());

        update();
        if(BookingActivity.autofill){
            autofill();
        }

    }

    private void autofill() {
        type_of_profession.setText("Salaried");
        employer_name.setText("Retinodes");
        designation.setText("Dev");
        duration.setText("1");
        total_exp.setText("4");
        income.setText("20000");
        salary.setText("Cash");
        active_emi_amount.setText("0");
        active_loans.setText("0");
    }

    public boolean validate() {
        boolean b = true;

        b = Step1Fragment.setAsRequired(type_of_profession, employer_name, designation,
                duration, total_exp, income, salary, active_loans, active_emi_amount);


        if (!cb1.isChecked() && !cb2.isChecked()) {
            b = false;
            Toast.makeText(getActivity(), "Please accept all conditions", Toast.LENGTH_SHORT).show();
        }

        if (!type_of_profession.getText().toString().equalsIgnoreCase("Salaried")) {
            type_of_profession.setError("Only Salaried Person are allowed");
            Toast.makeText(getActivity(), "Only Salaried Person are eligible", Toast.LENGTH_LONG).show();
            b = false;
        }

        try {
            String s = income.getText().toString();
            if (Integer.parseInt(s) < 12000) {
                income.setError("Minimum salary should be Rs.12,000");
                b = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            b = false;
            income.setError(e.getMessage());
        }


        return b;
    }

    public void update() {
        JSONObject bookingJSONObject = BookingActivity.bookingJSONObject;
        if (bookingJSONObject == null) {
            return;
        }
        try {
            type_of_profession.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            employer_name.setText(bookingJSONObject.getString("employer_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            designation.setText(bookingJSONObject.getString("designation"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            duration.setText(bookingJSONObject.getString("no_of_months_in_current_employment"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            total_exp.setText(bookingJSONObject.getString("total_work_ex"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            income.setText(bookingJSONObject.getString("income"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            salary.setText(bookingJSONObject.getString("mode_of_salary"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            active_loans.setText(bookingJSONObject.getString("active_loans"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            active_emi_amount.setText(bookingJSONObject.getString("active_emi_month"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
