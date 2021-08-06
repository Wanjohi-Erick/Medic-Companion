package com.example.mediccompanion.adapters;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediccompanion.Medications;
import com.example.mediccompanion.R;
import com.example.mediccompanion.activities.MedicationView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.myViewHolder> {
    private List<Medications> medications;
    public RecyclerViewAdapter(List<Medications> medications) {
        this.medications = medications;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.bagType.setText(medications.get(position).getDrugName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MedicationView.class);
                intent.putExtra("rowId", position);
                intent.putExtra("drugName", String.valueOf(medications.get(position).getDrugName()));
                intent.putExtra("type", String.valueOf(medications.get(position).getDrugType()));
                intent.putExtra("quantity", String.valueOf(medications.get(position).getDrugQuantity()));
                intent.putExtra("amount", String.valueOf(medications.get(position).getAmountPerDose()));
                intent.putExtra("dosages", String.valueOf(medications.get(position).getDrugDosages()));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder {
        private TextView bagType;
        public myViewHolder(@NonNull final View itemView) {
            super(itemView);
            bagType = itemView.findViewById(R.id.medication_name_view);
            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), MedicationView.class);
                    itemView.getContext().startActivity(intent);
                }
            });

             */
        }
    }
}
