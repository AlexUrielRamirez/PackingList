package com.symbol.barcodesample1;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.symbol.barcodesample1.GlobalPreferences.URL;

public class Resultados extends AppCompatActivity {

    private TextView txtNumeroCaja, txtError, btn_imprimir;
    private RecyclerView rvData;
    private ArrayList<ModelList> main_list;
    private ProgressDialog progressDialog;
    private rvAdapter adapter;
    private String Caja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Consultando información, por favor espere...");
        progressDialog.show();

        initViews();

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

    private void initViews() {
        txtNumeroCaja = findViewById(R.id.NumCaja);
        rvData = findViewById(R.id.rv_data);
        txtError = findViewById(R.id.txt_error);
        Caja = getIntent().getStringExtra("caja");
        btn_imprimir = findViewById(R.id.btn_imprimir);
        btn_imprimir.setOnClickListener(v->{
            /*Código de impresión*/
        });
        txtNumeroCaja.setText(Caja);
    }

    public class ModelList{
        private String Id;
        private String RazonSocial;
        private String Marca;
        private String Tienda;

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
