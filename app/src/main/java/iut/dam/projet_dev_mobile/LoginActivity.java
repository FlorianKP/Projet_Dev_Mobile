package iut.dam.projet_dev_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

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