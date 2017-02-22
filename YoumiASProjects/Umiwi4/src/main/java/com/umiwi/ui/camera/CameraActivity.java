package com.umiwi.ui.camera;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;
import com.umiwi.ui.R;

import java.util.List;

import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.ToastU;

/**
 * @author tangxiyong
 * @category KEY_CUT(是否需要裁剪) 默认不需求=false
 * 如果需要裁剪KEY_CUT_WIDTH,KEY_CUT_HEIGHT不能为0
 * @category KEY_CUT_WIDTH 裁剪的宽比
 * @category KEY_CUT_HEIGHT 裁剪的高比
 */
public class CameraActivity extends AppCompatActivity {
    /**
     * 是否需要裁剪 默认不需求=false
     */
    public static final String KEY_CUT = "key.cut";
    /**
     * 裁剪尺寸宽
     */
    public static final String KEY_CUT_WIDTH = "key.cutwidth";
    /**
     * 裁剪尺寸高
     */
    public static final String KEY_CUT_HEIGHT = "key.cutheight";
    /**
     * 是摄像头  camera
     * 还是本地选择照片  pictures
     */
    public static final String KEY_TYPE = "keyl.type";
    /**
     * 摄像头
     */
    public static final int TYPE_CAMERA = 1;
    /**
     * 本地选择照片
     */
    public static final int TYPE_PICTURE = 2;

    /**
     * 是否需要裁剪
     */
    private boolean isCut;
    /**
     * 裁剪尺寸宽
     */
    private int sizeCutWidth;
    /**
     * 裁剪尺寸高
     */
    private int sizeCutHeight;

    private int typeCameraPicture;

    private static final int REQUEST_CAMERA = 4;
    private static final int REQUEST_PICTURE = 5;
    private static final int REQUEST_CROP_PICTURE_CAMERA = 6;

    private Uri pictureUri;
    private String mPicturePath = Environment.getExternalStorageDirectory().getPath() + "/picture.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.Theme_Umiwi_Translucent);
        super.onCreate(savedInstanceState);

        pictureUri = Uri.parse(mPicturePath);

        Intent i = getIntent();
        isCut = i.getBooleanExtra(KEY_CUT, false);
        sizeCutWidth = i.getIntExtra(KEY_CUT_WIDTH, 180);
        sizeCutHeight = i.getIntExtra(KEY_CUT_HEIGHT, 180);
        typeCameraPicture = i.getIntExtra(KEY_TYPE, TYPE_CAMERA);

        switch (typeCameraPicture) {
            case TYPE_CAMERA:
                Camera();
                break;
            case TYPE_PICTURE:
                Pictures();
                break;
        }
//        setContentView(R.layout.activity_camera);
//
//
//        TextView cameraButton = (TextView) findViewById(R.id.camera_button);
//        TextView picturesButton = (TextView) findViewById(R.id.pictures_button);
//        TextView cancelButton = (TextView) findViewById(R.id.cancel_button);
//
//        cameraButton.setOnClickListener(new CameraClickListener());
//        picturesButton.setOnClickListener(new PicturesClickListener());
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                CameraActivity.this.finish();
//                CameraActivity.this.overridePendingTransition(R.anim.push_up_out, R.anim.push_up_in);
//            }
//        });

        CameraManager.getInstance().registerListener(cameraListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraManager.getInstance().unregisterListener(cameraListener);
    }
    private class CameraClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Camera();
        }

    }

    private class PicturesClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Pictures();
        }

    }

    /**
     * 摄像头
     */
    private void Camera() {
        pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * 相册
     */
//	@TargetApi(Build.VERSION_CODES.KITKAT)
    private void Pictures() {
////		if (AndroidSDK.isKK()) {
////			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////			intent.addCategory(Intent.CATEGORY_OPENABLE);
////			intent.setType("image/*");
////			startActivityForResult(intent, PIC_RESULT_KK);
////		} else {
//			Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//			startActivityForResult(choosePictureIntent, PIC_RESULT);
////		}
        pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        startActivityForResult(getPickImageIntent(CameraActivity.this), REQUEST_PICTURE);
    }

    public Intent getPickImageIntent(final Context context) {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select picture");
    }

    private void createTmpUploadFileFromUri(Uri imageUri) {
        SaveImageUriToCachePathAsyncTaskFragment fragment = SaveImageUriToCachePathAsyncTaskFragment
                .newInstance(imageUri);
        getSupportFragmentManager().beginTransaction().add(fragment, "")
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
                        CameraActivity.this.finish();
                        return;
                    }
                    if (isCut) {
                        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(sizeCutWidth, sizeCutHeight,
                                sizeCutWidth, sizeCutHeight, pictureUri);
                        cropImage.setSourceImage(pictureUri);
                        cropImage.setScale(true);
                        cropImage.setOutputFormat(Bitmap.CompressFormat.PNG.toString());
                        startActivityForResult(cropImage.getIntent(this),
                                REQUEST_CROP_PICTURE_CAMERA);
                    } else {
                        createTmpUploadFileFromUri(pictureUri);
                    }

                } else {
                    Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                    CameraActivity.this.finish();
                }
                break;

            case REQUEST_PICTURE:
                if (data == null) {
                    Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                    CameraActivity.this.finish();
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
                        CameraActivity.this.finish();
                        return;
                    }
                    uri = data.getData();
                    if (uri != null) {
                        if (isCut) {
                            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(sizeCutWidth, sizeCutHeight,
                                    sizeCutWidth, sizeCutHeight, pictureUri);
                            cropImage.setSourceImage(uri);
                            cropImage.setScale(true);
                            cropImage.setOutputFormat(Bitmap.CompressFormat.PNG.toString());
                            startActivityForResult(cropImage.getIntent(this),
                                    REQUEST_CROP_PICTURE_CAMERA);
                        } else {
                            createTmpUploadFileFromUri(pictureUri);
                        }
                    } else {
                        Toast.makeText(this, "图片未找到", Toast.LENGTH_SHORT).show();
                        CameraActivity.this.finish();
                    }
                } else {
                    Toast.makeText(this, "获取错误", Toast.LENGTH_SHORT).show();
                    CameraActivity.this.finish();
                }
                break;
            case REQUEST_CROP_PICTURE_CAMERA:
                if (data == null) {
                    Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                    CameraActivity.this.finish();
                    return;
                }
                createTmpUploadFileFromUri(pictureUri);
                break;
        }

    }

    private ModelStatusListener<CameraResultEvent, String> cameraListener = new ModelStatusListener<CameraResultEvent, String>() {

        @Override
        public void onModelGet(CameraResultEvent key, String models) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onModelUpdate(CameraResultEvent key, String s) {
            switch (key) {
                case SUCC:
                    CameraActivity.this.finish();
                    CameraActivity.this.overridePendingTransition(R.anim.push_up_out, R.anim.push_up_in);
                    break;
                case ERROR:
                    ToastU.showLong(CameraActivity.this, "提取图片失败,请重新选取照片");
                    CameraActivity.this.finish();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onModelsGet(CameraResultEvent key, List<String> models) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CameraActivity.this.finish();
            CameraActivity.this.overridePendingTransition(R.anim.push_up_out, R.anim.push_up_in);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
