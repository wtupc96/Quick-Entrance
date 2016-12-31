package com.ouyang.peter.myapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by wtupc96 on 2016/12/31.
 */
public class myDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle("正在努力加载~")
                .setView(R.layout.layout_dialog)
                .create();
    }
}
