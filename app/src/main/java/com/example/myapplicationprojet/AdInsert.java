package com.example.myapplicationprojet;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.load.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AdInsert extends AppCompatActivity {

    private Context context;
    private EditText title, description, prix;
    Button button,changeImg;
    Toolbar toolbar;
    RadioGroup radioGroup;
    ImageView imageView;
    FirebaseStorage storage;
    Uri imageUri;

    ActivityResultLauncher<String> mtakephoto;



    private static final String TAG = "";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_insert);

        context = getApplicationContext();
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        prix=findViewById(R.id.price);
        button=findViewById(R.id.add_ad);
        toolbar=findViewById(R.id.toolbar);
        radioGroup=findViewById(R.id.radioGroup);
        changeImg=findViewById(R.id.buttonimage);
        imageView=findViewById(R.id.imageArticle);
        storage=FirebaseStorage.getInstance();



        mtakephoto=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageView.setImageURI(result);
                imageUri=result;
                
            }
        });

        changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mtakephoto.launch("image/*");

            }


        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();

            }

            private void uploadImg() {
                if(imageUri != null){
                    StorageReference reference = storage.getReference().child("images/" + UUID.randomUUID().toString());
                    reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String articleTitle = title.getText().toString().trim();
                                    String articleDescription = description.getText().toString().trim();
                                    String articlePrix= prix.getText().toString();
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("name",articleTitle);
                                    map.put("description",articleDescription);
                                    map.put("price",articlePrix);
                                    map.put("image",uri);





                                    firestore.collection("ProductItems").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG,"onSuccess: Successful");
                                            //Intent intent = new Intent(context, AdPhoto.class);
                                            //startActivity(intent);
                                            Toast.makeText(AdInsert.this,"successfuly added your annonce!",Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG,"onFailure: Not Successful");
                                        }
                                    });

                                }
                            });
                        }
                    });
                }
            }

        });

    }

}