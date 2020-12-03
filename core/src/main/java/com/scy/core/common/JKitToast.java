package com.scy.core.common;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.scy.core.R;

/**
 * @author: SCY
 * @date: 2020/11/19   10:31
 * @version: 1.0
 * @desc: toast
 */
public class JKitToast {

    private static Toast toast;

    public static synchronized void success(String success) {
        try {
            if (toast!=null){
                toast.cancel();
            }
            toast =new Toast(JKit.getmApplication());
            View view = LayoutInflater.from(JKit.getmApplication()).inflate(R.layout.toast_layout, null);
            ((ImageView)view.findViewById(R.id.image)).setImageResource(R.drawable.sure_white);
            ((TextView)view.findViewById(R.id.toast_tv)).setText(success);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * toast 短显示
     * @param err       错误提示
     */
    public static synchronized void error(String err) {
        try {
            if (toast!=null){
                toast.cancel();
            }
            toast =new Toast(JKit.getmApplication());
            View view = LayoutInflater.from(JKit.getmApplication()).inflate(R.layout.toast_layout, null);
            ((ImageView)view.findViewById(R.id.image)).setImageResource(R.drawable.waring_white);
            ((TextView)view.findViewById(R.id.toast_tv)).setText(err);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
