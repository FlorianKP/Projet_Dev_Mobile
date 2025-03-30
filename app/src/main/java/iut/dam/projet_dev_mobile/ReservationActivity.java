package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import iut.dam.projet_dev_mobile.entities.Habitat;

public class ReservationActivity extends AppCompatActivity {
    private static final String SERVER_URL = "http://10.0.2.2/powerhome/registerReservation.php";

    private String appliance_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation);

        Button btnValider = findViewById(R.id.btnValider);
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvIdAppareil = findViewById(R.id.idAppareil);
                appliance_id = tvIdAppareil.getText().toString().trim();
                if(!appliance_id.isEmpty()){
                    registerReservation();
                }
            }
        });

        ImageButton btnRetourMain = findViewById(R.id.btnRetourMain);
        btnRetourMain.setOnClickListener(v->{
            Intent intent = new Intent(ReservationActivity.this, CalendrierActivity.class);
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerReservation() {
        Ion.with(this)
                .load("POST",SERVER_URL)
                .setBodyParameter("appliance_id", appliance_id)
                .setBodyParameter("time_slot_id", getIntent().getStringExtra("time_slot_id"))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(ReservationActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("ServerResponse", "Réponse brute : " + result);


                        try {
                            JsonObject jsonResponse = JsonParser.parseString(result).getAsJsonObject();

                            if (jsonResponse.has("message")) {
                                Toast.makeText(ReservationActivity.this, jsonResponse.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                UpdateUI();
                            } else if (jsonResponse.has("error")) {
                                Toast.makeText(ReservationActivity.this, jsonResponse.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JsonSyntaxException ex) {
                            Toast.makeText(ReservationActivity.this, "Réponse invalide du serveur", Toast.LENGTH_SHORT).show();
                            Log.e("JSONError", "Erreur de parsing JSON: " + ex.getMessage());
                        }
                    }
                });
    }

    private void UpdateUI() {
        Intent intent = new Intent(ReservationActivity.this, CalendrierActivity.class);
        startActivity(intent);
        finish();
    }
}