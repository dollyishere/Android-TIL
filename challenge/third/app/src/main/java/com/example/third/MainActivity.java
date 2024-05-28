package com.example.third;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView imageView1;
    ImageView imageView2;

    int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
    }

    public void onButtonClicked(View view) {
        changeImage();
    }

    // if you want to show only one image, you should change visability of imageViews, not setImageResource
    private void changeImage() {
        if (imageIndex == 0) {
            imageView1.setImageResource(R.drawable.dream02);
            imageView2.setImageResource(R.drawable.dream01);
            imageIndex = 1;
        } else {
            imageView1.setImageResource(R.drawable.dream01);
            imageView2.setImageResource(R.drawable.dream02);
            imageIndex = 0;
        }
    }
}