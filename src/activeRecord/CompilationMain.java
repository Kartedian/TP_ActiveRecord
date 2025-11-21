package activeRecord;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * cette classe a juste pour objectif de verifier les noms des methodes
 */
public class CompilationMain {

	public static void main(String[] args) throws SQLException, RealisateurAbsentException {
        ///////////////// test personne
		
		
		//creation de la table Personne
		Personne.createTable();
				
		
		//constructeur
		Personne p;
		p=new Personne("spielberg", "steven");
		p.save();
		p=new Personne("scott", "ridley");
		p.save();
		p=new Personne("scott", "ridley");
		System.out.println("**** table cree et tuples ajoutes ***");
		
		
		//recherche
		System.out.println("**** tuples present - findall ****");
		ArrayList<Personne> liste=Personne.findAll();
		for (Personne plits:liste)
			System.out.println(plits);
		
		//ajout
		System.out.println("**** ajout de David fincher - save ****");
		p=new Personne("fincher", "david");
		p.save();
		
		//recuperation
		System.out.println("**** recuperation steven Spielberg - findbyid ****");
		Personne temp=Personne.findById(1);
		System.out.println(temp);
		
		//suppression
		System.out.println("**** suppression steven spielberg - delete ****");
		temp.delete();
		ArrayList<Personne> liste2=Personne.findAll();
		for (Personne plits:liste2)
			System.out.println(plits);
		
	
		//recherche fincher
		System.out.println("**** recherche fincher - findbyname ****");
		temp.delete();
		ArrayList<Personne> liste3=Personne.findByName("fincher");
		for (Personne plits:liste3)
			System.out.println(plits);
		
		
		//modification
		Personne p2=liste3.get(0);
		p2.setNom("f_i_n_c_h_e_r");
		p2.setPrenom("davif");
		p2.save();
		System.out.println("**** test modification  - save *** ");
		ArrayList<Personne> liste4=Personne.findAll();
		for (Personne plits:liste4)
			System.out.println(plits);
		
		//getter
		p2.getId();
		p2.getNom();
		p2.getPrenom();

				
	
		
		///////////////// test film
		
		//create table
		Film.createTable();
		
		Film f=new Film("seven", p2);
		//sauvegarde
		f.save();
		
		//finByID
		System.out.println("**** film finbyid **** ");
		Film f2=Film.findById(1);
		System.out.println(f2);
		System.out.println(f2.getId());
		System.out.println(f2.getTitre());
		Personne p3=f2.getRealisateur();
		System.out.println(p3);
		
		//setter 
		f2.setTitre("test2");
		f2.save();
		
		//sauve film modifie
		System.out.println("**** film modifie **** ");
		Film f3=Film.findById(1);
		System.out.println(f3);
		System.out.println(f3.getId());
		System.out.println(f3.getTitre());
		Personne p5=f3.getRealisateur();
		System.out.println(p5);
		
		
		//supprime table film
		Film.deleteTable();
		//suppression de la table personne
		Personne.deleteTable();
		

		
	}
	
}
