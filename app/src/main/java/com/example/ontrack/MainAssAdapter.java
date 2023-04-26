package com.example.ontrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAssAdapter extends RecyclerView.Adapter<AssignmentViewHolder> {

    private static List<Assignment> assList;
    private MainActivity mainAct;
    private AssignmentActivity assAct;
    private boolean home;

    public MainAssAdapter(MainActivity ma, List<Assignment> assList) {
        this.assList = assList;
        mainAct = ma;
        this.home = true;
    }

    public MainAssAdapter(AssignmentActivity aa, List<Assignment> assList) {
        this.assList = assList;
        assAct = aa;
        this.home = false;
    }


    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (home) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_assignment, parent, false);


            itemView.setOnClickListener(mainAct);
            itemView.setOnLongClickListener(mainAct);

            return new AssignmentViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_assignment, parent, false);


            itemView.setOnClickListener(assAct);
            itemView.setOnLongClickListener(assAct);

            return new AssignmentViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {

        Assignment assignment = assList.get(position);
        holder.aName.setText(assignment.getName());
        holder.aDate.setText(assignment.getDueDate());
        holder.aDescription.setText(assignment.getNotes());
        holder.aPriority.setText(assignment.getPriority());

    }

    @Override
    public int getItemCount() {
        return assList.size();
    }
}
