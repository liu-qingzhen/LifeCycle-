package com.example.lifecycle.counter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.lifecycle.R;

public class DialogDefault {


    public static AlertDialog createAlertDialog(Context mContext,String title,int layoutId, OnClickListener leftOnClickListener,OnClickListener rightOnClickListener)
    {

        AlertDialog localAlertDialog = new AlertDialog.Builder(mContext).create();
        localAlertDialog.show();
        localAlertDialog.setContentView(layoutId);
        localAlertDialog.setCanceledOnTouchOutside(true);
        Button yes = localAlertDialog.findViewById(R.id.button_yes);
        Button no = localAlertDialog.findViewById(R.id.button_no);
        TextView textview1 =  localAlertDialog.findViewById(R.id.dialog_title);
        textview1.setText("Set the time");
        yes.setOnClickListener(leftOnClickListener);
        no.setOnClickListener(rightOnClickListener);

        return localAlertDialog;
    }
}
