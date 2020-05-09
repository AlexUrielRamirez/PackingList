package com.symbol.barcodesample1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import static com.symbol.barcodesample1.GlobalPreferences.URL;

public class Impresion {

    private ProgressDialog progressDialog;
     private ArrayList<ModelList> main_list;
     String Caja = Resultados.Caja;

    private interface api_network_enviar_params{
        @FormUrlEncoded
        @POST("/printDiseño.php")
        void setData(
                @Field("Caja") String Caja,
                @Field("RazonSocial") String RazonSocial,
                @Field("OC") String OC,
                @Field("MatGroup") String MatGroup,
                @Field("Referencia") String Referencia,
                @Field("Tienda") String Tienda,
                @Field("Proveedor") String Proveedor,
                @Field("Lugar") String Lugar,
                @Field("Fecha") String Fecha,

                Callback<Response> callback
        );
    }

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
                        try {
                            sendWork(main_list.get(which).getIP(), context); //Enviar la impresion a la dirección IP de impresora seleccionada
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void sendWork(String IP, Context context) throws JSONException {

        new RestAdapter.Builder().setEndpoint(URL).build().create(api_network_enviar_params.class).setData(
                Caja, Resultados.main_list.get(0).getRazonSocial(), Resultados.OC.getText().toString(),
                Resultados.MATGROUP.getText().toString(), Resultados.REFERENCIA.getText().toString(),
                Resultados.TIENDA.getText().toString(), Resultados.NOPROVEEDOR.getText().toString(),
                Resultados.LUGARENTREGA.getText().toString(), Resultados.FENTREGA.getText().toString(),
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            String Res = br.readLine();

                            StringBuffer zplres = new StringBuffer();

                            while (Res!=null){
                                zplres.append(Res);
                                Res = br.readLine();

                            }
                            Log.e("zpl", zplres.toString());
                            printWork(IP, zplres.toString(), context);
                        } catch (NullPointerException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(context, "Error, revise su conexión", Toast.LENGTH_SHORT).show();
                    }
                });


                        /*if (clientSocket.isConnected()) {

                                if (Resultados.main_list.size() <= 4)
                                    Log.e("impresión", "de 1 a 4");
                                else if (Resultados.main_list.size() >= 5 && Resultados.main_list.size() <= 8)
                                    Log.e("impresión", "de 5 a 8");
                                else if (Resultados.main_list.size() >= 9 && Resultados.main_list.size() <= 12)
                                    Log.e("impresión", "de 9 a 12");
                                else if (Resultados.main_list.size() >= 13 && Resultados.main_list.size() <= 16)
                                    Log.e("impresión", "de 13 a 16");
                                else if (Resultados.main_list.size() >= 17 && Resultados.main_list.size() <= 20)
                                    Log.e("impresión", "de 17 a 20");
                            }*/






    }


    public void printWork(String IP, String Respuesta, Context context){
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    Socket clientSocket = new Socket(IP, 6101);  //Especificar dirección IP de destino
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                    outToServer.writeBytes(Respuesta);

                    clientSocket.close();

                } catch (UnknownHostException ex) {
                    ex.printStackTrace();

                } catch (IOException ex) {
                    ex.printStackTrace();
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
