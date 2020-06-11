package com.example.qhs.wallpapershopping.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qhs.wallpapershopping.AuthHelper;
import com.example.qhs.wallpapershopping.MainActivity;
import com.example.qhs.wallpapershopping.R;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.example.qhs.wallpapershopping.network.NetworkRequest;
import com.example.qhs.wallpapershopping.network.Token;
import com.github.florent37.materialtextfield.MaterialTextField;

import org.json.JSONException;
import org.json.JSONObject;


public class Fragment_login extends Fragment {

    private TextView mTitleAction;
    private TextView mPromptAction;
    private EditText mEditEmail;
    private EditText mEditPassword;
    private EditText mEditUsername;
    private Button mButtonAction;
    private MaterialTextField materialTextField_username;

    private ProgressDialog mProgressDialog;
    private AuthHelper mAuthHelper;

    private Fragment fragment;

    /**
     * Flag to show whether it is sign up field that's showing
     */
    private boolean mIsSignUpShowing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("ورود");

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });


        mAuthHelper = AuthHelper.getInstance(getContext());
        mProgressDialog = new ProgressDialog(getContext());

        mTitleAction = (TextView) view.findViewById(R.id.text_title);
        mPromptAction = (TextView) view.findViewById(R.id.prompt_action);
        mEditEmail = (EditText) view.findViewById(R.id.edit_email);
        mEditPassword = (EditText) view.findViewById(R.id.edit_password);
        mEditUsername = (EditText) view.findViewById(R.id.edit_username);
        mButtonAction = (Button) view.findViewById(R.id.button_action);
        materialTextField_username = (MaterialTextField)view.findViewById(R.id.materialtextfield_username);

        setupView(mIsSignUpShowing);

        return view;
    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        //Toolbar
//        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
//        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
//        title.setText("ورود");
//
//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((AppCompatActivity)getActivity()).onBackPressed();
//            }
//        });
//
//        super.onActivityCreated(savedInstanceState);
//    }

    /**
     * Sets up the view based on whether or not the sign up screen is showing
     *
     * @param isSignUpShowing - flag indicating whether the sign up form is showing
     */
    private void setupView(boolean isSignUpShowing) {
        mIsSignUpShowing = isSignUpShowing;
        mTitleAction.setText(isSignUpShowing ? R.string.text_sign_up : R.string.text_login);
        mButtonAction.setText(isSignUpShowing ? R.string.text_sign_up : R.string.text_login);
        mPromptAction.setText(isSignUpShowing ? R.string.prompt_login: R.string.prompt_signup);
        //mEditUsername.setVisibility(isSignUpShowing ? View.VISIBLE : View.GONE);

        materialTextField_username.setVisibility(isSignUpShowing ? View.VISIBLE : View.GONE);

        mButtonAction.setOnClickListener(isSignUpShowing ? doSignUpClickListener : doLoginClickListener);
        mPromptAction.setOnClickListener(isSignUpShowing ? showLoginFormClickListener : showSignUpFormClickListener);
    }

    /**
     * Log the user in and navigate to profile screen when successful
     */
    private void doLogin() {
        String username = getEmailText();
        String password = getPasswordText();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), R.string.toast_no_empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage(getString(R.string.progress_login));
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        NetworkRequest request = new NetworkRequest();
        request.doLogin(username, password, mLoginCallback);
    }

    /**
     * Sign up the user and navigate to profile screen
     */
    private void doSignUp() {
        String email = getEmailText();
        String password = getPasswordText();
        String username = getUsernameText();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), R.string.toast_no_empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage(getString(R.string.progress_signup));
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        NetRequest request = new NetRequest(getContext());
//        request.JsonObjectNetRequest("POST", "wp/v2/users/register?email="+email+"&username="+username+"&password="+password,mSignUpCallback,"no_need");


        NetworkRequest request = new NetworkRequest();
        request.doSignUp(email, username, password, mSignUpCallback);
    }

    private String getEmailText() {
        return mEditEmail.getText().toString().trim();
    }

    private String getPasswordText() {
        return mEditPassword.getText().toString().trim();
    }

    private String getUsernameText() {
        return mEditUsername.getText().toString().trim();
    }

    /**
     * Save session details and navigates to the quotes activity
     * @param token - {@link Token} received on login or signup
     */
    private void saveSessionDetails(@NonNull Token token) {
        mAuthHelper.setIdToken(token);

        // start profile page
        startActivity(new Intent(getContext(), MainActivity.class) );
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame, new Fragment_home());
//        fragmentTransaction.commit();

    }

    /**
     * Callback for login
     */
    private NetworkRequest.Callback<Token> mLoginCallback = new NetworkRequest.Callback<Token>() {
        @Override
        public void onResponse(@NonNull Token response) {
            dismissDialog();
            // save token and go to profile page
            saveSessionDetails(response);
            Toast.makeText(getContext(), "Hi "+ response.getUser_display_name(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String error) {
            dismissDialog();
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public Class<Token> type() {
            return Token.class;
        }

    };

//    private NetRequest.Callback<JSONObject> mSignUpCallback = new NetRequest.Callback<JSONObject>() {
//        @Override
//        public void onResponse(@NonNull JSONObject response) {
//            dismissDialog();
//            try {
//                Log.d("SIGN_UP ", response.getString("message"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onError(String error) {
//            dismissDialog();
//            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//
//        }
//    };
    /**
     * Callback for sign up
     */
    private NetworkRequest.Callback<Token> mSignUpCallback = new NetworkRequest.Callback<Token>() {
        @Override
        public void onResponse(@NonNull Token response) {
            dismissDialog();
            doLogin();
            // save token and go to profile page
            //saveSessionDetails(response);
            Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String error) {
            dismissDialog();
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public Class<Token> type() {
            return Token.class;
        }
    };

    /**
     * Dismiss the dialog if it's showing
     */
    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Click listener to show sign up form
     */
    private final View.OnClickListener showSignUpFormClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setupView(true);
        }
    };

    /**
     * Click listener to show login form
     */
    private final View.OnClickListener showLoginFormClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setupView(false);
        }
    };

    /**
     * Click listener to invoke login
     */
    private final View.OnClickListener doLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doLogin();
        }
    };

    /**
     * Click listener to invoke sign up
     */
    private final View.OnClickListener doSignUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doSignUp();
        }
    };
}
