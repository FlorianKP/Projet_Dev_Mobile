package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import iut.dam.projet_dev_mobile.entities.TimeSlot;

public class CalendrierActivity extends AppCompatActivity implements TimeSlotAdapter.OnTimeSlotClickListener{

    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<TimeSlot> timeSlotList = new ArrayList<>();
    private static final String URL_TIMESLOTS = "http://10.0.2.2/powerhome/get_timeslots.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TimeSlotAdapter(timeSlotList, this);
        recyclerView.setAdapter(adapter);

        fetchTimeSlots();
        Log.d("DEBUG", "Nombre de créneaux récupérés: " + timeSlotList.size());

    }

    private void fetchTimeSlots() {
        Log.d("DEBUG", "Lancement de la requête vers l'API...");

        Ion.with(this)
                .load("GET", URL_TIMESLOTS)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Log.e("ERROR", "Erreur de connexion : " + e.getMessage(), e);
                        Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        List<TimeSlot> fetchedSlots = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            int id = obj.getInt("id");
                            Date begin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("begin"));
                            Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("end"));
                            int maxWattage = obj.getInt("max_wattage");

                            fetchedSlots.add(new TimeSlot(id, begin, end, maxWattage));
                        }
                        Log.d("DEBUG", "Créneaux récupérés depuis la BD :");
                        for (TimeSlot slot : fetchedSlots) {
                            Log.d("DEBUG", "ID: " + slot.id + " | " +
                                    new SimpleDateFormat("HH:mm").format(slot.begin) + " - " +
                                    new SimpleDateFormat("HH:mm").format(slot.end) +
                                    " | maxWattage: " + slot.maxWattage);
                        }


                        // Maintenant, on génère la liste complète des créneaux
                        generateCompleteTimeSlots(fetchedSlots);

                    } catch (JSONException | ParseException ex) {
                        Log.e("ERROR", "Erreur lors du parsing JSON : " + ex.getMessage(), ex);
                    }
                });
    }

    private void generateCompleteTimeSlots(List<TimeSlot> fetchedSlots) {
        List<TimeSlot> completeTimeSlots = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8); // Début à 08h00
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, 21); // Fin à 21h00
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);

        while (calendar.before(endCalendar)) {
            Date startTime = calendar.getTime();
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date endTime = calendar.getTime();

            boolean isReservable = false;
            TimeSlot reservableSlot = null;

            for (TimeSlot slot : fetchedSlots) {
                // Comparer en utilisant getTime() pour éviter les problèmes de millisecondes
                if (slot.begin.getTime() == startTime.getTime() && slot.end.getTime() == endTime.getTime()) {
                    isReservable = true;
                    reservableSlot = slot;
                    break;
                }
            }

            if (isReservable) {
                completeTimeSlots.add(reservableSlot); // Ajoute le créneau récupéré
            } else {
                completeTimeSlots.add(new TimeSlot(0, startTime, endTime, -1)); // Créneau non réservable
            }
        }

        // Mise à jour de l'affichage
        timeSlotList.clear();
        timeSlotList.addAll(completeTimeSlots);
        Log.d("DEBUG", "Créneaux ajoutés dans la liste affichée :");
        for (TimeSlot slot : timeSlotList) {
            Log.d("DEBUG", new SimpleDateFormat("HH:mm").format(slot.begin) + " - " +
                    new SimpleDateFormat("HH:mm").format(slot.end) +
                    " | maxWattage: " + slot.maxWattage);
        }

        adapter.notifyDataSetChanged();
    }



    @Override
    public void onTimeSlotClick(TimeSlot timeSlot) {
        Log.d("DEBUG", "Créneau sélectionné : " + timeSlot.begin + " - " + timeSlot.end);
    }

}
