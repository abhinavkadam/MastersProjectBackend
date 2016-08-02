package edu.data.model;
/**
 * @author abhinav kadam
 */
import java.io.Serializable;

/**
 * This is a model class that encapsulates the Resource Need data.
 */
@SuppressWarnings("serial")
public class ResourceNeed implements Serializable{
	
	public int food;
	public int medical;
	public int shelter;

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getMedical() {
		return medical;
	}

	public void setMedical(int medical) {
		this.medical = medical;
	}

	public int getShelter() {
		return shelter;
	}

	public void setShelter(int shelter) {
		this.shelter = shelter;
	}

	public ResourceNeed(int food,int medical,int shelter){
		this.food = food;
		this.medical = medical;
		this.shelter = shelter;
	}

}
