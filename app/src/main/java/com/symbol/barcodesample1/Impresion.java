package com.symbol.barcodesample1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static com.symbol.barcodesample1.GlobalPreferences.URL;

public class Impresion {

    private ProgressDialog progressDialog;
     private ArrayList<ModelList> main_list;

    public  void print(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Consultando impresoras, por favor espere...");
        progressDialog.show();

        Volley.newRequestQueue(context).add(new JsonObjectRequest(Request.Method.GET, URL+"getPrints.php", null, response -> {
            JSONArray json= response.optJSONArray("Data");
            main_list = new ArrayList<>();
            try {
                for(int i=0; i<json.length();i++){
                    ModelList entidades = new ModelList();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    entidades.setIdImpresora(jsonObject.optString("IdImpresora"));
                    entidades.setNombre(jsonObject.optString("Nombre"));
                    entidades.setIP(jsonObject.optString("IP"));

                    main_list.add(entidades);

                }


                String[] listNombres = new String[main_list.size()];
                for(int i=0; i<main_list.size(); i++)listNombres[i]=main_list.get(i).getNombre();


                progressDialog.dismiss();


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.print_white);
                builder.setTitle("Selecciona una impresora");
                builder.setItems(listNombres, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendWork(main_list.get(which).getIP(), context); //Enviar la impresion a la dirección IP de impresora seleccionada
                    }
                });
                builder.show();


            } catch (JSONException | NullPointerException e) {
                progressDialog.dismiss();
                Log.e("Datos","tronó->"+e);
            }

        },error -> {
            progressDialog.dismiss();
            Alert.show("Error", "Error de conexión, intente de nuevo", context);
            Log.e("Datos","vacío más->"+error);
        }));


    }

    private void sendWork(String IP, Context context){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket clientSocket=new Socket(IP,6101);  //Especificar dirección IP de destino

                    if(clientSocket.isConnected()){
                        Toast.makeText(context, "Oki", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Alert.show("Error", "Impresora no disponible", context);


                }
            }
        });

    }


    public class ModelList{
        private String IdImpresora;
        private String Nombre;
        private String IP;

        public String getIdImpresora() {
            return IdImpresora;
        }

        public void setIdImpresora(String idImpresora) {
            IdImpresora = idImpresora;
        }

        public String getNombre() {
            return Nombre;
        }

        public void setNombre(String nombre) {
            Nombre = nombre;
        }

        public String getIP() {
            return IP;
        }

        public void setIP(String IP) {
            this.IP = IP;
        }
    }
}
