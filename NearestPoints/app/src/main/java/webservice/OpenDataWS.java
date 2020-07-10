package webservice;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import beans.Fields;
import beans.Resultat;
import beans.StreamIn;

/**
 * Created by booth09-mgr2 on 22/03/2017.
 */

public class OpenDataWS {

    public static ArrayList<Fields> getFieldsDuServeur(String WS_URL) throws Exception {
        //Log.w("T", WS_URL);
        //Lancer la requête
        String reponseEnJson = OkHttpUtils.sendGetOkHttpRequest( WS_URL);
        //Log.w("JSON" , reponseEnJson);
        //Parser le résultat
        Gson gson = new Gson();

        StreamIn streamIn = gson.fromJson(reponseEnJson, StreamIn.class);


        ///Extraire les Fields
        ArrayList<Fields> fields = new ArrayList<>();
        if (streamIn == null) {
            throw new Exception("Variable resultat à null");
        } else if (streamIn.getResultat() != null) {
            for (Resultat resultat : streamIn.getResultat()) {
                if (resultat.getFields() != null) {
                    fields.add(resultat.getFields());

                }
            }
        }
        Log.w("JSON" ,""+fields.size());
        return fields;
    }
}