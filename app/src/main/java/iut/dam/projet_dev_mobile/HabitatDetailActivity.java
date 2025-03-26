package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import iut.dam.projet_dev_mobile.entities.Appliance;
import iut.dam.projet_dev_mobile.entities.Habitat;

public class HabitatDetailActivity extends AppCompatActivity {
    private static final String SERVER_URL = "http://192.168.1.22/powerhome/getTotalWattage.php";
    private Habitat habitat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habitat_detail);
        TextView tvHabitat = findViewById(R.id.habitat);
        habitat = (Habitat) getIntent().getSerializableExtra("habitat");
        tvHabitat.setText("Habitat n° " + habitat.id);
        getConsoTotal(habitat.id);
        displayResident();
        displayAllAppliances();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayResident() {
        TextView tvResident = findViewById(R.id.resident);
        if(habitat.user != null)
            tvResident.setText("Résident : " + habitat.user.firstName + " " + habitat.user.lastName);
        else tvResident.setText("Aucun résident");
    }

    private void displayAllAppliances() {
        TextView tvAppliances = findViewById(R.id.appliances);
        String appliances = "Listes des appareils :\n";
        if(!habitat.appliances.isEmpty()) {
            for (Appliance a : habitat.appliances) {
                appliances = appliances + "- " + a.name + " " + a.reference + "    " + a.wattage + " W\n";
            }
            tvAppliances.setText(appliances);
        }
        else tvAppliances.setText("Aucun appareil associé à cet habitat");
    }

    public void getConsoTotal(int habitat_id) {
        Ion.with(this)
                .load("POST", SERVER_URL)
                .setBodyParameter("habitat_id", String.valueOf(habitat_id))
                .asJsonObject()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(HabitatDetailActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        // Extraction du total_wattage à partir de la réponse JSON
                        int totalWattage = result.get("total_wattage").getAsInt(); // Récupère directement la valeur en int

                        // Affichage dans le Log
                        Log.d("TotalWattage", "Total Wattage: " + totalWattage);
                        TextView conso = findViewById(R.id.conso);
                        conso.setText("Consommation totale : " + totalWattage + " W");

                    } catch (Exception ex) {
                        // Gestion des erreurs de parsing JSON
                        Log.e("JSON Parse Error", "Erreur de parsing JSON");
                    }
                });
    }
    public void clickRetour(View view) {
        Intent intent = new Intent(HabitatDetailActivity.this, HabitatActivity.class);
        startActivity(intent);
    }
}