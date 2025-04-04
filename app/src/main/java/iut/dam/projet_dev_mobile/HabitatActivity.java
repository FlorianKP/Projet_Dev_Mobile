package iut.dam.projet_dev_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import iut.dam.projet_dev_mobile.entities.Habitat;

public class HabitatActivity extends AppCompatActivity {

    private static final String SERVER_URL = "http://10.0.2.2/powerhome/getHabitats_v3.php";
    private ListView listView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();

        // 🔹 INITIALISATION de listView AVANT d’appeler fetchHabitatsFromServer()
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Animation de pression initiale
                Animation press = AnimationUtils.loadAnimation(HabitatActivity.this, R.anim.bounce);
                view.startAnimation(press);

                // Animation de rebond après un court délai
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation bounce = AnimationUtils.loadAnimation(HabitatActivity.this, R.anim.bounce);
                        view.startAnimation(bounce);

                        // Transition après l'animation
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Habitat selectedHabitat = (Habitat) parent.getItemAtPosition(position);
                                Intent intent = new Intent(HabitatActivity.this, HabitatDetailActivity.class);
                                intent.putExtra("habitat", selectedHabitat);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }, 300);
                    }
                }, 100);
            }
        });

        ImageButton btnRetourMain = findViewById(R.id.btnRetourMain);
        btnRetourMain.setOnClickListener(v -> {
            Intent intent = new Intent(HabitatActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        fetchHabitatsFromServer(); // Appel pour récupérer les habitats

        // Gérer les Insets pour éviter que l'interface ne soit coupée par les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void fetchHabitatsFromServer() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Chargement des habitats...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        Ion.with(this)
                .load(SERVER_URL)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        pDialog.dismiss();
                        if (e != null) {
                            Toast.makeText(HabitatActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Habitat> habitats = parseJsonToHabitats(result);
                        if (habitats != null) {
                            displayHabitats(habitats);
                        } else {
                            Toast.makeText(HabitatActivity.this, "Erreur de parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private List<Habitat> parseJsonToHabitats(String json) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Habitat>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void displayHabitats(List<Habitat> habitats) {
        HabitatAdapter adapter = new HabitatAdapter(this, R.layout.item_habitat, habitats);
        listView.setAdapter(adapter);
    }
    public void clickRetour(View view) {
        Intent intent = new Intent(HabitatActivity.this, HabitatActivity.class);
        startActivity(intent);
    }
}
