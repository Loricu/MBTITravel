package com.example.mbtitravel.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mbtitravel.R;
import com.example.mbtitravel.model.Attraction;

import java.util.ArrayList;
import java.util.List;

/** Adaptor RecyclerView care afișează lista de atracții. */
public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    public interface Listener {
        void onEdit(Attraction attraction);
        void onDelete(Attraction attraction);
    }

    private final List<Attraction> items = new ArrayList<>();
    private final Listener listener;
    private final String userMbti;

    public AttractionAdapter(String userMbti, Listener listener) {
        this.userMbti = userMbti;
        this.listener = listener;
    }

    public void submitList(List<Attraction> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attraction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attraction a = items.get(position);
        holder.tvName.setText(a.name);
        holder.tvCategory.setText(a.category + " · " + a.vibe);
        holder.tvDescription.setText(a.description);
        holder.tvRecommended.setVisibility(a.isRecommendedFor(userMbti) ? View.VISIBLE : View.GONE);

        // Click scurt -> editare
        holder.itemView.setOnClickListener(v -> listener.onEdit(a));
        // Click lung sau butonul de coș -> ștergere
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDelete(a);
            return true;
        });
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(a));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvCategory, tvDescription, tvRecommended;
        final ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvAttractionName);
            tvCategory = itemView.findViewById(R.id.tvAttractionCategory);
            tvDescription = itemView.findViewById(R.id.tvAttractionDescription);
            tvRecommended = itemView.findViewById(R.id.tvRecommendedBadge);
            btnDelete = itemView.findViewById(R.id.btnDeleteAttraction);
        }
    }
}
