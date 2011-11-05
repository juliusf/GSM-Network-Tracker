package test.jwf.network.data;

public class GsmCellInfo {
	
	public enum CellType{UMTS, GSM}
	private CellType cellType;
	private String cid, lac, mcc, mnc, gsmCinr;
	
	
	
	public GsmCellInfo(String cid, String lac, String mcc, String mnc, String cinr, CellType type){
		this.cid = cid;
		this.lac = lac;
		this.mcc = mcc;
		this.mnc = mnc;
		this.gsmCinr = cinr;
		this.cellType = type;
	}
	/**
	 * gets the CellID.
	 */
	public String getCid() {
		return cid;
	}
	/**
	 * 
	 * @return gets the celltype. This could either be UMTS or GSM
	 */
	public CellType getCellType(){
		return cellType;
	}
	/**
	 * gets the mobile network code. Unique code for each provider in each country
	 */
	public String getMcc(){
		return mcc;
	}
	
	/**
	 * 
	 * @return
	 * get the location area. several cells are combined to one location area
	 */
	public String getLac(){
		return lac;
	}
	
	/**
	 * 
	 * @return gets the Carrier to Interference-plus-Noise Ratio, as defined in TS 27.007 8.5. 
	 * valid values are  (0-31, 99)
	 */
	public String getCinr(){
		return gsmCinr;
	}
	
	public String getMnc(){
		return mnc;
	}

	
	/**
	 * Convert an int to an hex String and pad with 0's up to minLen.
	 */
	
}
