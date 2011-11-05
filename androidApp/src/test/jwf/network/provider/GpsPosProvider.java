package test.jwf.network.provider;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class GpsPosProvider implements LocationListener {

	LocationManager lm;
	Location currentLoc;

	public enum GpsStatus {
		available, temporaryUnavailable, outOfService
	}

	private GpsStatus gpsStatus = GpsStatus.temporaryUnavailable;

	private boolean isEnabled;
	private Activity systemService = null;
	private ArrayList<OnGpsStateChangedListener> gpsChangedListener = new ArrayList<OnGpsStateChangedListener>();

	public GpsPosProvider(Activity sservice) {
		systemService = sservice;
		startLocationService();

	}

	private void startLocationService() {
		this.lm = (LocationManager) systemService
				.getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);

		currentLoc = lm.getLastKnownLocation("gps");

	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public Location getCurrentLocation() {
		return currentLoc;
	}

	public GpsStatus getCurrentGpsStatus() {
		return gpsStatus;
	}

	@Override
	public synchronized void onLocationChanged(Location location) {
		currentLoc = location;

	}

	@Override
	public void onProviderDisabled(String provider) {
		isEnabled = false;

	}

	@Override
	public void onProviderEnabled(String provider) {
		isEnabled = true;

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case 0:
			gpsStatus = GpsStatus.outOfService;
			isEnabled = true;
			break;
		case 1:
			gpsStatus = GpsStatus.temporaryUnavailable;
			isEnabled = false;
			break;
		case 2:
			gpsStatus = GpsStatus.available;
			isEnabled = true;
			break;

		}
		fireGpsStateChangedEvent();

	}

	public void addOnGpsStateChangedListener(OnGpsStateChangedListener listener) {
		gpsChangedListener.add(listener);
	}

	private void fireGpsStateChangedEvent() {
		for(OnGpsStateChangedListener h: gpsChangedListener){
			h.onGpsStateChanged(gpsStatus);
		}
	}
}
