package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.koushikdutta.ion.IonContext;

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

public class CalendrierActivity extends AppCompatActivity implements TimeSlotAdapter.OnTimeSlotClickListener {
    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<TimeSlot> timeSlotList = new ArrayList<>();
    private TextView tvSelectedDate;
    private Button btnPrevious, btnNext;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance(); // Stocke la date sélectionnée

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimeSlotAdapter(timeSlotList, this);
        recyclerView.setAdapter(adapter);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        ImageButton btnRetourMain = findViewById(R.id.btnRetourMain);
        btnRetourMain.setOnClickListener(v -> {
            Intent intent = new Intent(CalendrierActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        );

        updateDateDisplay(); // Affiche la date initiale
        fetchTimeSlots(); // Charge les créneaux du jour

        // Gestion du bouton "Jour précédent"
        btnPrevious.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDateDisplay();
            fetchTimeSlots();
            fetchTimeSlotConsumption();
        });

        // Gestion du bouton "Jour suivant"
        btnNext.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDateDisplay();
            fetchTimeSlots();
            fetchTimeSlotConsumption();
        });
        adapter.notifyDataSetChanged();

        fetchTimeSlotConsumption();
    }

    private void fetchTimeSlotConsumption() {
        String url = "http://10.0.2.2/powerhome/getPercentageWattage.php";

        Ion.with(this)
                .load(url)
                .asJsonArray()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Log.e("ERROR","Erreur de connexion");
                        return;
                    }
                    try {
                        for (int i = 0; i < result.size(); i++) {
                            JsonObject jsonSlot = result.get(i).getAsJsonObject();

                            int slotId = jsonSlot.get("time_slot_id").getAsInt();
                            float consumptionPercentage = jsonSlot.get("consumption_percentage").getAsFloat();

                            Log.d("TimeSlot", "ID: " + slotId + " | %: " + consumptionPercentage);

                            // Mettre à jour la liste des créneaux
                            updateTimeSlot(slotId, consumptionPercentage);
                        }

                        //  Rafraîchir la liste après toutes les mises à jour


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }


    public void updateTimeSlot(int slotId, float consumptionPercentage) {
        for (TimeSlot slot : timeSlotList) {
            if (slot.id == slotId) {
                slot.consumptionPercentage = consumptionPercentage;
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Met à jour l'affichage de la date
    private void updateDateDisplay() {
        tvSelectedDate.setText(sdf.format(calendar.getTime()));
    }

    private void fetchTimeSlots() {
        String selectedDate = sdf.format(calendar.getTime());
        String url = "http://10.0.2.2/powerhome/get_timeslots.php?date=" + selectedDate;

        Ion.with(this)
                .load(url)
                .asJsonArray()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Création de la liste complète des créneaux de 8h à 22h
                    timeSlotList.clear();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 8);
                    cal.set(Calendar.MINUTE, 0);

                    List<TimeSlot> availableSlots = new ArrayList<>();

                    for (int i = 8; i < 22; i++) { // Créneaux de 8h à 22h
                        Date begin = cal.getTime();
                        cal.add(Calendar.HOUR_OF_DAY, 1);
                        Date end = cal.getTime();
                        timeSlotList.add(new TimeSlot(-1, begin, end, 0)); // Créneaux non réservables
                    }

                    // Marquer les créneaux disponibles en BD comme "réservables"
                    for (int i = 0; i < result.size(); i++) {
                        JsonObject obj = result.get(i).getAsJsonObject();
                        try {
                            int id = obj.get("id").getAsInt();
                            String beginStr = obj.get("begin").getAsString();
                            String endStr = obj.get("end").getAsString();
                            int maxWattage = obj.get("max_wattage").getAsInt();

                            Date begin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginStr);
                            Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endStr);

                            // Comparer les heures et les minutes des créneaux
                            for (int j = 0; j < timeSlotList.size(); j++) {
                                TimeSlot slot = timeSlotList.get(j);
                                // Comparaison des heures et des minutes
                                if (slot.begin.getHours() == begin.getHours() && slot.begin.getMinutes() == begin.getMinutes()) {
                                    // Remplacer le créneau non réservable par celui de la BD
                                    timeSlotList.set(j, new TimeSlot(id, begin, end, maxWattage));
                                }
                            }

                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();

                    fetchTimeSlotConsumption();
                });
    }



    @Override
    public void onTimeSlotClick(TimeSlot timeSlot) {
        Toast.makeText(this, "Créneau sélectionné : " + timeSlot.begin + " - " + timeSlot.end, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CalendrierActivity.this, ReservationActivity.class);
        intent.putExtra("time_slot_id", String.valueOf(timeSlot.id));
        startActivity(intent);
    }
}
