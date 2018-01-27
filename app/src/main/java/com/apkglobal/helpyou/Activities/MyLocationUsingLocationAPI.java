package com.apkglobal.helpyou.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkglobal.helpyou.Activities.Helper.Configure;
import com.apkglobal.helpyou.R;
import com.apkglobal.helpyou.Activities.Helper.PermissionUtils;
import com.apkglobal.helpyou.Activities.Helper.PermissionUtils.PermissionResultCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MyLocationUsingLocationAPI extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener,OnRequestPermissionsResultCallback,
        PermissionResultCallback {


    @BindView(R.id.btnSubmit)Button submit;
    @BindView(R.id.btnProceed)Button proceed;
    @BindView(R.id.tvEmpty)TextView tvEmpty;
    @BindView(R.id.rlPickLocation)RelativeLayout rlPick;


    // LogCat tag
    // private static final String TAG = MyLocationUsingHelper.class.getSimpleName();

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    TextView tv_demand_show,tvAddress;
    EditText description,name,contact;
    String service_name,st_name,st_contact,st_description,st_address,type_service,st_city;

    private Location mLastLocation;

    // Google client to interact with Google API

    private GoogleApiClient mGoogleApiClient;

    double latitude;
    double longitude;

    // list of permissions

    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;

    boolean isPermissionGranted;
    ProgressDialog progressDialog;
    Configure configure=new Configure();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        tv_demand_show=findViewById(R.id.tv_demand_show);
        description=findViewById(R.id.et_description);
        name=findViewById(R.id.et_name);
        contact=findViewById(R.id.et_contact);
        tvAddress=findViewById(R.id.tvAddress);
        String show=getIntent().getStringExtra("DEMANDNAME");
        type_service=getIntent().getStringExtra("TYPE");
        if(type_service.equals("getservice"))
        {
            description.setHint("Add Description...");
        }
        else if(type_service.equals("giveservice"))
        {
            description.setHint("Add Experience...");
        }
        tv_demand_show.setText(show);

        ButterKnife.bind(this);

        permissionUtils=new PermissionUtils(MyLocationUsingLocationAPI.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);


        rlPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                } else {

                    if(submit.isEnabled())
                        submit.setEnabled(false);
                    if(proceed.isEnabled())
                        proceed.setEnabled(false);

                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(getApplicationContext(),"Proceeds to fetch data",Toast.LENGTH_SHORT,true).show();
                String sname=tv_demand_show.getText().toString();
                String pass_city = configure.getFetch_city();
                    Intent get_provider=new Intent(MyLocationUsingLocationAPI.this,GetProvider.class);
                    get_provider.putExtra("TYPE2",type_service);
                    get_provider.putExtra("SNAME",sname);
                    get_provider.putExtra("CITY",pass_city);
                    startActivity(get_provider);
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                st_name=name.getText().toString();
                service_name=tv_demand_show.getText().toString();
                st_contact=contact.getText().toString();
                if(type_service.equals("getservice")) {
                    st_description = "Description: "+description.getText().toString();
                }
                else if(type_service.equals("giveservice"))
                {
                    st_description = "Experience: "+description.getText().toString();
                }
                st_address=tvAddress.getText().toString();
                if(st_name.equals("")||st_contact.equals(""))
                {
                    Toasty.error(getApplicationContext(),"Enter All details",Toast.LENGTH_SHORT,true).show();
                }
                else {
                    Send send = new Send();
                    send.execute(service_name, st_name, st_contact, st_description, st_address);

                }
            }
        });

        // check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

    }


    /**
     * Method to display the location on UI
     * */

    private void getLocation() {

        if (isPermissionGranted) {

            try
            {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }

        }

    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public void getAddress()
    {

        Address locationAddress=getAddress(latitude,longitude);

        if(locationAddress!=null)
        {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();
            configure.setFetch_city(city);


            String currentLocation;

            if(!TextUtils.isEmpty(address))
            {
                currentLocation=address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+="\n"+address1;

                if (!TextUtils.isEmpty(city))
                {
                    currentLocation+="\n"+city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+="\n"+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+="\n"+country;

                tvEmpty.setVisibility(View.GONE);
                tvAddress.setText(currentLocation);
                tvAddress.setVisibility(View.VISIBLE);

                if(!submit.isEnabled())
                    submit.setEnabled(true);
                if(!proceed.isEnabled())
                    proceed.setEnabled(true);


            }

        }

    }

    /**
     * Creating google api client object
     * */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MyLocationUsingLocationAPI.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }




    /**
     * Method to verify google play services on the device
     * */

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
        //+ result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    // Permission check functions


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }




    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    public void showToast(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


    private class Send extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MyLocationUsingLocationAPI.this);
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            service_name=strings[0];
            st_name=strings[1];
            st_contact=strings[2];
            st_description=strings[3];
            st_address=strings[4];
            st_city=configure.getFetch_city();

            // attendance=Integer.parseInt(sattendance);


            try {
                URL url=null;
                if(type_service.equals("getservice"))
                {
                    url=new URL("http://geekmozo.com/insert_user.php");
                }
                else if(type_service.equals("giveservice"))
                {
                    url=new URL("http://geekmozo.com/insert_provider.php");
                }
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                String data= URLEncoder.encode("service_name","utf-8")+"="+URLEncoder.encode(service_name,"utf-8")+"&&"+URLEncoder.encode("user_name","utf-8")+"="+URLEncoder.encode(st_name,"utf-8")+"&&"+URLEncoder.encode("user_contact","utf-8")+"="+URLEncoder.encode(st_contact,"utf-8")+"&&"+URLEncoder.encode("description","utf-8")+"="+URLEncoder.encode(st_description,"utf-8")+"&&"+URLEncoder.encode("address","utf-8")+"="+URLEncoder.encode(st_address,"utf-8")+"&&"+URLEncoder.encode("city","utf-8")+"="+URLEncoder.encode(st_city,"utf-8");



                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            return "Details Submitted";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            name.setText("");
            contact.setText("");
            description.setText("");
            tvAddress.setText("");
            Toasty.info(getApplicationContext(), s, Toast.LENGTH_SHORT,true).show();
        }
    }
}


