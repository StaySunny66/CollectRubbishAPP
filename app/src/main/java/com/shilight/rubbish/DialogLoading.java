package com.shilight.rubbish;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class DialogLoading extends Dialog {

    private TextView loadingLabel;

    public DialogLoading(Context context) {
        super(context,R.style.Dialog);
        setContentView(R.layout.dialog_loading_layout);
        setCanceledOnTouchOutside(false);    //dialog弹出后点击屏幕，dialog不消失；点击物理返回键dialog消失
        loadingLabel=(TextView)this.findViewById(R.id.loading_text);
    }

    public void setDialogLabel(String lable){
        loadingLabel.setText(lable);
    }
}