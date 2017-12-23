package telemetry;

import java.text.DecimalFormat;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import common.Config;
import common.FoxSpacecraft;
import common.Log;
import common.Spacecraft;
import decoder.FoxDecoder;
import predict.PositionCalcException;
import uk.me.g4dpz.satellite.GroundStationPosition;
import uk.me.g4dpz.satellite.SatPos;
import uk.me.g4dpz.satellite.Satellite;
import uk.me.g4dpz.satellite.SatelliteFactory;
import uk.me.g4dpz.satellite.TLE;

public class PayloadWOD extends PayloadRtValues {
	public static final String WOD_RESETS = "WODTimestampReset";
	public static final String WOD_UPTIME = "WODTimestampUptime";
	
	
	public PayloadWOD(BitArrayLayout lay) {
		super(lay);
	}
	
	public PayloadWOD(int id, int resets, long uptime, String date, StringTokenizer st, BitArrayLayout lay) {
		super(id, resets, uptime, date, st, lay);	

	}

	@Override
	protected void init() {
		type = TYPE_WOD;
		fieldValue = new int[layout.NUMBER_OF_FIELDS];
	}

	@Override
	public void captureHeaderInfo(int id, long uptime, int resets) {
		copyBitsToFields();
		this.id = id;
		this.captureDate = fileDateStamp();	
	}
	
	
	@Override
	public void copyBitsToFields() {
		super.copyBitsToFields();
		resets = getRawValue(WOD_RESETS);
		uptime = getRawValue(WOD_UPTIME);
	}
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		copyBitsToFields();
		String s = new String();
		s = s + "WOD Telemetry:\n";
		for (int i=0; i < layout.fieldName.length; i++) {
			s = s + layout.fieldName[i] + ": " + fieldValue[i] + ",   ";
			if ((i+1)%6 == 0) s = s + "\n";
		}
		return s;
	}
	
	/**
	 * Output the set of fields in this framePart as a set of comma separated values in a string.  This 
	 * can then be written to a file
	 * @return
	 */
	public String toFile() {
		copyBitsToFields();
		String s = new String();
		s = s + captureDate + "," + id + "," + resets + "," + uptime + "," +  type + "," + satLatitude + "," + satLongitude + "," + satAltitude + "," ;
		for (int i=0; i < layout.fieldName.length-1; i++) {
			s = s + FoxDecoder.dec(getRawValue(layout.fieldName[i])) + ",";
		}
		s = s + FoxDecoder.dec(getRawValue(layout.fieldName[layout.fieldName.length-1]));
		return s;
	}
	
	public String getInsertStmt() {
		copyBitsToFields();
		String s = new String();
		s = s + " (captureDate,  id, resets, uptime, type, \n";
		for (int i=0; i < layout.fieldName.length-1; i++) {
			s = s + layout.fieldName[i] + ",\n";
		}
		s = s + layout.fieldName[layout.fieldName.length-1] + ")\n";
		s = s + "values ('" + this.captureDate + "', " + this.id + ", " + this.resets + ", " + this.uptime + ", " + this.type + ",\n";
		for (int i=0; i < fieldValue.length-1; i++) {
			s = s + fieldValue[i] + ",\n";
		}
		s = s + fieldValue[fieldValue.length-1] + ")\n";
		return s;
	}
	
}
