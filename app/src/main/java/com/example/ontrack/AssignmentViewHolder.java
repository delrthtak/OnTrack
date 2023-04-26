package com.example.ontrack;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AssignmentViewHolder extends RecyclerView.ViewHolder {
    public TextView aName;
    public TextView aDate;
    public TextView aDescription;
    public TextView aPriority;


    public AssignmentViewHolder(View view) {
        super(view);
        aName = view.findViewById(R.id.ra_Date);
        aDate = view.findViewById(R.id.ra_Minutes);
        aDescription = view.findViewById(R.id.ra_Description);
        aPriority = view.findViewById(R.id.rw_Value);

    }


}
