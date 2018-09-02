package com.publicbenifitsharing.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.publicbenifitsharing.android.entityclass.Dynamic;
import com.publicbenifitsharing.android.entityclass.TencentUserInfo;
import com.publicbenifitsharing.android.util.DBService;
import com.publicbenifitsharing.android.util.MySQLDBOpenHelper;
import com.publicbenifitsharing.android.viewpager.DynamicPage;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadDynamicActivity extends AppCompatActivity implements View.OnClickListener {

    private Button back;
    private TextView upload;
    private EditText contentEdit;
    private ImageButton imageView;

    private Uri imageUri;
    private File uploadImageFile;

    private static final int TAKE_PHOTO=1;
    private static final int CHOOSE_PHOTO=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dynamic);
        TextView titleText=(TextView) findViewById(R.id.title_text);
        back=(Button) findViewById(R.id.back);
        upload=(TextView) findViewById(R.id.upload);
        contentEdit=(EditText) findViewById(R.id.content_edit);
        imageView=(ImageButton) findViewById(R.id.image_view);
        titleText.setText("发布动态");
        back.setOnClickListener(this);
        upload.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    private static final String TAG = "UploadDynamicActivity";

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.upload:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadDynamicData();
                    }
                }).start();
                break;
            case R.id.image_view:
                showChooseMenu();
                break;
            default:
                break;
        }
    }

    private void uploadDynamicData(){
        String content=contentEdit.getText().toString();
        String imageUrl=null;
        if (uploadImageFile!=null){
            imageUrl="http://172.27.35.1:8080/PBS/dynamics/"+nameDynamicImage();
            uploadImage(uploadImageFile);
        }
        if (!TextUtils.isEmpty(content)||imageUrl!=null){
            Dynamic dynamic=new Dynamic();
            List<TencentUserInfo> tencentUserInfoList= LitePal.findAll(TencentUserInfo.class);
            dynamic.setUserName(tencentUserInfoList.get(0).getUserName());
            dynamic.setHeadIconUrl(tencentUserInfoList.get(0).getHeadIconUrl());
            dynamic.setImageUrl(imageUrl);
            dynamic.setContentTitle(content);
            dynamic.setReleaseDate(getSystemDate());
            dynamic.setReleaseTime(String.valueOf(System.currentTimeMillis()));
            DBService dbService=DBService.getDbService();
            int result=dbService.uploadDynamicData(dynamic);
            if (result==1){
                Intent intent=new Intent();
                intent.putExtra("data_return",true);
                setResult(RESULT_OK,intent);
                finish();
            }else {
                Toast.makeText(this,"发布失败,请检查系统设置！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(File uploadFile){
        Log.d(TAG, "uploadImage: "+uploadFile.getPath());
        MultipartBody.Builder builder=new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/jpg"),uploadFile);

        builder.addFormDataPart("file",nameDynamicImage(),requestBody);
        MultipartBody multipartBody=builder.build();

        Request request=new Request.Builder()
                .url("http://"+MainActivity.serverId+":8080/PublicBenfitSharingServer/UploadDynamicImageServlet")
                .post(multipartBody)
                .build();
        try{
            Response response=client.newCall(request).execute();
            Log.d(TAG, "uploadImage: "+response.body().string());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String nameDynamicImage(){
        DBService dbService=DBService.getDbService();
        int number=dbService.getDynamicNumber()+1;
        String name="dynamic"+number+".jpg";
        return name;
    }

    //获得系统时间
    private String getSystemDate(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date curDate =  new Date(System.currentTimeMillis());
        return dateFormat.format(curDate);
    }

    private void showChooseMenu(){
        final PopupMenu menu=new PopupMenu(this,imageView);
        menu.getMenuInflater().inflate(R.menu.choose_image_menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.album:
                        if (ContextCompat.checkSelfPermission(UploadDynamicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(UploadDynamicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }else {
                            openAlbum();
                        }
                        break;
                    case R.id.camera:
                        takePhoto();
                        break;
                    case R.id.cancel:
                        menu.dismiss();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    private void takePhoto(){
        File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
        try{
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(UploadDynamicActivity.this,"com.publicbenifitsharing.android.fileprovider",outputImage);
        }else{
            imageUri=Uri.fromFile(outputImage);
        }

        Intent intent =new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                        uploadImageFile=new File(getExternalCacheDir(),"output_image.jpg");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    String imagePath=null;
                    Uri uri=data.getData();
                    Cursor cursor=getContentResolver().query(uri,null,null,null,null);
                    if (cursor!=null){
                        if (cursor.moveToFirst()){
                            imagePath=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        cursor.close();
                    }
                    displayImage(imagePath);
                    if (imagePath!=null){
                        uploadImageFile=new File(imagePath);
                    }
                }
            default:
                break;
        }
    }

    private void displayImage(String imagePath){
        if (imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);

            //读取图片方向值
            int tag=0;
            int degree=0;
            try{
                ExifInterface exifInterface=new ExifInterface(imagePath);
                tag=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,-1);
            }catch(IOException e){
                e.printStackTrace();
            }
            if (tag==ExifInterface.ORIENTATION_ROTATE_90){
                degree=90;
            }

            if (degree!=0&&bitmap!=null){
                imageView.setImageBitmap(rotateImage(degree,bitmap));
            }else{
                imageView.setImageBitmap(bitmap);
            }
        }else{
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    //旋转图片
    private Bitmap rotateImage(int degree,Bitmap bitmap){
        Matrix matrix=new Matrix();
        matrix.setRotate(degree);
        Bitmap createBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return createBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(UploadDynamicActivity.this,"拒绝权限将无法使用！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
