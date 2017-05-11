package com.umiwi.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umiwi.ui.R;

/**
 * 聊天记录图片的大图显示
 */
public class BigPictureActivity extends AppCompatActivity {
    public static final String IMG_URL = "imgurl";
    private String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);
        imgUrl = getIntent().getStringExtra(IMG_URL);
    }
}
