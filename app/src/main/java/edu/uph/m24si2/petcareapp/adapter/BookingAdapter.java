package edu.uph.m24si2.petcareapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.R;
import edu.uph.m24si2.petcareapp.model.BookingItem;
import edu.uph.m24si2.petcareapp.model.Pet;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context context;
    private List<BookingItem> bookingItemList;
    private List<Pet> petList;
    private OnBookingActionListener listener;

    public interface OnBookingActionListener {
        void onComplete(BookingItem bookingItem);
    }

    public BookingAdapter(Context context, List<BookingItem> bookingItemList, List<Pet> petList, OnBookingActionListener listener) {
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
        } else if (bookingItem.getStatus().equalsIgnoreCase("Menunggu Konfirmasi")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // Dark orange
            holder.btnComplete.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#757575")); // Grey
            holder.btnComplete.setVisibility(View.GONE);
        }

        holder.btnComplete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComplete(bookingItem);
            }
        });

        // Hide rate button in booking schedule
        holder.btnRate.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return bookingItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPetName, tvService, tvDate, tvTime, tvStatus, tvPrice;
        Button btnComplete, btnRate;

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
        }
    }
}
