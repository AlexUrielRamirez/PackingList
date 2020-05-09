package com.symbol.barcodesample1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import static com.symbol.barcodesample1.GlobalPreferences.URL;

public class Resultados extends AppCompatActivity {

    public static  TextView txtNumeroCaja, txtError, btn_imprimir,
    OC, TIPOPEDIDO, TIENDA, LUGARENTREGA, MATGROUP, REFERENCIA, NOPROVEEDOR, FENTREGA;
    private RecyclerView rvData;
    public static ArrayList<ModelList> main_list;
    private ProgressDialog progressDialog;
    private rvAdapter adapter;
    public static String Caja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Consultando información, por favor espere...");
        progressDialog.show();

        initViews();

        fillHeader();

        rvData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvData.hasFixedSize();

        Volley.newRequestQueue(this).add(new JsonObjectRequest(Request.Method.GET, URL+"getContent.php?Caja="+Caja, null, response -> {
            Log.e("Datos",URL+"getContent.php?Caja="+Caja);
            JSONArray json= response.optJSONArray("Data");
            main_list = new ArrayList<>();
            try {
                for(int i=0; i<json.length();i++){
                    ModelList entidades = new ModelList();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    entidades.setId(jsonObject.optString("Id"));
                    entidades.setRazonSocial(jsonObject.optString("RazonSocial"));
                    entidades.setMarca(jsonObject.optString("Marca"));
                    entidades.setTienda(jsonObject.optString("Tienda"));

                    entidades.setMPrice(jsonObject.optString("ModPrice"));
                    entidades.setModelo(jsonObject.optString("Material"));
                    entidades.setUPC(jsonObject.optString("UPC"));
                    entidades.setTalla(jsonObject.optString("Talla"));
                    entidades.setPZS(jsonObject.optString("Piezas"));
                    entidades.setUnidades(jsonObject.optString("Unidad"));

                    main_list.add(entidades);

                }

                adapter = new rvAdapter(main_list);
                rvData.setAdapter(adapter);

                btn_imprimir.setVisibility(View.VISIBLE);

                progressDialog.dismiss();

            } catch (JSONException | NullPointerException e) {
                txtError.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                Log.e("Datos","tronó->"+e);
            }

        },error -> {
            txtError.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            Log.e("Datos","vacío más->"+error);
        }));

    }

    private interface AN_fill_header{
        @FormUrlEncoded
        @POST("/FillHeader.php")
        void serData(
                @Field("Caja") String Caja,
                Callback<Response> callback
        );
    }

    private void fillHeader() {
        new RestAdapter.Builder().setEndpoint(URL).build().create(AN_fill_header.class).serData(Caja, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try{
                    JSONObject json = new JSONObject(new BufferedReader(new InputStreamReader(response.getBody().in())).readLine());
                    OC.setText(json.getString("OrdenCompra"));
                    TIENDA.setText(json.getString("Tienda"));
                    MATGROUP.setText(json.getString("MatlGroup"));
                    REFERENCIA.setText(json.getString("Referencia"));
                    FENTREGA.setText(json.getString("FechaEntrega"));
                }catch(JSONException | IOException e){

                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void initViews() {

        OC = findViewById(R.id.OC);
        TIPOPEDIDO = findViewById(R.id.TIPODEPEDIDO);
        TIENDA = findViewById(R.id.TIENDA);
        LUGARENTREGA = findViewById(R.id.LUGARDEENTREGA);
        MATGROUP = findViewById(R.id.MATGROUP);
        REFERENCIA = findViewById(R.id.REFERENCIA);
        NOPROVEEDOR = findViewById(R.id.NOPROVEEDOR);
        FENTREGA = findViewById(R.id.FENTREG);

        txtNumeroCaja = findViewById(R.id.NumCaja);
        rvData = findViewById(R.id.rv_data);
        txtError = findViewById(R.id.txt_error);
        Caja = getIntent().getStringExtra("caja");
        btn_imprimir = findViewById(R.id.btn_imprimir);
        btn_imprimir.setOnClickListener(v->{

            Impresion impresion = new Impresion();
            impresion.print(this);

        });
        txtNumeroCaja.setText(Caja);
    }

    public class ModelList{
        private String Id;
        private String RazonSocial;
        private String Marca;
        private String Tienda;

        private String MPrice;
        private String Modelo;
        private String UPC;
        private String Talla;
        private String PZS;
        private String Unidades;

        public String getMPrice() {
            return MPrice;
        }

        public void setMPrice(String MPrice) {
            this.MPrice = MPrice;
        }

        public String getModelo() {
            return Modelo;
        }

        public void setModelo(String modelo) {
            Modelo = modelo;
        }

        public String getUPC() {
            return UPC;
        }

        public void setUPC(String UPC) {
            this.UPC = UPC;
        }

        public String getTalla() {
            return Talla;
        }

        public void setTalla(String talla) {
            Talla = talla;
        }

        public String getPZS() {
            return PZS;
        }

        public void setPZS(String PZS) {
            this.PZS = PZS;
        }

        public String getUnidades() {
            return Unidades;
        }

        public void setUnidades(String unidades) {
            Unidades = unidades;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getRazonSocial() {
            return RazonSocial;
        }

        public void setRazonSocial(String razonSocial) {
            RazonSocial = razonSocial;
        }

        public String getMarca() {
            return Marca;
        }

        public void setMarca(String marca) {
            Marca = marca;
        }

        public String getTienda() {
            return Tienda;
        }

        public void setTienda(String tienda) {
            Tienda = tienda;
        }
    }

}
