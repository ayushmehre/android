package com.retindode.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aquery.AQuery;
import com.aquery.listener.QueryNetworkListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    public static int requestCODE;
    public static ViewPager pager;
    public static boolean autofill = true;
    private Step0Fragment step0Fragmnet;
    private Step1Fragment step1Fragment;
    private Step2Fragment step2Fragment;
    private Step3Fragment step3Fragment;
    private Step4Fragment step4Fragment;
    private String user_id;
    private String name;
    private String email;
    private String mobile;
    private boolean pass = false;
    public static JSONObject bookingJSONObject;
    private Map<String, String> latestMap;
    private SharedPreferences prefs;
    private int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String userObj = prefs.getString("userObj", null);
        Log.d("USEROBJECT", userObj + "");
        try {
            JSONObject userObject = new JSONObject(userObj);
            user_id = userObject.getString("id");
            name = userObject.getString("name");
            email = userObject.getString("email");
            mobile = userObject.getString("mobile");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        String saved = prefs.getString("saved", null);
        int step = prefs.getInt("step", 0);
        if (saved != null) {
            try {
                bookingJSONObject = new JSONObject(saved);
                Toast.makeText(this, "Previously saved application found", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setupViewPager();

        pager.setCurrentItem(step);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager() {
        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(10);
        ArrayList<Pair<String, Fragment>> fragments = new ArrayList<>();

        step0Fragmnet = new Step0Fragment();
        fragments.add(new Pair<>("Select Date", step0Fragmnet));

        step1Fragment = new Step1Fragment();
        fragments.add(new Pair<>("Select Date", step1Fragment));

        step2Fragment = new Step2Fragment();
        fragments.add(new Pair<>("Select Date", step2Fragment));

        step3Fragment = new Step3Fragment();
        fragments.add(new Pair<>("Select Date", step3Fragment));

        step4Fragment = new Step4Fragment();
        fragments.add(new Pair<>("Select Date", step4Fragment));

        MyPagerAdapter adapter = new MyPagerAdapter(fragments, getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(5);
    }

    public void next(View view) {
        switch (pager.getCurrentItem()) {
            case 0:
                if (pass || step0Fragmnet.validate()) {
                    Map<String, String> map = new HashMap<>();
                    map = getMap0(map);
                    Map<String, String> finalMap = map;
                    new AQuery(this).ajax("https://credin.in/app/api/submit-data/first_step")
                            .showLoading()
                            .postForm(map).response(new QueryNetworkListener() {
                        @Override
                        public void response(String s, Throwable throwable) {
                            Log.d("RESPONSE", s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                                    latestMap = finalMap;
                                    step = 1;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
                break;
            case 1:
                if (pass || step1Fragment.validate()) {
                    Map<String, String> form = new HashMap<>();
                    form.put("fname", step1Fragment.fName.getText().toString());
                    form.put("lname", step1Fragment.lName.getText().toString());
                    form.put("pan_no", step1Fragment.pan.getText().toString());
                    new AQuery(this).ajax("https://credin.in/app/api/pan_verification")
                            .postForm(form).showLoading().response(new QueryNetworkListener() {
                        @Override
                        public void response(String s, Throwable throwable) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                                    latestMap = getMap1(new HashMap<>());
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(BookingActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case 2:
                if (pass || step2Fragment.validate()) {
                    Map<String, String> map = new HashMap<>();
                    getMap2(map);
                    new AQuery(this).ajax("https://credin.in/app/api/submit-data/second_step")
                            .showLoading()
                            .postForm(map).response(new QueryNetworkListener() {
                        @Override
                        public void response(String s, Throwable throwable) {
                            Log.d("RESPONSE", s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                                    latestMap = map;
                                    step = 3;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
                break;
            case 3:
                if (pass || step3Fragment.validate()) {
                    Map<String, String> map = new HashMap<>();
                    getMap2(map);
                    getMap3(map);


                    new AQuery(this).ajax("https://credin.in/app/api/submit-data/third_step")
                            .showLoading()
                            .postForm(map).response(new QueryNetworkListener() {
                        @Override
                        public void response(String s, Throwable throwable) {
                            Log.d("RESPONSE", s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                                    latestMap = map;
                                    step = 4;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
                break;
            case 4:
                if (pass || step4Fragment.validate()) {
                    Map<String, String> map = new HashMap<>();
                    getMap2(map);
                    getMap3(map);

                    //  for (int i = 0; i < 5; i++) {
                    call4thStep(map, 0);
                }
                //  }
                break;
        }
    }

    private void call4thStep(Map<String, String> map, int i) {
        getMap4(map, i);
        new AQuery(this).ajax("https://credin.in/app/api/submit-data/forth_step")
                .showLoading()
                //.postMultipartBody(build)
                .postForm(map).response((s, throwable) -> {
            Log.d("RESPONSE", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
//                    pager.setCurrentItem(pager.getCurrentItem() + 1);
//                    finish();
                    if (i<4) {
                        call4thStep(map, i + 1);
                    }else{
                    //    if( step4Fragment.panImage!=null && step4Fragment.adharImage!=null && step4Fragment.picImage!=null && step4Fragment.companyImage!=null) {
                            finish();
                            Toast.makeText(this, "Application successfully uploaded", Toast.LENGTH_SHORT).show();
                    //    }
                      //  else
                       //     Toast.makeText(getApplicationContext(),"Please upload all images",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String message = jsonObject.getString("message");
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMap4(Map<String, String> map, int step) {
        map.put("pan_card", "");
        map.put("aadhaar", "");
        map.put("photo", "");
        map.put("cancel_cheque", "");
        map.put("bank_statements", "");
        map.put("final", "false");
        switch (step) {
            case 0:
                map.put("pan_card", step4Fragment.panImage);
                map.put("final", "false");
                break;
            case 1:
                map.put("aadhaar", step4Fragment.adharImage);
                map.put("final", "false");
                break;
            case 2:
                map.put("photo", step4Fragment.picImage);
                map.put("final", "false");
                break;
            case 3:
                map.put("cancel_cheque", step4Fragment.companyImage);
                map.put("final", "false");
                break;
            case 4:
                map.put("bank_statements", step4Fragment.pdfBase64);
                map.put("final", "true");
                break;
        }
    }

    private Map<String, String> getMap0(Map<String, String> map) {
        map.put("user_id", user_id);
        map.put("name", name);
        map.put("email", email);
        map.put("product", step0Fragmnet.getProduct());
        map.put("mobile", mobile);
        map.put("amount", step0Fragmnet.getAmount());
        return map;
    }

    private Map<String, String> getMap3(Map<String, String> map) {
        map.put("office_address", step3Fragment.officeAddress.getStreet());
        map.put("office_postal_code", step3Fragment.officeAddress.getPostalCode());
        map.put("office_city", step3Fragment.officeAddress.getCity());
        map.put("office_state", step3Fragment.officeAddress.getState());
        map.put("office_phone", step3Fragment.office_landline.getText().toString());
        map.put("office_hr", step3Fragment.hr_contact.getText().toString());
        map.put("official_email", "");
        map.put("ifsc", step3Fragment.ifsc.getText().toString());
        map.put("bank_acc", step3Fragment.acc_number.getText().toString());
        map.put("ref", step3Fragment.relation.getText().toString());
        map.put("ref_contact", step3Fragment.contact.getText().toString());
        map.put("friend_contact", step3Fragment.contact.getText().toString());
        map.put("ref_name", step3Fragment.family_ref.getText().toString());
        map.put("ref_friend_name", step3Fragment.friend_reference_name.getText().toString());
        map.put("loan_purpose", step3Fragment.loan_purpose.getText().toString());
        return map;
    }

    private Map<String, String> getMap2(Map<String, String> map) {
        map.put("fname", step1Fragment.fName.getText().toString());
        map.put("lname", step1Fragment.lName.getText().toString());
        map.put("user_id", user_id);
        map.put("name", name);
        map.put("email", email);
        map.put("product", step0Fragmnet.getProduct());
        map.put("mobile", mobile);
        map.put("amount", step0Fragmnet.getAmount());
        map.put("pan_no", step1Fragment.pan.getText().toString());
        map.put("designation", step2Fragment.designation.getText().toString());
        map.put("active_loans", step2Fragment.active_loans.getText().toString());
        map.put("income", step2Fragment.income.getText().toString());
        String date = step1Fragment.dob.getText().toString();
        try {
            Date parse = Data.userDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            String format = simpleDateFormat.format(parse);
            map.put("dob", format);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        map.put("type", step1Fragment.residence.getText().toString());
        map.put("gender", step1Fragment.gender.getText().toString());
        map.put("residence_type", step1Fragment.residence.getText().toString());
        map.put("active_emi_month", step2Fragment.active_emi_amount.getText().toString());
        map.put("permanent_house_no", step1Fragment.permanentAddress.getStreet());
        map.put("permanent_street_address", step1Fragment.permanentAddress.getStreet());
        map.put("permanent_area", step1Fragment.permanentAddress.getArea());
        map.put("permanent_landmark", step1Fragment.permanentAddress.getLandmark());
        map.put("permanent_address_pincode", step1Fragment.permanentAddress.getPostalCode());
        map.put("permanent_city", step1Fragment.permanentAddress.getCity());
        map.put("permanent_state", step1Fragment.permanentAddress.getState());
        map.put("house_no", step1Fragment.addressObject.getHouseNo());
        map.put("street_address", step1Fragment.addressObject.getStreet());
        map.put("area", step1Fragment.addressObject.getArea());
        map.put("landmark", step1Fragment.addressObject.getLandmark());
        map.put("pincode", step1Fragment.addressObject.getPostalCode());
        map.put("city", step1Fragment.addressObject.getCity());
        map.put("state", step1Fragment.addressObject.getState());
        map.put("employer_name", step2Fragment.employer_name.getText().toString());
        map.put("no_of_months_in_current_employment", step2Fragment.duration.getText().toString());
        map.put("total_work_ex", step2Fragment.total_exp.getText().toString());
        map.put("mode_of_salary", step2Fragment.salary.getText().toString());
        map.put("qualification", step1Fragment.education_qualification.getText().toString());
        map.put("marital_status", step1Fragment.martitial_status.getText().toString());
        map.put("type_of_prof", step2Fragment.type_of_profession.getText().toString());
        return map;
    }

    private Map<String, String> getMap1(Map<String, String> map) {
        map.put("fname", step1Fragment.fName.getText().toString());
        map.put("lname", step1Fragment.lName.getText().toString());
        map.put("user_id", user_id);
        map.put("name", name);
        map.put("email", email);
        map.put("product", step0Fragmnet.getProduct());
        map.put("mobile", mobile);
        map.put("amount", step0Fragmnet.getAmount());
        map.put("pan_no", step1Fragment.pan.getText().toString());
        String date = step1Fragment.dob.getText().toString();
        try {
            Date parse = Data.userDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            String format = simpleDateFormat.format(parse);
            map.put("dob", format);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        map.put("type", step1Fragment.residence.getText().toString());
        map.put("gender", step1Fragment.gender.getText().toString());
        map.put("residence_type", step1Fragment.residence.getText().toString());
        map.put("permanent_house_no", step1Fragment.permanentAddress.getStreet());
        map.put("permanent_street_address", step1Fragment.permanentAddress.getStreet());
        map.put("permanent_area", step1Fragment.permanentAddress.getArea());
        map.put("permanent_landmark", step1Fragment.permanentAddress.getLandmark());
        map.put("permanent_address_pincode", step1Fragment.permanentAddress.getPostalCode());
        map.put("permanent_city", step1Fragment.permanentAddress.getCity());
        map.put("permanent_state", step1Fragment.permanentAddress.getState());
        map.put("house_no", step1Fragment.addressObject.getHouseNo());
        map.put("street_address", step1Fragment.addressObject.getStreet());
        map.put("area", step1Fragment.addressObject.getArea());
        map.put("landmark", step1Fragment.addressObject.getLandmark());
        map.put("pincode", step1Fragment.addressObject.getPostalCode());
        map.put("city", step1Fragment.addressObject.getCity());
        map.put("state", step1Fragment.addressObject.getState());
        map.put("qualification", step1Fragment.education_qualification.getText().toString());
        map.put("marital_status", step1Fragment.martitial_status.getText().toString());
        return map;
    }

    public void back(View view) {
        if (pager.getCurrentItem() == 0) {
            onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 45) {
            try {
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                step4Fragment.setPDF(files.get(0).getPath());
            }catch (NullPointerException e){}
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            //pic.setImageURI(fileUri);

            switch (requestCODE) {
                case Step4Fragment.PAN:
                    step4Fragment.setImage(Step4Fragment.PAN, fileUri);
                    break;
                case Step4Fragment.ADHAR:
                    step4Fragment.setImage(Step4Fragment.ADHAR, fileUri);
                    break;
                case Step4Fragment.COMPANY:
                    step4Fragment.setImage(Step4Fragment.COMPANY, fileUri);
                    break;
                case Step4Fragment.PIC:
                    step4Fragment.setImage(Step4Fragment.PIC, fileUri);
                    break;

            }

            //You can get File object from intent
            File file;
            file = ImagePicker.Companion.getFile(data);

            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
        builder.setTitle("Leave?");
        builder.setMessage("Do you want to save this application?");
        builder.setPositiveButton("Save", (dialog, which) -> {
            if (true) {
                int currentItem = pager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        latestMap = getMap0(new HashMap<>());
                        break;
                    case 1:
                        latestMap = getMap1(new HashMap<>());
                        break;
                    case 2:
                        latestMap = getMap2(new HashMap<>());
                        break;
                    case 3:
                        latestMap = getMap3(new HashMap<>());
                        break;
                }
                bookingJSONObject = new JSONObject(latestMap);
                prefs.edit().putString("saved", bookingJSONObject.toString()).apply();
                prefs.edit().putInt("step", pager.getCurrentItem()).apply();
                Toast.makeText(this, "Application Saved", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
        builder.setNegativeButton("Don't save", (dialog, which) -> finish());
        builder.show();
    }

    public static void setBookingJSONObject(JSONObject bookingJSONObject) {
        BookingActivity.bookingJSONObject = bookingJSONObject;
    }
}
