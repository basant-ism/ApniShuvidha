package model;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apnishuvidha.R;

public class CustumProgress2
{
    Activity activity;
    Dialog dialog;
    public ImageView imgOpenShop,imgCloseShop;
    public TextView tvSelectOption;

    public CustumProgress2(Activity activity) {
        this.activity = activity;
        dialog=new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custum_dialog2);
        imgCloseShop=dialog.findViewById(R.id.img_close_shop);
        imgOpenShop=dialog.findViewById(R.id.img_open_shop);
        tvSelectOption=dialog.findViewById(R.id.tv_select_option);
    }
    public  void startProgressBar()
    {
        dialog.show();
    }
    public  void closeProgressBar()
    {
        dialog.dismiss();
    }
}
