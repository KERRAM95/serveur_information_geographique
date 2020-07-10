package view2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nearestpoints.R;

import java.util.ArrayList;

import beans.Fields;


/**
 * Created by booth09-mgr2 on 21/03/2017.
 */

public class FieldAdapterPop extends RecyclerView.Adapter<FieldAdapterPop.ViewHolder> {



    //Pointeur vers une classe qui implémente OnFieldsListener
    private OnFieldsListener onFieldsListener;
    private ArrayList<Fields> fields;

    public FieldAdapterPop(ArrayList<FieldAdapterPop> fields, OnFieldsListener onFieldsListener) {

        this.onFieldsListener = onFieldsListener;
    }


    /**
     * Méthode qui permet de créer une ligne mais que nous n'appellerons jamais nous-mêmes
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_evenement, parent, false);

        return new FieldAdapterPop.ViewHolder(view);
    }

    /**
     * Méthode qui remplit une ligne créée mais que nous n'appellerons jamais nous-mêmes
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Fields  field= fields.get(position);

        holder.tv_commune.setText(field.getName());
        holder.tv_lat.setText(Float.toString(field.getLatitude()));
        holder.tv_lon.setText(Float.toString(field.getLongitude()));


        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFieldsListener != null) {
                    onFieldsListener.onClick(field);
                }
            }
        });
    }

    /**
     * Méthode qui indique le nombre de lignes à créer mais que nous n'appellerons jamais nous-mêmes
     */
    @Override
    public int getItemCount() {
        return fields.size();
    }

    /**
     * Classe interne représentant les pointeurs vers les composants graphiques d'une ligne de la liste
     * Il y aura une instance de cette classe par ligne
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_commune, tv_lat, tv_lon;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_commune = (TextView) itemView.findViewById(R.id.tv_Commune);
            tv_lat = (TextView) itemView.findViewById(R.id.tv_Lat);
            tv_lon = (TextView) itemView.findViewById(R.id.tv_lon);
            root = itemView.findViewById(R.id.root);
        }
    }

    //Notre moyen de communication
    public interface OnFieldsListener {

        void onClick(Fields fields);

    }


}