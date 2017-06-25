package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private List<Season> listaStagioni;
	private SerieADAO dao;
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo;
	private Map<Team, Integer> classifica;
	private Map<String, Team> mappaSquadre;
	
	public Model(){
		dao = new SerieADAO();
		
		
	}

	public List<Season> getAllSeason() {
		
		if(listaStagioni==null){
			listaStagioni = dao.listSeasons();
		}
		return listaStagioni;
	}
	
	public void creaGrafo(Season s){
		grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Set<Team> squadre = new HashSet<>(dao.listTeamsOfSeason(s));
		System.out.println(squadre);
		//System.out.println(squadre.size());
		Graphs.addAllVertices(grafo, squadre);
		mappaSquadre = new HashMap<>();
		for(Team t : squadre){
			mappaSquadre.put(t.getTeam(), t);
		}
		
		List<Match> listaPartite = new ArrayList<>(dao.listMatches(s, mappaSquadre));
		for(Match partita : listaPartite){
			DefaultWeightedEdge arco = grafo.addEdge(partita.getHomeTeam(), partita.getAwayTeam());
			
			grafo.setEdgeWeight(arco, calcolaPunteggio(partita));
		}
//		System.out.println(grafo);
//		System.out.println(grafo.vertexSet().size());
//		System.out.println(grafo.edgeSet().size());
	
	
		
	}

	private double calcolaPunteggio(Match partita) {
		if(partita.getFthg()>partita.getFtag())
			return 1.0;
		else{
			if(partita.getFthg()<partita.getFtag())
				return -1.0;
			else{
				return 0.0;
			}
		}
	}

	public List<TeamAndPoints> generaClassifica(){
		classifica = new HashMap<Team, Integer>();
		for(Team t : grafo.vertexSet()){
			classifica.put(t, 0);
		}
		
		for(DefaultWeightedEdge arco : grafo.edgeSet()){
			if(grafo.getEdgeWeight(arco)>0.0){
				int punti1 = classifica.get(grafo.getEdgeSource(arco));
				classifica.replace(grafo.getEdgeSource(arco), punti1, punti1+3);
				
				
			}else{
				if(grafo.getEdgeWeight(arco)<0.0){
					
					int punti2 = classifica.get(grafo.getEdgeTarget(arco));
					classifica.replace(grafo.getEdgeTarget(arco), punti2, punti2+3);
					
				}else{
					int punti1 = classifica.get(grafo.getEdgeSource(arco));
					classifica.replace(grafo.getEdgeSource(arco), punti1, punti1+1);
					
					int punti2 = classifica.get(grafo.getEdgeTarget(arco));
					classifica.replace(grafo.getEdgeTarget(arco), punti2, punti2+1);
				}
			}
		}
		List<TeamAndPoints> listaFinale = new ArrayList<>();
		for(Team t : classifica.keySet()){
			listaFinale.add(new TeamAndPoints(t, classifica.get(t)));
		}
		Collections.sort(listaFinale);
		return listaFinale;
	}
	
	public List<DefaultWeightedEdge> calcolaSequenzaMax(){
	List<DefaultWeightedEdge> finale = new ArrayList<>();
	for(Team t : grafo.vertexSet()){
		System.out.println(t);
		List<DefaultWeightedEdge> parziale = new ArrayList<>();
		int step = 0;
		recursive(parziale,t,step, finale);
	}
	return finale;
		
	}

	private void recursive(List<DefaultWeightedEdge> parziale, Team t, int step, List<DefaultWeightedEdge> finale) {
		
		//condizione di terminazione
		System.out.println(parziale.size());
			if(parziale.size()>finale.size()){
				finale.clear();
				finale.addAll(parziale);
				//System.out.println(finale.size());
				//System.out.println(finale.size()+" "+finale);
			}
		
		//ricorsione
			for(DefaultWeightedEdge arco : grafo.outgoingEdgesOf(t)){
					
					if(grafo.getEdgeWeight(arco)==1.0 && !parziale.contains(arco)){
						//System.out.println(arco);
						parziale.add(arco);
						Team vicino = grafo.getEdgeTarget(arco);
						recursive(parziale, vicino, step+1,finale);
						
						parziale.remove(arco);
					}
				
			}
				
				
	}
	
}
