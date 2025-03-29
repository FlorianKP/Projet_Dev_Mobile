package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://10.0.2.2/powerhome/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void clickRegister(View view) {
        EditText etNom = findViewById(R.id.etNom);
        String lastname = etNom.getText().toString().trim();
        EditText etPrenom = findViewById(R.id.etPrenom);
        String firstname = etPrenom.getText().toString().trim();
        EditText etEmail = findViewById(R.id.etEmail);
        String email = etEmail.getText().toString().trim();
        EditText etPassword = findViewById(R.id.etPassword);
        String password = etPassword.getText().toString().trim();
        if(isValid(lastname)){
            if(isValid(firstname)){
                if(isValidEmail(email)){
                    if(passwordValid(password)){
                        registerUser(lastname, firstname, email, password);
                        /*
                        Intent intent = new Intent(this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("firstname", firstname);
                        bundle.putString("email", email);
                        bundle.putString("password", password);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Toast t = Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT);
                        t.show();*/
                    }
                    else{
                        Toast t = Toast.makeText(this, "Mot de passe invalide", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
                else{
                    Toast t = Toast.makeText(this, "Email invalide", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
            else{
                Toast t = Toast.makeText(this, "Prénom invalide", Toast.LENGTH_SHORT);
                t.show();
            }
        }
        else{
            Toast t = Toast.makeText(this, "Nom invalide", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void registerUser(String lastname, String firstname, String email, String password) {


        Log.d("RegisterDebug", "Envoi des données à : " + LOGIN_URL);
        Log.d("RegisterDebug", "Données envoyées : " +
                "lastname=" + lastname + ", firstname=" + firstname + ", email=" + email + ", password=" + password);

        Ion.with(this)
                .load("POST", LOGIN_URL)  // Assure-toi que c'est bien POST
                .setBodyParameter("lastname", lastname)
                .setBodyParameter("firstname", firstname)
                .setBodyParameter("email", email)
                .setBodyParameter("password", password)
                .asString()  // On récupère la réponse en texte brut
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(RegisterActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            Log.e("RegisterError", "Exception: ", e);
                            return;
                        }

                        Log.d("ServerResponse", "Réponse brute : " + result);

                        try {
                            JsonObject jsonResponse = JsonParser.parseString(result).getAsJsonObject();

                            if (jsonResponse.has("message")) {
                                Toast.makeText(RegisterActivity.this, jsonResponse.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                UpdateUI();
                            } else if (jsonResponse.has("error")) {
                                Toast.makeText(RegisterActivity.this, jsonResponse.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JsonSyntaxException ex) {
                            Toast.makeText(RegisterActivity.this, "Réponse invalide du serveur", Toast.LENGTH_SHORT).show();
                            Log.e("JSONError", "Erreur de parsing JSON: " + ex.getMessage());
                        }
                    }
                });
    }
    public void clickHaveAccount(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void UpdateUI() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    private boolean passwordValid(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    private boolean isValid(String text) {
        return text.length()>=2 && text.length()<26 && text.matches("[\\p{L}\\s]+");
    }
}