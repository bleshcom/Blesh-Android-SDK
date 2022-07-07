package com.blesh.demo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blesh.demo.databinding.ActivityMainBinding;
import com.blesh.sdk.BleshSdk;
import com.blesh.sdk.core.models.ApplicationUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private ActivityMainBinding binding;

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    public static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 999;

    LocationManager locationManager;
    String provider;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private LocationManager getLocationManager() {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        return locationManager;
    }

    private String getProvider() {
        if (provider == null) {
            provider = getLocationManager().getBestProvider(new Criteria(), true);
        }

        return provider;
    }

    public void requestLocationPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setTitle("Blesh Demo")
                    .setMessage("This application requires location updates")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_PERMISSION_REQUEST_CODE);
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestBluetoothPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setTitle("Blesh Demo")
                    .setMessage("This application requires bluetooth scanning")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[] { Manifest.permission.BLUETOOTH_SCAN }, BLUETOOTH_PERMISSION_REQUEST_CODE);
                        } else {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, BLUETOOTH_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .create()
                    .show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[] { Manifest.permission.BLUETOOTH_SCAN }, BLUETOOTH_PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, BLUETOOTH_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        debugLog("Got location: " + location.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugLog("Resuming");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocationManager().requestLocationUpdates(getProvider(), 1000, 10, this);
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        debugLog("Pausing");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocationManager().removeUpdates(this);
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        BleshSdk.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) { // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                binding.buttonRequestLocation.setEnabled(false);
                binding.buttonLocation.setEnabled(true);
                binding.toggleLocationUpdates.setEnabled(true);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    debugLog("Location permission: ACCESS_FINE_LOCATION");
                } else {
                    debugLog("Location permission: ACCESS_COARSE_LOCATION");
                }
            } else {
                binding.buttonRequestLocation.setEnabled(true);
                binding.buttonLocation.setEnabled(false);
                binding.toggleLocationUpdates.setEnabled(false);

                debugLog("Location permission: Denied");
            }
        } else if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) { // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                binding.buttonRequestBt.setEnabled(false);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        debugLog("Bluetooth permission: ACCESS_FINE_LOCATION");
                    }
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                    debugLog("Bluetooth permission: BLUETOOTH_SCAN");
                }
            } else {
                binding.buttonRequestBt.setEnabled(true);

                debugLog("Bluetooth permission: Denied");
            }
        }
    }

    private void debugLog(CharSequence text) {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                String currentTime = new SimpleDateFormat("[HH:mm:ss] ", Locale.getDefault()).format(new Date());

                SpannableString boldTime = new SpannableString(currentTime);
                boldTime.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTime.length(), 0);

                binding.textViewDebug.append(boldTime);
                binding.textViewDebug.append(text);
                binding.textViewDebug.append("\n");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textViewDebug.setMovementMethod(new ScrollingMovementMethod());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    debugLog("Location couldn't be obtained");
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    onLocationChanged(location);
                }
            }
        };

        BleshSdk.setOnCampaignNotificationReceived(campaignId -> {
            debugLog("Received a campaign with id " + campaignId + " from Blesh");
            return true;
        });

        BleshSdk.setOnCampaignDisplayed((campaignId, contentId, notificationId) -> {
            debugLog("Displayed a campaign with id " + campaignId + " from Blesh");
        });

        BleshSdk.setOnSdkStartCompleted(sdkStartState -> {
            debugLog("Blesh SDK State: " + sdkStartState);
        });

        if (BleshSdk.getAdsEnabled()) {
            debugLog("BleshSDK: Ads enabled");
        } else {
            debugLog("BleshSDK: Ads disabled");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            debugLog("Location permission: ACCESS_FINE_LOCATION");
            binding.buttonRequestLocation.setEnabled(false);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                debugLog("Bluetooth permission: ACCESS_FINE_LOCATION");
                binding.buttonRequestBt.setEnabled(false);
            }
        } else {
            debugLog("Location permission: None");
            binding.buttonLocation.setEnabled(false);
            binding.toggleLocationUpdates.setEnabled(false);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            debugLog("Bluetooth permission: BLUETOOTH_SCAN");
            binding.buttonRequestBt.setEnabled(false);
        } else {
            debugLog("Bluetooth permission: None");
        }

        binding.buttonRequestLocation.setOnClickListener(senderView -> {
            debugLog("Requesting location permission");
            requestLocationPermission();
        });

        binding.buttonRequestBt.setOnClickListener(senderView -> {
            debugLog("Requesting bluetooth scan permission");
            requestBluetoothPermission();
        });

        binding.buttonLocation.setOnClickListener(senderView -> {
            debugLog("Requesting current location");
            getCurrentLocation();
        });

        binding.toggleLocationUpdates.setOnClickListener(senderView -> {
            debugLog("Toggling location updates");
            startLocationUpdates();
        });

        binding.buttonDisplayAd.setOnClickListener(senderView -> {
            debugLog("Starting Blesh SDK and requesting a mock ad (test mode only)");
            startBleshSdk(true);
        });

        binding.buttonStartSdk.setOnClickListener(senderView -> {
            debugLog("Starting Blesh SDK");
            startBleshSdk(false);
        });

        binding.buttonRestartSdk.setOnClickListener(senderView -> {
            debugLog("Restarting Blesh SDK");
            BleshSdk.restart();
        });

        binding.buttonStopSdk.setOnClickListener(senderView -> {
            debugLog("Stopping Blesh SDK");
            BleshSdk.stop();
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null);
    }

    private void startLocationUpdates() {
        if (!binding.toggleLocationUpdates.isChecked()) {
            debugLog("Stopping location updates");
            fusedLocationClient.removeLocationUpdates(locationCallback);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        debugLog("Starting location updates");
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void startBleshSdk(Boolean requestAd) {
        Map<String, String> other = new HashMap<>();

        // Enable below to force SDK start
        // other.put("Random", Double.valueOf(Math.random()*((99999999-19999999)+1)).toString());

        if (requestAd) {
            other.put("DisplayMockAd", "true");
        }

        ApplicationUser user = new ApplicationUser
                .Builder()
                .userId("42")
                .other(other)
                .build();

        BleshSdk.start(user);
    }
}