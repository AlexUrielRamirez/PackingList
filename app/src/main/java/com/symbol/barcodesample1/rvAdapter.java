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
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRSocial, txtMarca, txtTienda;
        public ViewHolder(View itemView) {
            super(itemView);
            txtRSocial = (TextView) itemView.findViewById(R.id.txtRazonSocial);
            txtMarca = (TextView) itemView.findViewById(R.id.txt_Marca);
            txtTienda = (TextView) itemView.findViewById(R.id.txt_Tienda);
        }
    }
}
