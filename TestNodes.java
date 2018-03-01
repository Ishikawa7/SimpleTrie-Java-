/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testnodes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davide
 */
public class TestNodes 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            FileReader dizionario = new FileReader("dizionario1.txt");
            FileReader file1 = new FileReader("testo1.txt");
            Node node=new Node('0',0); //starting node
            node.train(dizionario); //train 
            node.search(file1);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(TestNodes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    
}
