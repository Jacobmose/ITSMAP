/*
package com.jacobmosehansen.themeproject.Tools;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.jacobmosehansen.themeproject.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

*/
/**
 * Created by Jacobmosehansen on 08-10-2015.
 *//*

public class FetchCityIntentService extends IntentService {

    Geocoder geocoder;
    protected ResultReceiver mReceiver;

    */
/**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     *//*

    public FetchCityIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        geocoder = new Geocoder(this, Locale.getDefault());

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        List<Address> addresses = null;

        try{
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        }catch (IOException ioException) {
            // IO Error

        }catch (IllegalArgumentException illegalArgumentException){
            // Invalid latitude and longitude
        }

        if (addresses == null || addresses.size() == 0){
            // No address found

            deliverResultToReceiver(Constants.FAILURE_RESULT, "na");
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            // Address found

            deliverResultToReceiver(Constants.SUCCES_RESULT, TextUtils.join(System.getProperty("line.separator"),addressFragments));
        }

    }

    public void deliverResultToReceiver(int resultCode, String message){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}


*/
