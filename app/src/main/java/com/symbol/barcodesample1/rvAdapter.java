package com.symbol.barcodesample1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class rvAdapter extends RecyclerView.Adapter<rvAdapter.ViewHolder> implements View.OnClickListener{

    public rvAdapter(ArrayList<Resultados.ModelList> lista) {
        this.lista = lista;
    }

    ArrayList<Resultados.ModelList> lista;
    private View.OnClickListener listener;


    @Override
    public rvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_rv,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(rvAdapter.ViewHolder holder, int position) {
        holder.txtRSocial.setText(lista.get(position).getRazonSocial());
        holder.txtMarca.setText(lista.get(position).getMarca());
        holder.txtTienda.setText(lista.get(position).getTienda());

        holder.txtMPrice.setText(lista.get(position).getMPrice());

        holder.txtModelo.setText(lista.get(position).getModelo());
        holder.txtUPC.setText(lista.get(position).getUPC());
        holder.txtTalla.setText(lista.get(position).getTalla());
        holder.txtPZS.setText(lista.get(position).getPZS());
        holder.txtUNIDAD.setText(lista.get(position).getUnidades());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRSocial, txtMarca, txtTienda, txtMPrice, txtModelo, txtUPC, txtTalla, txtPZS, txtUNIDAD;
        public ViewHolder(View itemView) {
            super(itemView);
            txtRSocial = (TextView) itemView.findViewById(R.id.txtRazonSocial);
            txtMarca = (TextView) itemView.findViewById(R.id.txt_Marca);
            txtTienda = (TextView) itemView.findViewById(R.id.txt_Tienda);

            txtMPrice = (TextView) itemView.findViewById(R.id.txt_mprice);
            txtModelo = (TextView) itemView.findViewById(R.id.txt_modelo);
            txtUPC = (TextView) itemView.findViewById(R.id.txt_upc);
            txtTalla = (TextView) itemView.findViewById(R.id.txt_talla);
            txtPZS = (TextView) itemView.findViewById(R.id.txt_piezas);
            txtUNIDAD = (TextView) itemView.findViewById(R.id.txt_unidad);
        }
    }
}
