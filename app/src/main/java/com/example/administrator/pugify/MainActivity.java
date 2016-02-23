package com.example.administrator.pugify;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.pugify.model.Owner;
import com.example.administrator.pugify.model.Pug;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.administrator.pugify.MESSAGE";
    private String selectedImagePath = "";
    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;

    String passingPath = "";
    String imgPath;
    ImageView mImageView;
    Button btnCamera;
    Bitmap imageSelected;

    public void launchCamera(View view){
        //dispatchTakePictureIntent();
    }

    public Uri setImageUri(){
        File file = new File(Environment.getExternalStorageDirectory() + "/Pugify/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImgPath(){
        return imgPath;
    }




  /*  private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;

                try{
                photoFile = createImageFile();
            } catch (IOException ex){

            }

            if (photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            galleryAddPic();
            setPic();
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException{
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        Button btnCapture = (Button) findViewById(R.id.btnCapture);
        mImageView = (ImageView) findViewById(R.id.imageView);


    btnCamera.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v){

            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, PICK_IMAGE);
            /*Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
        }

    });

        btnCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(intent, CAPTURE_IMAGE);
            }
        });
/*        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(false);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == PICK_IMAGE){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                imageSelected = BitmapFactory.decodeFile(picturePath);
                passingPath = picturePath;
            /*
                selectedImagePath = getAbsolutePath(data.getData());
                imageSelected = decodeFile(selectedImagePath);
                mImageView.setImageBitmap(imageSelected);*/
            } else if(requestCode == CAPTURE_IMAGE){
                selectedImagePath = getImgPath();
               imageSelected = decodeFile(selectedImagePath);
                mImageView.setImageBitmap(imageSelected);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public Bitmap decodeFile(String path){
        try{
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opt);

            final int REQUIRED_SIZE = (Integer) mImageView.getWidth();

            int scale = 1;
            while (opt.outWidth/ scale / 2 >= REQUIRED_SIZE && opt.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inSampleSize = scale;
            return  BitmapFactory.decodeFile(path, opt2);
        } catch (Throwable e){
            e.printStackTrace();
        }

        return null;
    }

    /*public String getAbsolutePath(Uri uri){
        //Uri selectedImage = data.getData

        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return null;
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void sendMessage(View view){
        Intent intentPassing = new Intent(this, DisplayMessageActivity.class);
        EditText edText = (EditText) findViewById(R.id.edit_message);

        edText = (EditText) findViewById(R.id.edit_message);

       // Pug pug = new Pug(edText.getText().toString(), "ciao", passingPath , new Owner("","",""));
       /* Log.v("stamp", pug.toString());

        if(pug.isValid()) {
            ArrayList<Pug> lst = new ArrayList<Pug>();
            lst.add(pug);

            //pug.setProfilePicture(imageSelected);

            intentPassing.putExtra("serialize", (Serializable) pug);
            startActivity(intentPassing);

        }else{
            //alert
            Log.v("alert", pug.toString());

        }
*/

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // scala immagine per risparmiare su dimensioni
   /* private void setPic(){
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageBitmap(bitmap);
    }

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }*/
    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }
}
