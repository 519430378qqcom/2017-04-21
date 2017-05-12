package com.umiwi.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;

/**
 * 聊天记录图片的大图显示
 */
public class BigPictureActivity extends AppCompatActivity {
    public static final String IMG_URL = "imgurl";
    private String imgUrl;
    private ImageView iv_big_picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);
        imgUrl = getIntent().getStringExtra(IMG_URL);
        iv_big_picture = (ImageView) findViewById(R.id.iv_big_picture);
        Glide.with(this).load(imgUrl).into(iv_big_picture);
        iv_big_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
