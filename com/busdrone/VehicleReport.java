package com.busdrone;

import java.util.Date;

public class VehicleReport implements Cloneable {
	public String uid;
	public String dataProvider;
	public String operator;
	public String vehicleType;
	public String vehicleId;
	public String prevStop;
	public String nextStop;
	public String coach;
	public String name;
	public String routeId;
	public String route;
	public String tripId;
	public String destination;
	public String color;
	public int speedMph;
	public int speedKmh;
	public double lat;
	public double lon;
	public double heading;
	public boolean inService = true; // XXX
	public long timestamp = java.lang.Long.MIN_VALUE;
	public long initialStaleness = java.lang.Long.MIN_VALUE;
	
	public void cleanup() {
		if (vehicleId == null) vehicleId = coach;
		if (coach == null) coach = vehicleId;
		if (uid == null) uid = dataProvider+'/'+vehicleId;
		if (speedMph == 0 && speedKmh != 0) speedMph = (int) (0.621371 * speedKmh);
		if (speedKmh == 0 && speedMph != 0) speedKmh = (int) (1.609344 * speedMph);
		//if (timestamp = java.lang.Long.MIN_VALUE) 
	}

	public Event toEvent() {
		cleanup();
		Event event = new Event("update_vehicle");
		event.vehicle = this;
		return event;
	}
	
	public String toEventJson() {
		return toEvent().toJson();
	}
	
	public boolean isDeletable() {
		//return Math.abs(lat) > 89 || Math.abs(lon) > 89 || !inService || age >= 1000*60*10;
		
		long currentAge = (new Date()).getTime() - timestamp;
		
		boolean retval = Math.abs(lat) > 89 || Math.abs(lon) > 179 || !inService || currentAge >= 1000*60*10;
		
		/*if (retval) {
			System.out.println("["+uid+"] "+
					" Math.abs(lat) > 89:"+(Math.abs(lat) > 89)+
					" Math.abs(lon) > 179:"+(Math.abs(lon) > 179) +
					" !inService:"+!inService+
					" age >= 1000*60*10:"+(currentAge >= 1000*60*10));
		}*/
		
		return retval;
	}
	
	@Override public boolean equals(Object aThat) {		
		if ( this == aThat ) return true;
		if ( !(aThat instanceof VehicleReport) ) return false;
		VehicleReport that = (VehicleReport)aThat;
		
		this.cleanup(); that.cleanup();
		
		boolean retVal = (
			this.vehicleId.equals(that.vehicleId) &&
			this.coach.equals(that.coach) &&
			this.route.equals(that.route) &&
			((this.tripId == null && that.tripId == null) || this.tripId.equals(that.tripId)) &&
			(this.destination == null ? "" : this.destination).equals(that.destination == null ? "" : that.destination) &&
			(Math.abs(this.lat-that.lat) < 0.00001) &&
			(Math.abs(this.lon-that.lon) < 0.00001));

		/*if (!retVal && (Math.abs(this.lat-that.lat) < 0.00001) && (Math.abs(this.lon-that.lon) < 0.00001)) {
			System.out.println("["+retVal+"] equals? "+this.uid+":["+this.lat+","+this.lon+"] "+that.uid+": ["+that.lat+","+that.lon+"]");
			System.out.println("this.vehicleId == that.vehicleId            " +  this.vehicleId + " == " + that.vehicleId + ":" +    this.vehicleId.equals(that.vehicleId   )       + "\n" +
							"this.coach     == that.coach                " +  this.coach     + " == " + that.coach     + ":" +    this.coach.equals(that.coach        )       + "\n" +
							"this.routeId   == that.routeId              " +  this.routeId   + " == " + that.routeId   + ":" +    this.routeId.equals(that.routeId      )       + "\n" +
							"this.route     == that.route                " +  this.route     + " == " + that.route     + ":" +    this.route.equals(that.route        )       + "\n" +
							"this.tripId    == that.tripId               " +  this.tripId    + " == " + that.tripId    + ":" +    this.tripId.equals(that.tripId       )       + "\n" +
							"this.destination == that.destination        " +  this.destination + " == " + that.destination + ":" + this.destination.equals(that.destination)       + "\n" +
							"(Math.abs(this.lat-that.lat) < 0.00001)     " +  Math.abs(this.lat-that.lat) + ":"+  (Math.abs(this.lat-that.lat) < 0.00001)      + "\n" +
							"(Math.abs(this.lon-that.lon) < 0.00001)     " +  Math.abs(this.lon-that.lon) + ":"+  (Math.abs(this.lon-that.lon) < 0.00001)      + "\n" +
							"(Math.abs(this.heading-that.heading) < 1)); " +  Math.abs(this.heading-that.heading) + ":"+ (Math.abs(this.heading-that.heading) < 1)					);
			System.out.println(this.toEventJson());
			System.out.println(that.toEventJson());
		} */
		return retVal;
	}
	
	public Object clone() {
		try {
			return super.clone();
		}
		catch(Exception e) {
			return null;
		}
	}
	
}
