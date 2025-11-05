package com.example.repairapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private DatabaseHelper dbHelper;
    private String type;
    private List<Request> requests;
    private boolean isStatisticsMode = false;

    public RequestAdapter(DatabaseHelper dbHelper, String type) {
        this.dbHelper = dbHelper;
        this.type = type;
        this.requests = new ArrayList<>();
        refresh();
    }

    public RequestAdapter(DatabaseHelper dbHelper, List<Request> list, boolean isStats) {
        this.dbHelper = dbHelper;
        this.requests = list != null ? list : new ArrayList<>();
        this.isStatisticsMode = isStats;
    }

    public void refresh() {
        if (type != null && dbHelper != null) {
            requests = dbHelper.getRequestsByType(type);
        }
        if (requests == null) requests = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request r = requests.get(position);
        holder.textTitle.setText(r.getOwnerName() + " — " + r.getModel());
        holder.textInfo.setText("Дата: " + r.getDateCreated() + " | Статус: " + r.getStatus());

        int statusColor = "выполнено".equals(r.getStatus()) ?
                holder.itemView.getContext().getResources().getColor(R.color.green) :
                holder.itemView.getContext().getResources().getColor(R.color.orange);
        holder.textInfo.setTextColor(statusColor);

        if (isStatisticsMode) {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnComplete.setVisibility("выполнено".equals(r.getStatus()) ? View.GONE : View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), EditRequestActivity.class);
                intent.putExtra("request_id", r.getId());
                v.getContext().startActivity(intent);
            });

            holder.btnComplete.setOnClickListener(v -> {
                dbHelper.updateStatus(r.getId(), "выполнено");
                Toast.makeText(v.getContext(), "Заявка завершена", Toast.LENGTH_SHORT).show();
                refresh();
            });

            holder.btnDelete.setOnClickListener(v -> {
                dbHelper.deleteRequest(r.getId());
                Toast.makeText(v.getContext(), "Заявка удалена", Toast.LENGTH_SHORT).show();
                refresh();
            });
        }
    }

    @Override
    public int getItemCount() {
        return requests != null ? requests.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textInfo;
        Button btnEdit, btnComplete, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textInfo = itemView.findViewById(R.id.textInfo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}