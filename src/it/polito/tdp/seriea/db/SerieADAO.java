package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public Set<Team> listTeamsOfSeason(Season s) {
		String sql = "select HomeTeam "+
                     "from matches "+
                     "where Season=? " ;
		
		Set<Team> result = new HashSet<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("HomeTeam"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Match> listMatches(Season s, Map<String, Team> mappaSquadre) {
		String sql = "select match_id, Season, `Div`, Date, HomeTeam, AwayTeam, FTHG, FTAG,FTR "+
                     "from matches "+
                     "where Season=? " ;
		
		List<Match> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team teamCasa = null;
				Team teamOspite =null;
				if(mappaSquadre.containsKey(res.getString("HomeTeam"))){
					teamCasa = mappaSquadre.get(res.getString("HomeTeam"));
				}else{
					teamCasa = new Team(res.getString("HomeTeam"));
				}
				if(mappaSquadre.containsKey(res.getString("AwayTeam")))
					teamOspite = mappaSquadre.get(res.getString("AwayTeam"));
				else
					teamOspite = new Team(res.getString("AwayTeam"));
				result.add( new Match(res.getInt("match_id"),
										s,
										res.getString("Div"),
										res.getDate("Date").toLocalDate(),
										teamCasa,
										teamOspite,
										res.getInt("FTHG"),
										res.getInt("FTAG"),
										res.getString("FTR"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	


}
