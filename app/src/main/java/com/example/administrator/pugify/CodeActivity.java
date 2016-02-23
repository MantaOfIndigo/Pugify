package com.example.administrator.pugify;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 24/11/2015.
 */
public class CodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        Intent intent = getIntent();
        TextView txtView = (TextView) findViewById(R.id.cd_textresult);
        txtView.setText(intent.getStringExtra(DisplayMessageActivity.EXTRA_SCDMESSAGE));
       /* if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if( id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    public static class PlaceholderFragment extends Fragment{
        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
            View rootView = inflater.inflate(R.layout.frgment_display_message, container, false);
            return rootView;
        }
    }*/
}
