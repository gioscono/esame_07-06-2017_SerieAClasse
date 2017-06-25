package it.polito.tdp.seriea.model;

public class TeamAndPoints implements Comparable<TeamAndPoints>{
	
	private Team squadra;
	private int punti;
	
	public TeamAndPoints(Team squadra, int punti) {
		super();
		this.squadra = squadra;
		this.punti = punti;
	}

	public Team getSquadra() {
		return squadra;
	}

	public int getPunti() {
		return punti;
	}
	
	public void vittoria(){
		this.punti+=3;
	}
	
	public void sconfitta(){
		this.punti -= 3;
	}

	@Override
	public int compareTo(TeamAndPoints altra) {
		
		return -(this.punti-altra.punti);
	}

}
