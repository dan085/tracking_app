package com.transvip.test.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transvip.test.R;
import com.transvip.test.ui.home.model.locationItem;

import java.util.ArrayList;

///Agaptador que muestra pa posicióon
public class locationAdapter extends RecyclerView.Adapter<locationAdapter.ItemViewHolder> {


     ArrayList<locationItem> locationList;
     OnItemClickListenerItemAdapterRecycler mdelegate;

    public locationAdapter(ArrayList<locationItem> locationList,  OnItemClickListenerItemAdapterRecycler delegate) {
        this.locationList = locationList;
        this.mdelegate= delegate;
    }

    ///Agrega de la sección  la ultima posición // para no solititar todas las posiciones despues
    // hay que liminar la cantidad de resultados reemplazando el listener del evento

    public void addLocationTop(ArrayList<locationItem> locationListNewData){
          try {
              locationList.add(0, locationListNewData.get(0));
          }catch(NullPointerException ee){
              ee.printStackTrace();
          }
    }
    public void setLocationData(ArrayList<locationItem> DatalocationList){
        locationList = DatalocationList;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == 1){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item, parent, false);
           return new ItemViewHolder(view);

        }else{//// en caso de asignar una vista especial
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item, parent, false);
           return new ItemViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        locationItem mCurrent = locationList.get(position);
        holder.longitude.setText(String.valueOf(mCurrent.longitude));
        holder.latitude.setText(String.valueOf(mCurrent.longitude));
        holder.fecha.setText(mCurrent.date);
        holder.v.setOnClickListener(v -> mdelegate.OnClick(mCurrent,holder.getAdapterPosition(),v.getContext()));
    }
    @Override
    public int getItemCount() {
        return locationList.size();
    }

     class ItemViewHolder extends RecyclerView.ViewHolder {

         public TextView longitude;
         public TextView fecha;
         public  TextView latitude;
         public  View v;
        public ItemViewHolder(@NonNull View itemView) {
             super(itemView);
             v= itemView;
             fecha  = itemView.findViewById(R.id.fecha);
             longitude = itemView.findViewById(R.id.longitude);
             latitude = itemView.findViewById(R.id.latitude);
         }
    }

}
