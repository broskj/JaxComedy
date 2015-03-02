package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Kyle on 2/23/2015.
 */
public class ActivityManager extends Activity {

    private int screenWidth, screenHeight;
    private Context context;

    ActivityManager(Context context) {
        this.context = context;
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public void manageActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void scaleBackground(RelativeLayout myLayout, int backgroundID) {

        Bitmap bitmapBackground = BitmapFactory.decodeResource(context.getResources(), backgroundID);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(context.getResources(), resizedBitmapBackground));

    }

    public void scaleBackground(LinearLayout myLayout, int backgroundID) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmapBackground = BitmapFactory.decodeResource(context.getResources(), backgroundID, options);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(context.getResources(), resizedBitmapBackground));

    }

    public void scaleImage(ImageView myImage, int imageID, double widthScale, double heightScale) {

        Bitmap bitmapIVLogo = BitmapFactory.decodeResource(context.getResources(), imageID);
        Bitmap resizedBitmapIVLogo = Bitmap.createScaledBitmap(bitmapIVLogo, (int) (screenWidth * widthScale), (int) (screenHeight * heightScale), true);
        myImage.setImageBitmap(resizedBitmapIVLogo);
    }
}
