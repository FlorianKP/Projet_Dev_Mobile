package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; //  secondes
    private ProgressBar progressBar;
    private int progress = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialisation de la ProgressBar
        progressBar = findViewById(R.id.loadingProgressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        // Simulation de chargement
        new Thread(() -> {
            while (progress < 100) {
                progress += 2; // Augmentation plus rapide pour 3 secondes
                handler.post(() -> progressBar.setProgress(progress));

                try {
                    Thread.sleep(SPLASH_DURATION / 50); // Division pour synchroniser avec la durée totale
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Redirection après le chargement
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}