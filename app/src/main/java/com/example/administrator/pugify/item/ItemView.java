package com.example.administrator.pugify.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.administrator.pugify.R;
import com.example.administrator.pugify.model.Feed;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 03/12/2015.
 */
public class ItemView extends LinearLayout {


    Feed currentFeed;
    Context contx;
    public Button btnReport;

    public ItemView(Context context, AttributeSet attrs, Feed feed, String photoURL){
        super(context,attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.hm_item, this);

        currentFeed = feed;
        contx = context;


        ImageView img = (ImageView) findViewById(R.id.itm_img);
        TextView txtTitle = (TextView) findViewById(R.id.itm_title);
        TextView txtDesc = (TextView) findViewById(R.id.itm_desc);
        TextView txtLikes = (TextView) findViewById(R.id.item_likescount);
        final ImageButton btnRate = (ImageButton) findViewById(R.id.item_buttonRate);
        btnReport = (Button) findViewById(R.id.item_report);

        if (feed.getReported()){
            modifyLayout();
            txtDesc.setText("Contenuto in fase di revisione");
            txtDesc.setTextColor(Color.WHITE);
        }else{
            txtDesc.setText(feed.getDescription());
            if(photoURL != "photo"){

                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                Picasso.with(context).load(photoURL).resize(width,width).into(img);
            }
        }

        //img = inserisco il root della foto, più veloce
        txtTitle.setText(feed.getPugName());

        final int[] mess = {feed.getRate()};
        txtLikes.setText(""+mess[0]);
        final String id = feed.getObjectId();

        btnReport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Interactor.sendReport(currentFeed);
                Toast toast = Toast.makeText(contx, "Report inviato!", Toast.LENGTH_SHORT);
                modifyLayout();
                toast.show();
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mess[0] += 1;
                Interactor.liked(id);
                refreshLikes(mess[0]);
                Toast toast = Toast.makeText(btnRate.getContext(), ""+ mess[0], Toast.LENGTH_SHORT);
                toast.show();

            }
        });


    }
    public void modifyLayout(){
        LinearLayout modify = (LinearLayout) findViewById(R.id.hm_item);
        modify.setBackgroundColor(Color.argb(255,255,179,8));
        Button btnReport = (Button) findViewById(R.id.item_report);
        btnReport.setEnabled(false);
    }
    public void refreshLikes(int likes){

        TextView txtLikes = (TextView) findViewById(R.id.item_likescount);
        txtLikes.setText(""+likes);
    }
}
