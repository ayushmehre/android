package com.retindode.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 8;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.retindode.screens", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String userObj = prefs.getString("userObj", null);
        if (userObj != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("6170986742-9b5q5c61rsqtqn6pihkb30d53lfdddr8.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(v -> {
                onClickGoogle(null);
        });

        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);
        //loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        onFBSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "ERROR: "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                        // App code
                    }
                });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            String name = Profile.getCurrentProfile().getFirstName();
            Toast.makeText(this, "Logged in as " + name, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Not Logged in ", Toast.LENGTH_SHORT).show();
        }
    }

    private void onFBSuccess(LoginResult loginResult) {
        try {
            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            // App code
            final Profile currentProfile = Profile.getCurrentProfile();
//                        String name = currentProfile.getFirstName() + " " + currentProfile.getLastName();

            final String accessToken = loginResult.getAccessToken().getUserId();

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject jsonObject,
                                                GraphResponse response) {

                            // Getting FB User Data and checking for null
                            Bundle facebookData = getFacebookData(jsonObject);
                            String email = "";
                            String first_name = "";
                            String last_name = "";
                            String profile_pic = "";

                            if (facebookData.getString("email") != null && !TextUtils.isEmpty(facebookData.getString("email")))
                                email = facebookData.getString("email");
                            else
                                email = "";

                            if (facebookData.getString("first_name") != null && !TextUtils.isEmpty(facebookData.getString("first_name")))
                                first_name = facebookData.getString("first_name");
                            else
                                first_name = "";

                            if (facebookData.getString("last_name") != null && !TextUtils.isEmpty(facebookData.getString("last_name")))
                                last_name = facebookData.getString("last_name");
                            else
                                last_name = "";

                            if (facebookData.getString("profile_pic") != null && !TextUtils.isEmpty(facebookData.getString("profile_pic")))
                                profile_pic = facebookData.getString("profile_pic");
                            else
                                profile_pic = "";

                            //sendValues(first_name+" "+last_name,email, "", "", accessToken, "Facebook",profile_pic);
                            Toast.makeText(LoginActivity.this, first_name + " " + last_name, Toast.LENGTH_SHORT).show();
//                            EditText fullname = findViewById(R.id.fullname);
//                            EditText emailET = findViewById(R.id.email);
//
//                            fullname.setText(first_name + " " + last_name);
//                            emailET.setText(email);
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,first_name,last_name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));


        } catch (Exception e) {
            Log.d("TAG", "BUNDLE Exception : " + e.toString());
        }
        return bundle;
    }


    public void openNextScreen(View view) {
        startActivity(new Intent(this, NewAccountActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        if (account != null) {
            Toast.makeText(this, "Already Logged with google as " + account.getEmail(), Toast.LENGTH_SHORT).show();
        }
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginWithNumberActivity.class));
    }

    public void onClickGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            assert account != null;

            Toast.makeText(this, account.getEmail()+" signed in", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE SIGNIN", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
            Toast.makeText(this, "Something went wrong, try login with number instead", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Code: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View view) {
        loginButton.performClick();
    }
}
