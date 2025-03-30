package iut.dam.projet_dev_mobile;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.IonContext;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import iut.dam.projet_dev_mobile.entities.TimeSlot;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<TimeSlot> timeSlotList;
    private OnTimeSlotClickListener listener;

    private final String url= "http://10.0.2.2/powerhome/getPercentageWattage.php";
    public TimeSlotAdapter(List<TimeSlot> timeSlotList, OnTimeSlotClickListener listener) {
        this.timeSlotList = timeSlotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot slot = timeSlotList.get(position);
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");

        holder.timeTextView.setText(sdfHour.format(slot.begin) + " - " + sdfHour.format(slot.end));

        if (slot.id == -1) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            holder.itemView.setEnabled(false);
        } else {
            // Définir la couleur en fonction de la consommation
            int color;
            if (slot.consumptionPercentage <= 30) {
                color = Color.GREEN;
            } else if (slot.consumptionPercentage <= 70) {
                color = Color.rgb(255, 165, 0); // Orange
            } else {
                color = Color.RED;
            }

            holder.itemView.setBackgroundColor(color);
            holder.itemView.setEnabled(true);

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTimeSlotClick(slot);
                }
            });
        }
    }








    @Override
    public int getItemCount() {
        return timeSlotList.size();
    }

    // ✅ **Ajoute cette classe interne pour résoudre l’erreur**
    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }

    // ✅ **Ajoute aussi cette interface pour gérer le clic**
    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(TimeSlot timeSlot);
    }
}


