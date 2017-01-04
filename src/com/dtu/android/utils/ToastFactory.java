package com.dtu.android.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class ToastFactory {
    private static ToastFactory instance;
    private TextView textView;
    private Toast toast;

    private ToastFactory() {
        this.toast = null;
    }

    public static ToastFactory getInstance() {
        if (instance == null) {
            instance = new ToastFactory();
        }
        return instance;
    }

    public void showToast(Context context, String txt) {
        if (this.toast == null) {
            this.toast = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
        } else {
            this.toast.setText(txt);
            this.toast.setDuration(Toast.LENGTH_SHORT);
        }
        this.toast.show();
    }

    public void cancelToast() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }
}
