package com.symbol.barcodesample1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Alert {

    public static void show(String Titulo, String Mensaje, Context context){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
        dialogo1.setTitle(Titulo);
        dialogo1.setMessage(Mensaje);
        dialogo1.setCancelable(true);
        dialogo1.show();
    }
}
