package test.jwf.network;

import test.jwf.network.data.GsmCellInfo;
import test.jwf.network.provider.CellInfoProvider;
import test.jwf.network.provider.DatabaseStorageProvider;
import test.jwf.network.provider.GpsPosProvider;
import test.jwf.network.provider.GpsPosProvider.GpsStatus;
import test.jwf.network.provider.OnGpsStateChangedListener;
import test.jwf.network.provider.OnSignalStrengthChangedListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.telephony.*;
import android.telephony.gsm.GsmCellLocation;
import android.database.sqlite.*;

public class GSMNetworkTrackerActivity extends Activity implements
		OnSignalStrengthChangedListener, OnGpsStateChangedListener {
	/** Called when the activity is first created. */
	private TelephonyManager tm;
	private GsmCellLocation location;
	private int cid, lac, mcc, mnc, cellPadding;

	private TextView textView;
	private TextView lat;
	private TextView longi;

	private CellInfoProvider cellInfo;
	private LocationManager lm;
	private GpsPosProvider posProvider;
	
	private DatabaseStorageProvider stoProvider;
	
	private Boolean gpsConnection = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		cellInfo = new CellInfoProvider(
				(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));

		cellInfo.onCreate(savedInstanceState);
		cellInfo.setOnSignalStrengthChanged(this);

		
		posProvider = new GpsPosProvider(this);
		posProvider.addOnGpsStateChangedListener(this);

		textView = (TextView) findViewById(R.id.Cinr);
		longi = (TextView) findViewById(R.id.longitude);
		lat = (TextView) findViewById(R.id.latitude);
		
		stoProvider =  new DatabaseStorageProvider(getApplicationContext());
		

	}

	@Override
	public void onSignalStrengthChanged(GsmCellInfo cellInfo) {
		Location loc = posProvider.getCurrentLocation();
		String cellType;
			switch (posProvider.getCurrentGpsStatus()) {
			case available:

				lat.setText("Latitude: " + loc.getLatitude());
				longi.setText("Longitude: " + loc.getLongitude());

				break;
			case temporaryUnavailable:
				lat.setText("Latitude: GPS temporary unavailable") ;
				longi.setText("Longitude: GPS temporary Unavailable");
				break;
			case outOfService:
				lat.setText("OutOfService") ;
				longi.setText("OutOfService");
				break;
			}


		textView.setText("CINR: " + cellInfo.getCinr());
		
		if (cellInfo.getCellType() == GsmCellInfo.CellType.GSM)
			 cellType = "GSM";
		else
			cellType = "UMTS";
		if (gpsConnection == true)
		stoProvider.insert(cellInfo.getCid(), cellType, cellInfo.getLac(), cellInfo.getCinr(), Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude()), Float.toString(loc.getAccuracy()));


	}

	@Override
	public void onGpsStateChanged(GpsStatus status) {
		
		//Changing the GPS state indication
		switch (status) {
		case available:
			lat.setBackgroundColor(Color.GREEN);
			longi.setBackgroundColor(Color.GREEN);
			gpsConnection = true;
			break;
		case temporaryUnavailable:
			lat.setBackgroundColor(Color.RED);
			longi.setBackgroundColor(Color.RED);
			gpsConnection = false;
			break;
		case outOfService:
			gpsConnection = false;
			lat.setBackgroundColor(Color.RED);
			longi.setBackgroundColor(Color.RED);
			break;
		}
		
	}

}
