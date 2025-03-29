package iut.dam.projet_dev_mobile;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import iut.dam.projet_dev_mobile.entities.TimeSlot;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private List<TimeSlot> timeSlotList;
    private Context context;
    private OnTimeSlotClickListener listener;

    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(TimeSlot timeSlot);
    }

    public TimeSlotAdapter(List<TimeSlot> timeSlotList, Context context) {
        this.timeSlotList = timeSlotList;
        this.context = context;
        if (context instanceof OnTimeSlotClickListener) {
            this.listener = (OnTimeSlotClickListener) context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlotList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timeText = sdf.format(timeSlot.begin) + " - " + sdf.format(timeSlot.end);
        holder.timeTextView.setText(timeText);

        if (timeSlot.maxWattage == -1) { // Créneau non réservable
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            holder.itemView.setClickable(false);
            holder.itemView.setOnClickListener(null); // Désactive le clic
            Log.d("DEBUG", "Créneau désactivé : " + timeText);
        } else { // Créneau réservable
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(v -> {
                Log.d("DEBUG", "Créneau cliqué dans Adapter : " + timeText);
                listener.onTimeSlotClick(timeSlot);
            });
        }
    }





        @Override
    public int getItemCount() {
        return timeSlotList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}

