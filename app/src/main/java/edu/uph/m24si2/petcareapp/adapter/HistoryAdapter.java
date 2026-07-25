package edu.uph.m24si2.petcareapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.R;
import edu.uph.m24si2.petcareapp.model.BookingItem;
import edu.uph.m24si2.petcareapp.model.Pet;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<BookingItem> bookingItemList;
    private List<Pet> petList;
    private OnHistoryActionListener listener;

    public interface OnHistoryActionListener {
        void onComplete(BookingItem bookingItem);
        void onRate(BookingItem bookingItem);
    }

    public HistoryAdapter(Context context, List<BookingItem> bookingItemList, List<Pet> petList, OnHistoryActionListener listener) {
        this.context = context;
        this.bookingItemList = bookingItemList;
        this.petList = petList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingItem bookingItem = bookingItemList.get(position);

        String petName = "Unknown Pet";
        for (Pet pet : petList) {
            if (pet.getId() == bookingItem.getPetId()) {
                petName = pet.getName();
                break;
            }
        }

        holder.tvPetName.setText(petName);
        holder.tvService.setText(bookingItem.getServiceName());
        holder.tvDate.setText(bookingItem.getDate());
        holder.tvTime.setText(bookingItem.getTime());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "Rp %,d", bookingItem.getPrice()));
        
        holder.tvStatus.setText(bookingItem.getStatus());
        
        // Dynamic status coloring
        if (bookingItem.getStatus().equalsIgnoreCase("Selesai") || bookingItem.getStatus().equalsIgnoreCase("Paid")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_paid);
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // Dark green
            holder.btnComplete.setVisibility(View.GONE);
            
            if (bookingItem.getRating() > 0) {
                holder.btnRate.setVisibility(View.GONE);
                holder.ratingDisplay.setVisibility(View.VISIBLE);
                holder.ratingDisplay.setRating(bookingItem.getRating());
            } else {
                holder.btnRate.setVisibility(View.VISIBLE);
                holder.ratingDisplay.setVisibility(View.GONE);
            }
        } else if (bookingItem.getStatus().equalsIgnoreCase("Menunggu Konfirmasi")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // Dark orange
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnComplete.setText("Selesaikan");
            holder.btnRate.setVisibility(View.GONE);
            holder.ratingDisplay.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#757575")); // Grey
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnRate.setVisibility(View.GONE);
            holder.ratingDisplay.setVisibility(View.GONE);
        }

        holder.btnComplete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComplete(bookingItem);
            }
        });

        holder.btnRate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRate(bookingItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPetName, tvService, tvDate, tvTime, tvStatus, tvPrice;
        Button btnComplete, btnRate;
        RatingBar ratingDisplay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvService = itemView.findViewById(R.id.tvService);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnRate = itemView.findViewById(R.id.btnRate);
            ratingDisplay = itemView.findViewById(R.id.ratingDisplay);
        }
    }
}
