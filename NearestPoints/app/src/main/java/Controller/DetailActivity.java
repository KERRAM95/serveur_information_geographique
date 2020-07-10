package Controller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nearestpoints.R;
import java.util.ArrayList;
import beans.Fields;
import view.FieldAdapter;
import view2.FieldAdapterPop;
import webservice.OpenDataWS;
public class DetailActivity extends AppCompatActivity implements FieldAdapterPop.OnFieldsListener, FieldAdapter.OnFieldsListener {
    ///Field_key
    public final static String FIELED_KEY="FIELED_KEY";
    //outil

    //cordonnées
    public double lat;
    public double longt;
    // données
    private ArrayList<Fields> fields;
    //outil
    private FieldAdapter fieldAdapterpop;
    private RecyclerView rvpop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Fields fields1= (Fields) getIntent().getExtras().getSerializable(FIELED_KEY);

        rvpop = (RecyclerView)findViewById( R.id.rvpop );
        fields =new ArrayList<>();
        fieldAdapterpop = new FieldAdapter(fields,this);
        rvpop.setLayoutManager(new LinearLayoutManager(this));
        rvpop.setAdapter(fieldAdapterpop);



         lat = fields1.getLatitude();
         longt = fields1.getLongitude();


        try {
            MonA monAt = new MonA();
            monAt.execute();
        }catch (Exception e){

        }


    }

    @Override
    public void onClick(Fields fields) {
       // Actualisation des données de navigation à savoir langitude et longitud
        lat = fields.getLatitude();
        longt = fields.getLongitude();
        try {
            MonA monAt = new MonA();
            monAt.execute();
        }catch (Exception e){

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class MonA extends AsyncTask {

        ArrayList<Fields> StreamIn=null;
        Exception exception=null;

        @Override
        protected Object doInBackground(Object[] objects) {

            String url="http://127.0.0.1:8080/nearestPoints/"+lat+"/"+longt;
            try {
                StreamIn= OpenDataWS.getFieldsDuServeur(url);
            } catch (java.lang.Exception e) {
                exception =e;
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);

            if(exception !=null ){
                Log.e("TAG",""+exception);
            }
            else{

            }

            fields.clear();
            fields.addAll(StreamIn);
            fieldAdapterpop.notifyDataSetChanged();


        }


    }
}
