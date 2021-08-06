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
import com.example.mediccompanion.activities.MedicationTake;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.myViewHolder> {
    private List<Medications> medications;
    public HomeRecyclerAdapter(List<Medications> medsToTake) {
        this.medications = medsToTake;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_home, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.drugName.setText(medications.get(position).getDrugName());
        String amountNow = medications.get(position).getAmountPerDose() + " " +
                medications.get(position).getDrugType();
        holder.amount_to_take_view.setText(amountNow);
        String amountLeft = medications.get(position).getDrugQuantity() + " " + medications.get(position).getDrugType() + " " + "left";
        holder.amount_left_view.setText(amountLeft);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), MedicationTake.class);
                intent.putExtra("drugId", position);
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
        TextView drugName, amount_to_take_view, amount_left_view;
        public myViewHolder(@NonNull final View itemView) {
            super(itemView);
            drugName = itemView.findViewById(R.id.medication_name_view);
            amount_left_view = itemView.findViewById(R.id.meds_left_view);
            amount_to_take_view = itemView.findViewById(R.id.number_of_meds_to_take_view);
        }
    }
}
