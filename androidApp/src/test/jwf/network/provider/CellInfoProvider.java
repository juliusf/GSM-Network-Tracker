package test.jwf.network.provider;

import java.util.ArrayList;

import test.jwf.network.data.GsmCellInfo;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class CellInfoProvider {

	private TelephonyManager tm;
	private GsmCellLocation location;
	private int cid, lac, mcc, mnc, cellPadding, gsmCinr;
	private SignalStrength signalStrength;
	private GsmCellInfo.CellType celltype;
	private ArrayList<OnSignalStrengthChangedListener> sinalStrengthChangedListener = new ArrayList<OnSignalStrengthChangedListener>();

	private PhoneStateListener listener;

	public CellInfoProvider(TelephonyManager systemService) {
		tm = systemService;
	}

	public void onCreate(Bundle savedInstanceState) {

		listener = new PhoneStateListener() {
			@Override
			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				super.onSignalStrengthsChanged(signalStrength);
				CellInfoProvider.this.signalStrength = signalStrength;

				updateDataset();

			}

		};

		tm.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
		
	}

	public void updateDataset() {
		location = (GsmCellLocation) tm.getCellLocation();
		cid = location.getCid();
		lac = location.getLac();

		String networkOperator = tm.getNetworkOperator();
		if (networkOperator != null && networkOperator.length() > 0) {
			try {
				mcc = Integer.parseInt(networkOperator.substring(0, 3));
				mnc = Integer.parseInt(networkOperator.substring(3));
			} catch (NumberFormatException e) {
			}
		}

		/*
		 * Check if the current cell is a UMTS (3G) cell. If a 3G cell the cell
		 * id padding will be 8 numbers, if not 4 numbers.
		 */
		if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
			cellPadding = 8;
			celltype = GsmCellInfo.CellType.UMTS;
		} else {
			cellPadding = 4;
			celltype = GsmCellInfo.CellType.GSM;
		}

		gsmCinr = signalStrength.getGsmSignalStrength();

		fireOnSignalStrengthChangedEvent();
	}

	public void setOnSignalStrengthChanged(
			OnSignalStrengthChangedListener listener) {
		sinalStrengthChangedListener.add(listener);
	}

	private void fireOnSignalStrengthChangedEvent() {
		GsmCellInfo data = new GsmCellInfo(getPaddedHex(cid, cellPadding),
				getPaddedHex(lac, 4), getPaddedInt(mcc, 3),
				getPaddedInt(mnc, 2), Integer.toString(gsmCinr), celltype);
		
		for (OnSignalStrengthChangedListener h : sinalStrengthChangedListener) {
			h.onSignalStrengthChanged(data);
		}
	}

	private String getPaddedHex(int nr, int minLen) {
		String str = Integer.toHexString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * Convert an int to String and pad with 0's up to minLen.
	 */

	private String getPaddedInt(int nr, int minLen) {
		String str = Integer.toString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}
}
