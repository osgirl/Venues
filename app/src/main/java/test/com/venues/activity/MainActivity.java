package test.com.venues.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import test.com.venues.Preferences.PreferencesManager;
import test.com.venues.R;
import test.com.venues.adapter.VenuesAdapter;
import test.com.venues.model.Venues;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    ArrayList<Venues> mArrayLisVenues = null;
    protected GoogleApiClient mGoogleApiClient;
    private int REQUEST_CODE_ASK_PERMISSIONS = 20;
    private ProgressBar mProgressbar;
    private ImageView mIVSort;
    private RecyclerView recyclerView;
    private LatLng mCurrentLocation;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressbar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        mProgressbar.setVisibility(View.VISIBLE);
        mIVSort = (ImageView) findViewById(R.id.iv_sort);
        mIVSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSortDialog();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, MainActivity.this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
                .build();

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://movesync-qa.dcsg.com/dsglabs/mobile/api/venue/";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("response ---- " + response);
                            JSONObject jsonObjMain = new JSONObject(response);
                            JSONArray jsonArrayVenues = jsonObjMain.getJSONArray("venues");
                            mArrayLisVenues = null;
                            mArrayLisVenues = new ArrayList<>();
                            for (int i = 0; i < jsonArrayVenues.length(); i++) {
                                JSONObject jsonObjVenue = jsonArrayVenues.getJSONObject(i);
                                Venues been = new Venues();
                                been.setId(jsonObjVenue.getString("id"));
                                if (jsonObjVenue.has("name"))
                                    been.setName(jsonObjVenue.getString("name"));
                                if (jsonObjVenue.has("url"))
                                    been.setUrl(jsonObjVenue.getString("url"));
                                if (jsonObjVenue.has("verified"))
                                    been.setVerified(jsonObjVenue.getBoolean("verified"));
                                if (jsonObjVenue.has("ratingColor"))
                                    been.setRatingcolor(jsonObjVenue.getString("ratingColor"));
                                if (jsonObjVenue.has("ratingSignals"))
                                    been.setRatingSignals(jsonObjVenue.getString("ratingSignals"));
                                if (jsonObjVenue.has("rating"))
                                    been.setRating(jsonObjVenue.getString("rating"));
                                //been.setStoreId(jsonObjVenue.getString("storeId"));

                                if (jsonObjVenue.has("location")) {
                                    JSONObject jsonObjLocation = jsonObjVenue.getJSONObject("location");
                                    been.setAddress(jsonObjLocation.getString("address"));
                                    been.setLatitude(jsonObjLocation.getDouble("latitude"));
                                    been.setLongitude(jsonObjLocation.getDouble("longitude"));
                                    been.setPostalcode(jsonObjLocation.getString("postalCode"));
                                    been.setCity(jsonObjLocation.getString("city"));
                                    been.setState(jsonObjLocation.getString("state"));
                                    been.setCountry(jsonObjLocation.getString("country"));
                                } else {
                                    break;
                                }

                                if (jsonObjVenue.has("location")) {
                                    JSONArray jsonArrayContacts = jsonObjVenue.getJSONArray("contacts");
                                    been.setContacts(jsonArrayContacts);
                                }
                                /*JSONObject jsonObjectContacts = jsonArrayContacts.getJSONObject(0);
                                been.setPhone(jsonObjectContacts.getString("phone"));
                                been.setTwitter(jsonObjectContacts.getString("twitter"));
                                been.setFacebook(jsonObjectContacts.getString("facebook"));
                                been.setFacebookName(jsonObjectContacts.getString("facebookName"));*/

                                JSONArray jsonArrayPhotos;
                                if (jsonObjVenue.has("photos")) {
                                    jsonArrayPhotos = jsonObjVenue.getJSONArray("photos");
                                    been.setPhotos(jsonArrayPhotos);
                                }

                                PreferencesManager.initializeInstance(MainActivity.this);
                                String id = PreferencesManager.getInstance().getFavedID();
                                if (!TextUtils.isEmpty(id) && !id.equals("null") && !TextUtils.isEmpty(jsonObjVenue.getString("id"))
                                        && !jsonObjVenue.getString("id").equals("null")
                                        && jsonObjVenue.getString("id").equals(id)) {
                                    mArrayLisVenues.add(0, been);
                                    been.setFavorite(true);
                                } else {
                                    mArrayLisVenues.add(been);
                                    been.setFavorite(false);
                                }

                            }
                            addDatatoAdapter(mArrayLisVenues);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "There was some error in parsing data", Toast.LENGTH_LONG).show();
                            mProgressbar.setVisibility(View.GONE);
                        }
                    }
                    // Display the first 500 characters of the response string.
                    // mTextView.setText("Response is: "+ response.substring(0,500));
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                Toast.makeText(getApplicationContext(),
                        "There was some error connecting with server, please try again", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        checkLocationEnabled();

    }

    private void addDatatoAdapter(final ArrayList<Venues> mArrayLisVenues) {
        if (mArrayLisVenues != null && mArrayLisVenues.size() > 0) {
            VenuesAdapter adapterObj =
                    new VenuesAdapter(mArrayLisVenues, R.layout.list_item_movie, MainActivity.this);
            recyclerView.setAdapter(adapterObj)
            ;
            adapterObj.setOnItemClickListener(new VenuesAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Venues been = mArrayLisVenues.get(position);
                    String title = been.getName();
                    String rating = been.getRating();
                    String totalreviews = been.getRatingSignals();
                    String phone = been.getPhone();
                    String address = been.getAddress() + "\n" + been.getCity() + "," + been.getState() + "," + "\n" +
                            been.getCountry() + ".\n" + been.getPostalcode();
                    String fb = been.getFacebookName();
                    boolean isverified = been.isVerified();
                    String twitter = been.getTwitter();
                    String contact = been.getContacts().toString();
                    System.out.println("v -------------" + been.getPhotos().toString());
                    String photos = been.getPhotos().toString();
                    String url = been.getUrl().toString();
                    Intent i = new Intent(MainActivity.this, VenueDetailsActivity.class);
                    i.putExtra("KEY_TITLE", title);
                    i.putExtra("KEY_RATING", rating);
                    i.putExtra("KEY_TOTAL_RATINGS", totalreviews);
                    i.putExtra("KEY_PHONE", phone);
                    i.putExtra("KEY_ADDRES", address);
                    i.putExtra("KEY_CONTACT", contact);
                    i.putExtra("KEY_FB", fb);
                    i.putExtra("KEY_TWITTER", twitter);
                    i.putExtra("KEY_PHOTOS", photos);
                    i.putExtra("KEY_URL", url);
                    i.putExtra("KEY_VERIFIED", isverified);

                    startActivity(i);
                }
            });
            mProgressbar.setVisibility(View.GONE);
        } else {
            Toast.makeText(getApplicationContext(),
                    "There are no DICK's sorting Goods location found", Toast.LENGTH_SHORT).show();
            mProgressbar.setVisibility(View.GONE);
        }
    }

    private void checkLocationEnabled() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_ASK_PERMISSIONS);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        // Make sure that GPS is enabled on the device
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            showDialogGPS();
        } else {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                        //Toast.makeText(getApplicationContext(), "fff  "+placeLikelihood.getPlace().getLatLng(), Toast.LENGTH_LONG).show();

                        //    Toast.makeText(getApplicationContext(), "fff  " + distance, Toast.LENGTH_LONG).show();
                        mCurrentLocation = placeLikelihood.getPlace().getLatLng();

                        /*(placeLikelihood.getPlace().getLatLng(),
                                placeLikelihood.getPlace().getName().toString(),
                                placeLikelihood.getPlace().getAddress().toString());*/

                        break;
                    }
                    likelyPlaces.release();
                }
            });
        }
    }

    /**
     * Show a dialog to the user requesting that GPS be enabled
     */
    private void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS to get current location");
        // builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
       /* Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Make sure that GPS is enabled on the device
            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            checkLocationEnabled();
        }
    }

    private void openSortDialog() {
        try {
            final Dialog mDialog = new Dialog(MainActivity.this);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 5.0f;
            mDialog.getWindow().setAttributes(lp);
            mDialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setContentView(R.layout.dialog_sort);
            mDialog.setCancelable(true);
            TextView tvSortFavs = (TextView) mDialog.findViewById(R.id.tv_favs);
            tvSortFavs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* ArrayList<Venues> listLocal = new ArrayList<>();
                    listLocal.addAll(mArrayLisVenues);
                    mArrayLisVenues = null;
                    mArrayLisVenues = new ArrayList<>();*/
                    PreferencesManager.initializeInstance(MainActivity.this);
                    String idPref = PreferencesManager.getInstance().getFavedID();
                    for (int i = 0; i < mArrayLisVenues.size(); i++) {
                    //    Venues been = new Venues();
                        String idFromList = mArrayLisVenues.get(i).getId();
                        if (!TextUtils.isEmpty(idPref) && !idPref.equals("null") && !TextUtils.isEmpty(idFromList)
                                && !idFromList.equals("null")
                                && idFromList.equals(idPref)) {
                            //mArrayLisVenues.add(0, been);

                            Collections.swap(mArrayLisVenues, 0, i);
                        } else {
                           // mArrayLisVenues.add(been);
                        }
                    }
                    addDatatoAdapter(mArrayLisVenues);
                    mDialog.dismiss();
                }
            });
            TextView tvSortDistance = (TextView) mDialog.findViewById(R.id.tv_distance);
            tvSortDistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    checkLocationEnabled();

                    for (int i = 0; i < mArrayLisVenues.size(); i++) {
                        double latitude = mArrayLisVenues.get(i).getLatitude();
                        double longitude = mArrayLisVenues.get(i).getLongitude();

                        float[] results = new float[1];
                        Location.distanceBetween(mCurrentLocation.latitude,
                                mCurrentLocation.longitude,
                                latitude, longitude, results);
                        float distance = results[0];
                        System.out.println("distance ---- "+distance);

                        mArrayLisVenues.get(i).setDistance(distance);
                    }
                    Collections.sort(mArrayLisVenues, new Comparator<Venues>() {
                        public int compare(Venues obj1, Venues obj2) {
                            // ## Ascending order
                            return obj1.getDistance().compareTo(obj2.getDistance()); // To compare string values
                            // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                            // ## Descending order
                            // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                            // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
                        }
                    });
                    addDatatoAdapter(mArrayLisVenues);
                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
