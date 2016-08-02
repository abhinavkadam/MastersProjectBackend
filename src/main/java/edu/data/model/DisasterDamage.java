package edu.data.model;

/**
 * @author abhinav kadam
 */
import java.io.Serializable;

/**
 * This is a model class for encapsulating the Disaster Damage Data.
 */
@SuppressWarnings("serial")
public class DisasterDamage implements Serializable {

	double magnitude;
	int BuildingDamages;
	int TransportationDamages;
	int UtilityDamages;

	public DisasterDamage(double magnitude, int BuildingDamages,
			int TransportationDamages, int UtilityDamages) {
		this.magnitude = magnitude;
		this.BuildingDamages = BuildingDamages;
		this.TransportationDamages = TransportationDamages;
		this.UtilityDamages = UtilityDamages;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}

	public int getBuildingDamages() {
		return BuildingDamages;
	}

	public void setBuildingDamages(int BuildingDamages) {
		this.BuildingDamages = BuildingDamages;
	}

	public int getTransportationDamages() {
		return TransportationDamages;
	}

	public void setTransportationDamages(int TransportationDamages) {
		this.TransportationDamages = TransportationDamages;
	}

	public int getUtilityDamages() {
		return UtilityDamages;
	}

	public void setUtilityDamages(int UtilityDamages) {
		this.UtilityDamages = UtilityDamages;
	}

}
