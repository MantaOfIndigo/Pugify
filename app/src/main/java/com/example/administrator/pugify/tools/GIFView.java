package com.example.administrator.pugify.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.pugify.R;

import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.util.InputMismatchException;

/**
 * Created by Administrator on 01/12/2015.
 */
public class GIFView extends View {
    public Movie mMovie;
    public long movieStart;

    public GIFView(Context context){
        super(context);
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs){
        super(context, attrs);
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initializeView();
    }

    private void initializeView(){
        InputStream is = getContext().getResources().openRawResource(+ R.drawable.pug);
        mMovie = Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT);

        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if(movieStart == 0){
            movieStart = now;
        }
        if (mMovie != null){
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            Float scaleMultiplier = (float) this.getWidth() / (float) mMovie.width();
            canvas.scale(scaleMultiplier , scaleMultiplier);
            mMovie.draw(canvas, 0, 0);
            this.invalidate();
        }
    }

    private int gifId;

    public  void setGIFResource(int resId){
        this.gifId = resId;
        initializeView();
    }

    public int getGIFResource(){
        return this.gifId;
    }
}
