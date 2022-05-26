package com.example.myapplicationprojet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTab2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText email, password;
    TextView signup;
    Button btnLogin;
    FirebaseAuth auth;


    public FragmentTab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTab2.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTab2 newInstance(String param1, String param2) {
        FragmentTab2 fragment = new FragmentTab2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        email=v.findViewById(R.id.email);
        password=v.findViewById(R.id.password);
        btnLogin =v.findViewById(R.id.login);
        signup = v.findViewById(R.id.signup);

        auth=FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignUp.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private void loginUser() {

        String userEmail= email.getText().toString();
        String userPassword= password.getText().toString();


        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(getActivity(),"Password is Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(getActivity(),"Email is Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if( userPassword.length()< 6){
            Toast.makeText(getActivity(),"Password must be greater than 6 letter!",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"logIn Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),AdInsert.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"Error:"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}