package com.retindode.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
public class Step3Fragment extends Fragment {

    @NonNull
    @BindView(R.id.acc_holder_name)
    EditText acc_holder_name;

    @NonNull
    @BindView(R.id.acc_number)
    EditText acc_number;

    @NonNull
    @BindView(R.id.bank)
    EditText bank;

    @NonNull
    @BindView(R.id.ifsc)
    EditText ifsc;

    @NonNull
    @BindView(R.id.loan_purpose)
    EditText loan_purpose;

    @NonNull
    @BindView(R.id.office_address)
    EditText office_address;

    @NonNull
    @BindView(R.id.hr_contact)
    EditText hr_contact;

    @NonNull
    @BindView(R.id.office_landline)
    EditText office_landline;

    @NonNull
    @BindView(R.id.family_ref)
    EditText family_ref;

    @NonNull
    @BindView(R.id.relation)
    EditText relation;

    @NonNull
    @BindView(R.id.contact)
    EditText contact;

    @NonNull
    @BindView(R.id.friend_reference_name)
    EditText friend_reference_name;

    @NonNull
    @BindView(R.id.friend_reference_contact)
    EditText friend_reference_contact;

    public AddressObject officeAddress = new AddressObject();


    public Step3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_step3, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Step0Fragment.setOptions(loan_purpose, new CharSequence[]{
                "Home repair",
                "Home furnishing",
                "Travel expenses",
                "Marriage expenses",
                "Education",
                "Consumer durable",
                "Others"
        }, getActivity());

        Step0Fragment.setOptions(relation, new CharSequence[]{
                "Father",
                "Mother",
                "Spouse"
        }, getActivity());

        Step0Fragment.setOptions(bank, new CharSequence[]{
                "Allahabad Bank",
                "Andhra Bank",
                "Axis Bank",
                "Bank Of Baroda",
                "Bank Of India",
                "Bank Of Maharashtra",
                "Canara Bank",
                "City Union Bank",
                "Corporation Bank",
                "Federal Bank",
                "Axis Bank",
                "Kotak Bank",
                "HDFC Bank",
                "ICICI Bank",
                "IDBI Bank",
                "IndusInd Bank",
                "Janata Bank",
                "Karnataka Bank",
                "Oriental Bank Of Commerce",
                "PNB Bank",
                "State Bank Of India",
                "Syndicate Bank",
                "Tamilnad Mercantile Bank",
                "Uco Bank",
                "Union Bank",
                "United Bank Of India",
                "Yes Bank",
                "Other"
        }, getActivity());


        Step1Fragment.setAsAddressField(office_address, getActivity(), addressObject -> officeAddress = addressObject);
        //Step1Fragment.setAsAddressField(office_address, getActivity(), addressObject -> officeAddress = addressObject);

        //update();
        autofill();
    }

    private void autofill() {
        acc_holder_name.setText("s");
        acc_number.setText("s");
        bank.setText("s");
        ifsc.setText("s");
        loan_purpose.setText("s");

        officeAddress.setHouseNo("s");
        officeAddress.setStreet("s");
        officeAddress.setHouseNo("s");
        officeAddress.setCity("ss");
        officeAddress.setPostalCode("s");
        officeAddress.setState("ss");
        officeAddress.setArea("aa");

        office_address.setText(officeAddress.getSingleLineAddress());
        hr_contact.setText("ss");
        office_landline.setText("ss");
        family_ref.setText("ss");
        relation.setText("ss");
        contact.setText("ss");
        friend_reference_name.setText("ss");
        friend_reference_contact.setText("ss");
    }

    private void update() {
        JSONObject bookingJSONObject = BookingActivity.bookingJSONObject;
        if (bookingJSONObject == null) {
            return;
        }
        try {
            acc_holder_name.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            acc_number.setText(bookingJSONObject.getString("bank_acc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            bank.setText(bookingJSONObject.getString("bank"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ifsc.setText(bookingJSONObject.getString("ifsc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            loan_purpose.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            office_address.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            hr_contact.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            office_landline.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            family_ref.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            relation.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            friend_reference_name.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            friend_reference_contact.setText(bookingJSONObject.getString("type_of_prof"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean validate() {
        boolean b = true;
        b = Step1Fragment.setAsRequired(acc_holder_name, acc_number, bank,
                ifsc, loan_purpose, office_address, hr_contact, office_landline, family_ref, relation,
                contact, friend_reference_name, friend_reference_contact);

        return b;
    }
}
