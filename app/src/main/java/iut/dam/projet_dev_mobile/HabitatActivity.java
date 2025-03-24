package iut.dam.projet_dev_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
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

    private static final String SERVER_URL = "http://192.168.1.22/powerhome/getHabitats.php";
    private ListView listView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        // 🔹 INITIALISATION de listView AVANT d’appeler fetchHabitatsFromServer()
        listView = findViewById(R.id.listView);

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

}
