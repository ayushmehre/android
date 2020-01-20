package com.retindode.screens;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Step4Fragment extends Fragment {


    public static final int PAN = 1;
    public static final int ADHAR = 2;
    public static final int PIC = 3;
    public static final int COMPANY = 4;
    public static final int PICK_PDF_CODE = 45;
    @NonNull
    @BindView(R.id.pan)
    ImageView pan;

    @NonNull
    @BindView(R.id.adhar)
    ImageView aadhar;

    @NonNull
    @BindView(R.id.company_id)
    ImageView company_id;

    @NonNull
    @BindView(R.id.pic)
    ImageView pic;

    @NonNull
    @BindView(R.id.btn_pdf)
    Button pdf;

    public String panImage;
    public String companyImage;
    public String picImage;
    public String adharImage;
    public String pdfBase64;

    public Step4Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_step4, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setOnCLick(pan, PAN);
        setOnCLick(pic, PIC);
        setOnCLick(aadhar, ADHAR);
        setOnCLick(company_id, COMPANY);

        pdf.setOnClickListener(v -> {
            if(panImage!=null && adharImage!=null && picImage!=null && companyImage!=null) {
                Intent intent = new Intent(getActivity(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .setShowVideos(false).setShowAudios(false)
                        .enableImageCapture(false)
                        .setMaxSelection(1)
                        .setSkipZeroSizeFiles(true)
                        .setSuffixes(".pdf").setSingleChoiceMode(true)
                        .setShowFiles(true)
                        .build());
                getActivity().startActivityForResult(intent, PICK_PDF_CODE);
            }
            else
                Toast.makeText(getApplicationContext(),"Please upload other documents first",Toast.LENGTH_SHORT).show();

        });


    }

    private void setOnCLick(ImageView pan, int pan2) {
        pan.setOnClickListener(v -> {
            ImagePicker.Companion.with(getActivity())
                    //.galleryOnly()    //User can only select image from Gallery
                    .start();
            BookingActivity.requestCODE = pan2;
        });

    }


    public void setImage(int id, Uri fileUri) {
        switch (id) {
            case PAN:
                panImage = encodeImage(fileUri.getPath());
                //Log.d("BASE64", panImage);
                setImage(fileUri, pan);
                break;
            case ADHAR:
                adharImage = encodeImage(fileUri.getPath());
                setImage(fileUri, aadhar);
                break;
            case PIC:
                picImage = encodeImage(fileUri.getPath());
                setImage(fileUri, pic);
                break;
            case COMPANY:
                companyImage = encodeImage(fileUri.getPath());
                setImage(fileUri, company_id);
                break;
            case PICK_PDF_CODE:
                pdfBase64 = encodePDF(fileUri.getPath());
                pdf.setText(fileUri.getPath() + "");
                break;
        }
    }

    private void setImage(Uri fileUri, ImageView pan) {
        pan.setScaleType(ImageView.ScaleType.CENTER_CROP);
        pan.setAlpha(1.0f);
        pan.setPadding(0, 0, 0, 0);
        pan.setImageURI(fileUri);
    }

    public boolean validate() {
        if(panImage!=null && adharImage!=null && picImage!=null && companyImage!=null)
            return true;
        else{
            Toast.makeText(getApplicationContext(),"Please upload all documents",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private String encodeImage(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = "data:image/jpg;base64," + Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        Log.d("BASE64", encImage);
        return encImage;

    }

    private String encodePDF(String path) {
        try {
            File imagefile = new File(path);
            int size = (int) imagefile.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imagefile));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setPDF(String path) {
            String encodePDF = encodePDF(path);
            pdfBase64 = "data:application/pdf;base64," + encodePDF;
            pdf.setText(path);
      }
}
