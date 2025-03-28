package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://192.168.1.48/powerhome/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void loginUser(View view) {
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String url = LOGIN_URL + "?email=" + email + "&password=" + password;

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("LoginResponse", "Réponse brute : " + result);

                        Gson gson = new Gson();

                        try {
                            // Tenter de parser la réponse en tant qu'objet LoginResponse
                            LoginResponse response = gson.fromJson(result, LoginResponse.class);

                            // Vérifier si l'objet est bien formé
                            if (response != null && response.token != null) {
                                // L'utilisateur est connecté avec succès
                                Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

                                // Sauvegarde du token dans les préférences partagées
                                getSharedPreferences("app_prefs", MODE_PRIVATE)
                                        .edit()
                                        .putString("user_token", response.token)
                                        .putString("token_expiry", response.expired_at)
                                        .apply();
                                UpdateUI();
                            } else {
                                // Si l'objet JSON ne contient pas "token", afficher l'erreur
                                Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JsonSyntaxException ex) {
                            // Si le JSON n'est pas valide (ex: un simple message d'erreur au lieu d'un objet JSON)
                            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void UpdateUI() {
        Intent intent = new Intent(this, HabitatActivity.class);
        startActivity(intent);
    }

    public void clickLogin(View view) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            String trueEmail = bundle.getString("email");
            String truePassword = bundle.getString("password");
            if (validId(trueEmail, truePassword)) {
                Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundleMain = new Bundle();
                bundleMain.putString("firstname", bundle.getString("firstname"));
                bundleMain.putString("email", trueEmail);
                bundleMain.putString("password", truePassword);
                intentMain.putExtras(bundleMain);
                startActivity(intentMain);
            } else {
                Toast.makeText(this, "Identifiants invalides", Toast.LENGTH_SHORT).show();
            }
        }
        else Toast.makeText(this, "Identifiants invalides", Toast.LENGTH_SHORT).show();
    }
    private boolean validId(String trueEmail, String truePassword){
        EditText etMail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        return (email.equals(trueEmail) && password.equals(truePassword));
    }

    public void clickForgottenPassWord(View view) {
    }

    public void clickNewAccount(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void clickFacebook(View view) {
    }
}