package com.example.myapplicationprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationprojet.database.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Context context;
    private EditText name, email, phoneNmbr, password;
    private RadioButton pro, part;
    private RadioGroup acc_type;
    private Button signUpBtn;
    private Spinner spinner;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        context = getApplicationContext();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNmbr = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        pro = findViewById(R.id.pro);
        part = findViewById(R.id.particular);
        signUpBtn = findViewById(R.id.signup);
        spinner = findViewById(R.id.cities);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.locations,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUser();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text= adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void createUser() {
        String userName = name.getText().toString();
        String userEmail= email.getText().toString();
        String userPassword= password.getText().toString();
        String userPhone= phoneNmbr.getText().toString();


        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Name is Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Password is Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this,"Email is Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if( userPassword.length()< 6){
            Toast.makeText(this,"Password must be greater than 6 letter!",Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Users users= new Users(userName,userEmail,userPassword);
                    String id= task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(id).setValue(users);

                    Toast.makeText(SignUp.this,"Registration successful!",Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(SignUp.this,"Error:"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}