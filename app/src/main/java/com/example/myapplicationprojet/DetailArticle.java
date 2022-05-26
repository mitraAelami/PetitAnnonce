package com.example.myapplicationprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplicationprojet.database.Products;

public class DetailArticle extends AppCompatActivity {

    Toolbar detailtoolbar;
    ImageView detailimage;
    TextView detailprix, detailtitleDescription,detaildescription;
    Button detailbutton;

    Products products = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        final Object object =getIntent().getSerializableExtra("detail");
        if (object instanceof Products){
            products = (Products) object;
        }


        detailtoolbar=findViewById(R.id.detailToolbar);

        detailimage=findViewById(R.id.detailImage);
        detailprix=findViewById(R.id.detailPrix);
        detailtitleDescription=findViewById(R.id.detailTitleDescription);
        detaildescription=findViewById(R.id.detailDescription);

        if(products != null){
            Glide.with(getApplicationContext()).load(products.getImage()).into(detailimage);
            detailprix.setText(products.getPrice());
            detaildescription.setText(products.getDescription());
        }

        detailbutton=findViewById(R.id.detailButton);

        detailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:768585858"));
                startActivity(intent);
            }
        });
    }
}