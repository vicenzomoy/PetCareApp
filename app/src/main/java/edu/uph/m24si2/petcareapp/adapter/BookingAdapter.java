package edu.uph.m24si2.petcareapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.R;
import edu.uph.m24si2.petcareapp.model.BookingGrooming;
import edu.uph.m24si2.petcareapp.model.Pet;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context context;
    private List<BookingGrooming> bookingGroomingList;
    private List<Pet> petList;

    public BookingAdapter(Context context, List<BookingGrooming> bookingGroomingList, List<Pet> petList) {
        this.context = context;
        this.bookingGroomingList = bookingGroomingList;
        this.petList = petList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingGrooming bookingGrooming = bookingGroomingList.get(position);

        String petName = "Unknown Pet";
        for (Pet pet : petList) {
            if (pet.getId() == bookingGrooming.getPetId()) {
                petName = pet.getName();
                break;
            }
        }

        holder.tvPetName.setText(petName);
        holder.tvService.setText(bookingGrooming.getService());
        holder.tvDate.setText(bookingGrooming.getBookingDate());
        holder.tvTime.setText(bookingGrooming.getBookingTime());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "Rp %,d", bookingGrooming.getPrice()));
        
        holder.tvStatus.setText(bookingGrooming.getStatus());
        
        // Dynamic status coloring
        if (bookingGrooming.getStatus().equalsIgnoreCase("Paid")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_paid);
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // Dark green
        } else if (bookingGrooming.getStatus().equalsIgnoreCase("Pending")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // Dark orange
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#757575")); // Grey
        }
    }

    @Override
    public int getItemCount() {
        return bookingGroomingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPetName, tvService, tvDate, tvTime, tvStatus, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvService = itemView.findViewById(R.id.tvService);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
