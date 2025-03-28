package iut.dam.projet_dev_mobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "server";
    private static final String SERVER_URL = "http://192.168.1.22/powerhome_server/getHabitats.php";
    private ProgressDialog pDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView linkHabitat = findViewById(R.id.linkHabitat);
        TextView linkCalendrier = findViewById(R.id.linkCalendrier);
        /*
        setupUI();

        // Récupération des données du serveur
        getRemoteHabitats();*/
        // Redirection vers HabitatActivity
        linkHabitat.setOnClickListener(v -> {
            Intent habitatIntent = new Intent(MainActivity.this, HabitatActivity.class);
            startActivity(habitatIntent);
        });

        // Redirection vers CalendrierActivity
        linkCalendrier.setOnClickListener(v -> {
            Intent calendrierIntent = new Intent(MainActivity.this, CalendrierActivity.class);
            startActivity(calendrierIntent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 🔹 Méthode pour gérer les actions UI (textes et liens)
    /*
    private void setupUI() {
        TextView mainText = findViewById(R.id.mainText);
        TextView linkHabitat = findViewById(R.id.linkHabitat);
        TextView linkCalendrier = findViewById(R.id.linkCalendrier);

        // Récupération des données passées depuis l'autre activité
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String firstname = bundle.getString("firstname");
            String password = bundle.getString("password");
            mainText.setText("Bonjour " + firstname + "! Votre mot de passe est " + password + " !");
        }

        // Redirection vers HabitatActivity
        linkHabitat.setOnClickListener(v -> {
            Intent habitatIntent = new Intent(MainActivity.this, HabitatActivity.class);
            startActivity(habitatIntent);
        });

        // Redirection vers CalendrierActivity
        linkCalendrier.setOnClickListener(v -> {
            Intent calendrierIntent = new Intent(MainActivity.this, CalendrierActivity.class);
            startActivity(calendrierIntent);
        });
    }

    // 🔹 Récupération des habitats depuis le serveur
    private void getRemoteHabitats() {
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
                            Log.d(TAG, "Erreur de connexion au serveur !");
                            Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d(TAG, "Réponse serveur : " + result);
                        Toast.makeText(MainActivity.this, "Données reçues : " + result, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    */
}
