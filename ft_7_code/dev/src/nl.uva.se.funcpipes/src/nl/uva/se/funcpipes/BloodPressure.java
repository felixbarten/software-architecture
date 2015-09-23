package nl.uva.se.funcpipes;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;

public class BloodPressure {

	private int bloodPressure;
	private enum State { DANGEROUS, CAUTION, OK, LOWBP, UNKNOWN, HIGHBP }
	private State state;
	
	public BloodPressure(int bloodPressure) {
		super();
		this.bloodPressure = bloodPressure;
		setBPState(bloodPressure);
	}

	public BloodPressure(int bloodPressure, State state) {
		super();
		this.bloodPressure = bloodPressure;
		this.state = state;
	}
	
	public int getBloodPressure() {
		return bloodPressure;
	}
	
	public void setBloodPressure(int bloodPressure) {
		// check if pressure is between reasonable bound, otherwise discard
		if (bloodPressure > 50 && bloodPressure < 200) {
			this.bloodPressure = bloodPressure;
			setBPState(bloodPressure);
		}
	}
	
	// sets state based on bloodpressure value
	private void setBPState(int pressure) {
		if (pressure > 50 && pressure <= 70){
			setState(State.LOWBP);
		} else if (pressure > 70 && pressure <= 90){
			setState(State.CAUTION);
		} else if ( pressure >90 && pressure <= 110){
			setState(State.OK);
		} else if (pressure > 110 && pressure <=130){
			setState(State.HIGHBP);
		} else if (pressure > 130 && pressure <= 150) {
			setState(State.CAUTION);
		} else if (pressure >150){
			setState(State.DANGEROUS);
		} else {
			setState(State.UNKNOWN);
		}
	}

	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	
	public void readDiagnosis() {
		String diagnosis = "Dear patient, Your bloodpressure is: " + bloodPressure + ". ";
		State state = getState();
		// although case statements are not the greatest solution they are efficient
		String stateDiagnosis = "";
		switch (state) {
			case DANGEROUS: 
				stateDiagnosis = "We have established your blood pressure is dangerously high and you should visit a medical professional as soon as possible.";
				break;
			case CAUTION:
				stateDiagnosis =" We have established your blood pressure is not an immediate health danger but caution is advised";
				break;
			case OK:
				stateDiagnosis =" We have established your blood pressure is not an immediate health danger but caution is advised";
				break;
			case LOWBP:
				stateDiagnosis =" We have established your blood pressure is very low and you should visit a medical professional.";
				break;
			case HIGHBP:
				stateDiagnosis =" We have established your blood pressure is not an immediate health danger but caution is advised";
				break;
			case UNKNOWN:
				stateDiagnosis ="We were unable to diagnose your blood pressure, please visit a medical professional.";
				break;
			default: 
				stateDiagnosis =" We were unable to diagnose your blood pressure, please visit a medical professional.";
				break;
		}
		diagnosis = diagnosis.concat(stateDiagnosis);
		
		
		System.out.println();
		System.out.println("-------------------------------------");
		System.out.println(diagnosis);
		System.out.println("-------------------------------------");
	}

	
}
