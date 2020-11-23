package deadwood.XML;

// Example Code for parsing XML file
// Dr. Moushumi Sharmin
// CSCI 345

import java.util.ArrayList;

import org.w3c.dom.Document;
//import deadwood.model.*;
import deadwood.model.areas.*;


public class XMLTest{

   public static void main(String args[]) throws Exception {
   
      //Document doc1 = null;
      Document doc2 = null;
      XMLParser parsing = new XMLParser();
      //ArrayList<SceneCard> scenes;
      ArrayList<Area> areas;

      //try{
      
         /* doc1 = parsing.getDocFromFile("./CSCI-345-Assignment-2/deadwood/XML/cards.xml");
         scenes = parsing.readSceneData(doc1); */

         /* scenes.stream()
            .forEach(s -> System.out.println(s.getSceneName())); */

         doc2 = parsing.getDocFromFile("./CSCI-345-Assignment-2/deadwood/XML/board.xml");
         areas = parsing.readAreaData(doc2);
         
         areas.stream()
            .forEach((Area a) -> {
               System.out.printf("%20s \t neighbors: ", a.getName());
               a.getNeighbors().forEach(b -> System.out.print(b.getName() + " "));
               System.out.println();
               if(a instanceof Set) {
                  System.out.print("                     \t ");
                  System.out.print("roles: ");
                  ((Set)a).getRoles().forEach(b -> System.out.print(b.getRoleName() + "   "));
                  System.out.println();
                  System.out.print("                     \t ");
                  System.out.println("shots: " + ((Set)a).getShotTokenCount());
               } else
               if(a instanceof CastingOffice) {
                  System.out.print("                     \t ");
                  System.out.println("upgrades: ");
                  for(int i = 2; i <= 6; i++) {
                     System.out.print("                     \t ");
                     System.out.printf("rank: %d    money: %d   credits: %d %n", 
                        i,
                        ((CastingOffice)a).getMoneyForRank(i),
                        ((CastingOffice)a).getCreditsForRank(i));
                  }
               }
            });
      
      /* }catch (Exception e){
      
         System.out.println("Error = "+e + "\n" + e.getStackTrace());
      
      } */
      
   
   }
}