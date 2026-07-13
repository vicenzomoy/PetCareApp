package edu.uph.m24si2.petcareapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import edu.uph.m24si2.petcareapp.R;
import edu.uph.m24si2.petcareapp.model.Booking;
import edu.uph.m24si2.petcareapp.model.Pet;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context context;
    private List<Booking> bookingList;
    private List<Pet> petList;

    public BookingAdapter(Context context, List<Booking> bookingList, List<Pet> petList) {
        this.context = context;
        this.bookingList = bookingList;
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
        Booking booking = bookingList.get(position);

        String petName = "Unknown Pet";
        for (Pet pet : petList) {
            if (pet.getId() == booking.getPetId()) {
                petName = pet.getName();
                break;
            }
        }

        holder.tvPetName.setText(petName);
        holder.tvService.setText(booking.getService());
        holder.tvDate.setText(booking.getBookingDate());
        holder.tvTime.setText(booking.getBookingTime());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "Rp %,d", booking.getPrice()));
        
        holder.tvStatus.setText(booking.getStatus());
        
        // Dynamic status coloring
        if (booking.getStatus().equalsIgnoreCase("Paid")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_paid);
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // Dark green
        } else if (booking.getStatus().equalsIgnoreCase("Pending")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // Dark orange
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.tvStatus.setTextColor(Color.parseColor("#757575")); // Grey
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
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
