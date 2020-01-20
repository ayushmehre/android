package com.retindode.screens;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class Step1Fragment extends Fragment {


    @NonNull
    @BindView(R.id.f_name)
    EditText fName;

    @NonNull
    @BindView(R.id.l_name)
    EditText lName;

    @NonNull
    @BindView(R.id.dob)
    EditText dob;

    @NonNull
    @BindView(R.id.gender)
    EditText gender;

    @NonNull
    @BindView(R.id.pan)
    EditText pan;

    @NonNull
    @BindView(R.id.adhar)
    EditText adhar;

    @NonNull
    @BindView(R.id.martitial_status)
    EditText martitial_status;

    @NonNull
    @BindView(R.id.residence)
    EditText residence;

    @NonNull
    @BindView(R.id.permanent_address)
    EditText permanent_address;

    @NonNull
    @BindView(R.id.current_address)
    EditText current_address;

    @NonNull
    @BindView(R.id.education_qualification)
    EditText education_qualification;

    private View inflate;
    public AddressObject addressObject = new AddressObject();
    public AddressObject permanentAddress = new AddressObject();

    public Step1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_step1, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        Calendar calendar = Calendar.getInstance();

        dob.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view13, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                dob.setText(Data.userDateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            datePickerDialog.show();
        });

        Step0Fragment.setOptions(gender, new CharSequence[]{"Male", "Female", "Transgender"}, getActivity());
        Step0Fragment.setOptions(martitial_status, new CharSequence[]{
                "Married",
                "Single",
                "Divorcee",
                "Widow"
        }, getActivity());
        Step0Fragment.setOptions(residence, new CharSequence[]{
                "Rented",
                "Owned",
                "Parental",
                "Company Provided"
        }, getActivity());
        Step0Fragment.setOptions(education_qualification, new CharSequence[]{
                "10th",
                "12th",
                "Graduate",
                "Post-Graduate"
        }, getActivity());

        setAsAddressField(current_address, getActivity(), (AddressObject addressObject) -> {
            this.addressObject = addressObject;
        });

        setAsAddressField(permanent_address, getActivity(), (AddressObject addressObject) -> {
            permanentAddress = addressObject;
        });

        update();
        if (BookingActivity.autofill) {
            autofill();
        }

    }

    private void autofill() {
        fName.setText("Ayush");
        lName.setText("Mehre");
        dob.setText("13 Jan 1995");
        gender.setText("Male");
        pan.setText("CUVPM5722L");

        addressObject.setPostalCode("2");
        addressObject.setArea("s");
        addressObject.setLandmark("s");
        addressObject.setCity("s");
        addressObject.setState("s");
        addressObject.setStreet("s");
        addressObject.setHouseNo("ss");

        permanentAddress.setPostalCode("2");
        permanentAddress.setArea("s");
        permanentAddress.setLandmark("s");
        permanentAddress.setCity("s");
        permanentAddress.setState("s");
        permanentAddress.setStreet("s");
        permanentAddress.setHouseNo("ss");

        current_address.setText(addressObject.getSingleLineAddress());
        permanent_address.setText(permanentAddress.getSingleLineAddress());
        education_qualification.setText("213");
        martitial_status.setText("Single");
        residence.setText("Owned");
    }

    static void setAsAddressField(EditText current_address, Activity activity, OnGetAddress onGetAddress) {
        current_address.setFocusable(false);
        current_address.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(R.layout.activity_addressdialog);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            View decorView = alertDialog.getWindow().getDecorView();
            TextView ok = decorView.findViewById(R.id.ok);
            EditText street = decorView.findViewById(R.id.street);
            EditText area = decorView.findViewById(R.id.area);
            EditText h_no = decorView.findViewById(R.id.h_no);
            EditText landmark = decorView.findViewById(R.id.landmark);
            EditText pincode = decorView.findViewById(R.id.pincode);
            EditText city = decorView.findViewById(R.id.city);
            Step0Fragment.setOptions(city, new CharSequence[]{"Vadodara", "Ahmedabad", "Anand", "Bharuch"}, activity);
            EditText state = decorView.findViewById(R.id.state);
            state.setText("Gujarat");
            state.setFocusable(false);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v1) {
                    if (this.validate()) {
                        AddressObject addressObject = new AddressObject();
                        addressObject.setArea(area.getText().toString());
                        addressObject.setStreet(street.getText().toString());
                        addressObject.setHouseNo(h_no.getText().toString());
                        addressObject.setLandmark(landmark.getText().toString());
                        addressObject.setPostalCode(pincode.getText().toString());
                        addressObject.setState(state.getText().toString());
                        addressObject.setCity(city.getText().toString());
                        onGetAddress.onGetAddress(addressObject);
                        alertDialog.dismiss();
                        current_address.setText(addressObject.getHouseNo()
                                + ", " + addressObject.getStreet() + ", "
                                + addressObject.getLandmark() + ", " + addressObject.getArea() + ", " +
                                addressObject.getCity() + ", " + addressObject.getState() + ", " + addressObject.getPostalCode());
                    }
                }

                private boolean validate() {
                    boolean b = setAsRequired(street, h_no, area, landmark, pincode, city, state);
                    return b;
                }
            });
        });
    }

    public boolean validate() {
        boolean b = true;

        b = setAsRequired(fName, lName, dob, gender, pan,
                martitial_status, residence, current_address,
                permanent_address, education_qualification);

        if (!isValidPAN()) {
            pan.setError("Invalid PAN Number");
            return false;
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = simpleDateFormat.parse(dob.getText().toString());

            int age = calculateAgeWithJava7(date, Calendar.getInstance().getTime());

            if (age < 21 || age > 55) {
                Toast.makeText(getActivity(), "Age should be between 21 & 55", Toast.LENGTH_LONG).show();
                dob.setError("Age should be between 21 & 55");
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            dob.setError(e.getMessage());
            return false;
        }


        return b;
    }

    public int calculateAgeWithJava7(
            Date birthDate,
            Date currentDate) {
        // validate inputs ...
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(formatter.format(birthDate));
        int d2 = Integer.parseInt(formatter.format(currentDate));
        int age = (d2 - d1) / 10000;
        return age;
    }

    private boolean isValidPAN() {
        String s = pan.getText().toString(); // get your editext value here
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(s);
// Check if pattern matches
        if (matcher.matches()) {
            Log.i("Matching", "Yes");
            return true;
        }
        return false;
    }

    static boolean setAsRequired(EditText... editTexts) {
        int count = 0;
        for (EditText editText : editTexts) {
            CharSequence hint = editText.getHint();
            if (editText.getText().toString().isEmpty()) {
                editText.setError("Required");
                return false;
            }
            count++;
        }
        return true;
    }

    public void update() {
        JSONObject bookingJSONObject = BookingActivity.bookingJSONObject;
        if (bookingJSONObject == null) {
            return;
        }
        try {
            fName.setText(bookingJSONObject.getString("fname") + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            lName.setText(bookingJSONObject.getString("lname") + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String dob = bookingJSONObject.getString("dob");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date date = simpleDateFormat.parse(dob);
            SimpleDateFormat userDateformat = new SimpleDateFormat("dd MMM yyyy");
            this.dob.setText(userDateformat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
            dob.setText(e.getMessage());
            dob.setText("");
        }
        try {
            gender.setText(bookingJSONObject.getString("gender"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pan.setText(bookingJSONObject.getString("pan_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            martitial_status.setText(bookingJSONObject.getString("marital_status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            residence.setText(bookingJSONObject.getString("residence_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addressObject.setHouseNo(bookingJSONObject.getString("house_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addressObject.setArea(bookingJSONObject.getString("area"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addressObject.setLandmark(bookingJSONObject.getString("landmark"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addressObject.setCity(bookingJSONObject.getString("city"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addressObject.setState(bookingJSONObject.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addressObject.setPostalCode(bookingJSONObject.getString("pincode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addressObject.setPostalCode(bookingJSONObject.getString("street_address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        current_address.setText(addressObject.getSingleLineAddress());
        try {
            permanentAddress.setHouseNo(bookingJSONObject.getString("permanent_house_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            permanentAddress.setStreet(bookingJSONObject.getString("permanent_street_address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            permanentAddress.setCity(bookingJSONObject.getString("permanent_city"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            permanentAddress.setState(bookingJSONObject.getString("permanent_state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            permanentAddress.setPostalCode(bookingJSONObject.getString("permanent_address_pincode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            permanentAddress.setArea(bookingJSONObject.getString("permanent_area"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            permanentAddress.setHouseNo(bookingJSONObject.getString("permanent_house_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            permanentAddress.setLandmark(bookingJSONObject.getString("permanent_landmark"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        permanent_address.setText(permanentAddress.getSingleLineAddress());
        try {
            education_qualification.setText(bookingJSONObject.getString("qualification"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (permanent_address.getText().toString().contains("null")) {
            permanent_address.setText("");
        }

        if (current_address.getText().toString().contains("null")) {
            current_address.setText("");
        }
    }
}
