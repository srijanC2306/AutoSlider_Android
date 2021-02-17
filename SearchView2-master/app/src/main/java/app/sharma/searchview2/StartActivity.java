package app.sharma.searchview2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private TextView city;
    private Button changeCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent intent = getIntent();

        city = (TextView) findViewById(R.id.city);
        changeCity = (Button) findViewById(R.id.changecity);

        // Get the extras
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("selected")) {
                String cityName = extras.getString("selected", "KOlKATA");
                city.setText(cityName);
                // TODO: Do something with the value of isNew.
            }
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (city.getText().toString().equals("Dummy") )
            getLocation();

        changeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getLocation(){
        gpsTracker = new GpsTracker(StartActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            city.setText(addresses.get(0).getLocality());
        }else{
            gpsTracker.showSettingsAlert();
        }
    }
}