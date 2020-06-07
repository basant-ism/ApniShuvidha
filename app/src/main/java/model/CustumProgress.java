package model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.apnishuvidha.R;

import java.util.zip.Inflater;

public class CustumProgress
{
    Activity activity;
    Dialog dialog;

    public CustumProgress(Activity activity) {
        this.activity = activity;
    }
    public  void startProgressBar(String massage)
    {
//        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
//        LayoutInflater inflater=activity.getLayoutInflater();
//        View view=inflater.inflate(androidx.appcompat.R.layout.custom_dialog,null);
//        TextView textView=view.findViewById(R.id.tv_loading);
//        //textView.setText(massage);
//        builder.set(view);
//        builder.setCancelable(true);
//        dialog=builder.create();
//        dialog.show();
        dialog=new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custum_dialog);

        TextView textView=dialog.findViewById(R.id.tv_loading);
        textView.setText(massage);
        dialog.show();
    }
    public  void closeProgressBar()
    {
        dialog.dismiss();
    }
}
