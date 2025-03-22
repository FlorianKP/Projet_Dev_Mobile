package iut.dam.projet_dev_mobile;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import iut.dam.projet_dev_mobile.entities.Appliance;
import iut.dam.projet_dev_mobile.entities.Habitat;

public class HabitatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        setTextView();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private List<Habitat> setPeople() {
        Appliance ap1 = new Appliance(1,"machine à laver", "123",80);
        Appliance ap2 = new Appliance(2,"machine à laver", "1234",80);
        Appliance ap3 = new Appliance(3,"aspirateur", "12345",50);
        Appliance ap4 = new Appliance(3,"fer à repasser", "123456",30);
        Habitat h1 = new Habitat(1, 10,1);
        Habitat h3 = new Habitat(2, 10,4);
        Habitat h2 = new Habitat(3, 10,2);
        Habitat h4 = new Habitat(4, 10,1);
        h1.addAppliance(ap1);
        h1.addAppliance(ap4);
        h2.addAppliance(ap1);
        h2.addAppliance(ap3);
        h2.addAppliance(ap4);
        h4.addAppliance(ap3);
        List<Habitat> habitats = new ArrayList<>();
        habitats.add(h1);
        habitats.add(h2);
        habitats.add(h3);
        habitats.add(h4);
        return habitats;
}
private void setTextView() {
        ListView tv=  findViewById(R.id.textview);
        List<Habitat> habitats = setPeople();

        HabitatAdapter adapter =
                new HabitatAdapter(this,
                        R.layout.item_habitat,
                        habitats);
        tv.setAdapter(adapter);
    }



}