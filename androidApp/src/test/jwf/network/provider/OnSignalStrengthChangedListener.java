package test.jwf.network.provider;

import test.jwf.network.data.GsmCellInfo;

/*Listener that is called when the signal strength changes.
*
*/
public interface OnSignalStrengthChangedListener {

	void onSignalStrengthChanged(GsmCellInfo cellInfo);
}
