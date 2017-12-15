package com.example.sylvain.albumz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

/**
 * Created by Sylvain on 07/11/2017.
 */


public class AccountFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    //User Auth via FireBase
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser currentUser;


    ProgressBar progressBarAuth;
    ScrollView scrollView;
    //UI register
    EditText register_email;
    EditText register_name;
    EditText register_password;
    EditText register_password_confirmed;
    Button register_button;

    //UI login
    EditText login_email;
    EditText login_password;
    Button login_button;


    //User.Class
    FirebaseUtils User_utils = new FirebaseUtils(); //constructor only to get Utils methods (clear, isExist etc )

    //user try to auth
    User userToAuthRegister;
    User userToAuthLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_view, container, false); //get current layout to get UI element

        //Register
        register_email = view.findViewById(R.id.form_email_register);
        register_name = view.findViewById(R.id.form_name_register);
        register_password = view.findViewById(R.id.form_password_register);
        register_password_confirmed = view.findViewById(R.id.form_password_confirmed_register);
        //Submit register
        register_button = view.findViewById(R.id.registerButton);
        register_button.setOnClickListener(this);




        //Login
        login_email = view.findViewById(R.id.form_email_login);
        login_password = view.findViewById(R.id.form_password_login);
        //Submit login
        login_button = view.findViewById(R.id.loginButton);
        login_button.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();


        // Inflate the layout for this fragment

        progressBarAuth = view.findViewById(R.id.progressBarAuth);
        scrollView = view.findViewById(R.id.scrollView);
        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:

                Map<String, String> validation_response_register = User_utils.isValideRegister(
                        register_email.getText().toString(),
                        register_name.getText().toString(),
                        register_password.getText().toString(),
                        register_password_confirmed.getText().toString());


                if (validation_response_register.get("error") == "yes") {
                    String error_str = "";
                    //we hit error display all error
                    for (String item : validation_response_register.keySet()) {
                        String key = item.toString();
                        String value = validation_response_register.get(item).toString();
                        if (validation_response_register.get(item).toString() == "yes") {
                            //remove yes from error message;
                        } else {
                            error_str += value + "\n";
                        }
                    }
                    //make toast with all error _ ERROR MESSAGE
                    Toast.makeText(getActivity(), error_str, Toast.LENGTH_SHORT).show();
                } else {
                    userToAuthRegister = new User(register_email.getText().toString(), register_name.getText().toString(), register_password.getText().toString(), register_password_confirmed.getText().toString());
                    mAuth.createUserWithEmailAndPassword(userToAuthRegister.getEmail(), userToAuthRegister.getPassword())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        currentUser = mAuth.getCurrentUser();
                                        mDatabase.child("users").child(currentUser.getUid()).child("name").setValue(userToAuthRegister.getName());
                                        mDatabase.child("users").child(currentUser.getUid()).child("email").setValue(userToAuthRegister.getEmail());

                                        //TODO: disable les inputs + redirect after 3 sc

                                        progressBarAuth.setVisibility(View.VISIBLE);
                                        login_email.setFocusable(false);
                                        login_password.setFocusable(false);

                                        login_button.setEnabled(false);


                                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                login_email.setFocusable(true);
                                                login_password.setFocusable(true);
                                                progressBarAuth.setVisibility(View.GONE);
                                                login_button.setEnabled(true);
                                                goBackToHome();
                                            }
                                        }, 3000);

                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;

            //TODO: pareil que register avc regidrect + affichage des extra dans mainactivity
            case R.id.loginButton:
                Map<String, String> validation_response_login = User_utils.isValideLogin(
                        login_email.getText().toString(),
                        login_password.getText().toString());


                if (validation_response_login.get("error") == "yes") {
                    String error_str = "";
                    //we hit error display all error
                    for (String item : validation_response_login.keySet()) {
                        String key = item.toString();
                        String value = validation_response_login.get(item).toString();
                        if (validation_response_login.get(item).toString() == "yes") {
                            //remove yes from error message;
                        } else {
                            error_str += value + "\n";
                        }
                    }
                    //make toast with all error _ ERROR MESSAGE
                    Toast.makeText(getActivity(), error_str, Toast.LENGTH_SHORT).show();
                } else {
                    userToAuthLogin = new User(login_email.getText().toString(), login_password.getText().toString());
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(userToAuthLogin.getEmail(), userToAuthLogin.getPassword())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        currentUser = mAuth.getCurrentUser();
                                        progressBarAuth.setVisibility(View.VISIBLE);
                                        register_name.setFocusable(false);
                                        register_email.setFocusable(false);
                                        register_password.setFocusable(false);
                                        register_password_confirmed.setFocusable(false);
                                        register_button.setEnabled(false);

                                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBarAuth.setVisibility(View.GONE);
                                                register_name.setFocusable(true);
                                                register_email.setFocusable(true);
                                                register_password.setFocusable(true);
                                                register_password_confirmed.setFocusable(true);

                                                register_button.setEnabled(true);

                                                goBackToHome();
                                            }
                                        }, 3000);
                                    }  else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
        }
    }

    public void goBackToHome() {
        Intent home = new Intent(getContext(), MainActivity.class);
        /*
        home.putExtra("name", userToAuth.getName());
        home.putExtra("email", userToAuth.getEmail());
        */
        startActivity(home);
    }


}
