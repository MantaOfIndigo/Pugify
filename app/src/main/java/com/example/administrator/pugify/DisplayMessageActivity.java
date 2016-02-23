package com.example.administrator.pugify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.pugify.model.Owner;
import com.example.administrator.pugify.model.Pug;

import java.util.ArrayList;

public class DisplayMessageActivity extends AppCompatActivity {
    public final static String EXTRA_SCDMESSAGE = "com.example.administrator.pugify.SCDMESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();

        //ArrayList<Pug> message = getIntent().getParcelableArrayListExtra("Pug");
        //Log.i("pug-", message.get(0).toString());
        Bundle b = getIntent().getExtras();

        Pug getData = (Pug) getIntent().getExtras().getSerializable("serialize");

        TextView txtTitle = (TextView) findViewById(R.id.dm_textResult);
        TextView txtDescription = (TextView) findViewById(R.id.profile_city);
        ImageView imgBox = (ImageView) findViewById(R.id.profile_image);
        //if(getData.getProfilePicturePath()  != null) {
           // imgBox.setImageBitmap(getData.getProfilePicture(imgBox.getWidth()));
        //}
        //Log.i("pug-", message.get(0).getDescription());
        if(getData.isValid()) {
            txtTitle.setText(getData.getTitle());
            if(getData.getDescription() == "") {
                txtDescription.setText("no description");
            }else{
                txtDescription.setText(getData.getDescription());
            }
        }
    }

    public void resendMessage(View view){
        Intent intent = new Intent(this, CodeActivity.class);
        TextView txtView = (TextView) findViewById(R.id.dm_textResult);
        String message = txtView.getText().toString();
        intent.putExtra(EXTRA_SCDMESSAGE, message);
        startActivity(intent);
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
}
