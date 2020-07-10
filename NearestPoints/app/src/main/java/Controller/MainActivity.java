package Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearestpoints.R;

import java.util.ArrayList;

import beans.Fields;
import view.FieldAdapter;
import webservice.OpenDataWS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FieldAdapter.OnFieldsListener, LocationListener {
    //Constantes
    private static final int FINE_LOCATION_REQ_CODE = 64;

    // composants graphique
    private Button bt_getP, bt_desable;
    private EditText editText2;
    private EditText editText3;
    private TextView tv_info;
    private Button button;
    private RecyclerView rv;
    // Données

    private ArrayList<Fields> fields;
    //outil
    private FieldAdapter fieldAdapter;
    private LocationManager locationMgr = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText2 = (EditText)findViewById( R.id.editText2 );
        editText3 = (EditText)findViewById( R.id.editText3 );
        tv_info = (TextView) findViewById( R.id.tv_info );
        button = (Button)findViewById( R.id.button );
        bt_getP = (Button)findViewById( R.id.bt_getP );
        bt_desable= (Button)findViewById( R.id.bt_desable );

        rv = (RecyclerView)findViewById( R.id.rv );
        button.setOnClickListener(this);

        fields =new ArrayList<>();
        fieldAdapter = new FieldAdapter(fields,this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(fieldAdapter);
        /*-----------------------------------------*/
        bt_getP.setOnClickListener(this);
        bt_desable.setOnClickListener(this);
        /*-----------------------------------------*/
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }
    @Override
    public void onClick(View v) {
        if (v == bt_getP) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //J'ai la permission
                demarrerLocalisation();
            }
            else {
                //je demande la permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQ_CODE);
            }
        }
        else if (v == bt_desable) {
            arreterLocalisation();
        }

        try {
            MonAT monAT = new MonAT();
            monAT.execute();
            fieldAdapter.notifyDataSetChanged();
        }catch (Exception e){
            tv_info.setText(e.getMessage());
        }


    }


    @Override
    public void onClick(Fields fields) {
/////////


        //editText2.setText(Double.toString(fields.getLatitude()));
        //editText3.setText(Double.toString(fields.getLongitude()));
        /*try {
            MonAT monAT = new MonAT();
            monAT.execute();
        }catch (Exception e){
            tv_info.setText(e.getMessage());
        }
         */

        Intent intent =new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.FIELED_KEY,fields);
        startActivity(intent);


        ////////


    }

    public class MonAT extends AsyncTask{

        ArrayList<Fields> resultat=null;
        Exception exception=null;
        @Override
        protected Object doInBackground(Object[] objects) {
            String url="http://127.0.0.1:8080/nearestPoints/"+editText2.getText()+"/"+editText3.getText();
            try {
                resultat=OpenDataWS.getFieldsDuServeur(url);
            } catch (java.lang.Exception e) {
                exception =e;
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);

            if(exception !=null ){
                tv_info.setText(exception.getMessage());
            }
            else{
                fields.clear();
                fields.addAll(resultat);
            }


        }
    }

/*-------------Locatisation-------------------*/
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == FINE_LOCATION_REQ_CODE) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //J'ai la permission
            demarrerLocalisation();
        }
        else {
            Toast.makeText(this, "On ne peut pas utiliser la localisation sans la permission", Toast.LENGTH_SHORT).show();
        }
    }
}

    private void demarrerLocalisation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //On teste si le provider existe avant de s'y abonner
        if (locationMgr.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
        }

        if (locationMgr.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        }
    }

    private void arreterLocalisation() {

        locationMgr.removeUpdates(this);
    }

    /* ---------------------------------
    // CallBack location
    // -------------------------------- */

    @Override
    public void onLocationChanged(Location location) {
        editText2.setText(Double.toString(location.getLatitude()));
        editText3.setText(Double.toString(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, provider + " activé", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, provider + " désactivé", Toast.LENGTH_SHORT).show();
    }
    /*--------------Fin Localisation----------*/

}
