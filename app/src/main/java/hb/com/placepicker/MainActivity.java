package hb.com.placepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private Button mPickerBtn;
    int PLACE_PICKER_REQUEST = 1;
    TextView mLatTv, mLongTV, mAddressTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPickerBtn = (Button) findViewById(R.id.pick_place_btn);
        mLatTv = (TextView) findViewById(R.id.lat_tv);
        mLongTV = (TextView) findViewById(R.id.long_tv);
        mAddressTV = (TextView) findViewById(R.id.address_tv);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(MainActivity.this, MainActivity.this)
                .build();
        mPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // handle connection failures.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Error", connectionResult.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
            //location recieved
                Place place = PlacePicker.getPlace(data, this);
                LatLng s = place.getLatLng();
                String toastMsg = "Lat:" + s.latitude + ",long:" + s.longitude;
                Log.d("Success", toastMsg);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mLatTv.setText("" + s.latitude);
                mLongTV.setText("" + s.longitude);
                mAddressTV.setText(place.getAddress());
            }
        }
    }

}
