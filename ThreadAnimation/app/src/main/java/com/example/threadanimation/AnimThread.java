package com.example.threadanimation;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import java.util.ArrayList;

public class AnimThread extends Thread {
    private Handler handler;
    private ArrayList<Drawable> drawableList;
    private ImageView imageView;

    public AnimThread(Handler handler, ArrayList<Drawable> drawableList, ImageView imageView) {
        this.handler = handler;
        this.drawableList = drawableList;
        this.imageView = imageView;
    }

    public void run() {
        int index = 0;
        for (int i = 0; i < 100; i++) {
            final Drawable drawable = drawableList.get(index);
            index += 1;
            if (index > 4) {
                index = 0;
            }

            handler.post(new Runnable() {
                public void run() {
                    imageView.setImageDrawable(drawable);
                }
            });

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
