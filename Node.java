/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testnodes;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Davide
 */
public class Node
{
    static int nodeId=-1;
    final int id; //Node id
    final int level; //Node level
    boolean atWordEnd=false;
    char value;
    ArrayList<Node> childNodes;
    public Node(char value,int level)
    {
        childNodes = new ArrayList<>(0);
        this.value=value;
        id=nodeId++;
        this.level=level;
    }
    private Node searchNext(char nodeValue)
    {
        if(childNodes==null) {return null;} //if no childnodes present return null
        for(Node n : childNodes)
        {
            if(nodeValue==n.value)
            {
                return n;
            }
        }
        return null; //if no childnode found return null
    }
    public int train(Reader stream)
    {
        int input;
        int returnValue;
        char nodeValue;
        Node n;
        System.out.println(this.toString());
        try //note: the dictionary should not start with a space(word's separator) and must end with a space!!!
        {
            input=stream.read();  System.out.println("Input="+input);
            
            if(input==-1) return -1;  //END OF FILE (return -1)
            if(input==32) {atWordEnd=true;return 0;}// END OF PATH (space as words separator) (return 0)
            
            nodeValue=(char)input;    System.out.println("read char: "+nodeValue);
            
            if((n=this.searchNext(nodeValue))==null)    //if value not found in any child create a new node with that value
            {
                n = new Node(nodeValue,level+1);    System.out.println("Node to add: id["+n.id+"] value["+n.value+"] level["+n.level+"]");
                childNodes.add(n);    System.out.print("ADDED-"); 
                returnValue=n.train(stream);    //continue path from new node
            } 
            else 
            {   System.out.print("EXISTING-");//if node with value exist continue from existing node
                returnValue=n.train(stream);//continue path from existing node
            }
            if(returnValue==-1) return -1; //trasmit EOF back in calls chain then the root node exit (END OF TRAINING)
            if(level==0)this.train(stream); //restart from root
            if(returnValue==0) return 0; //trasmit END OF PATH back to root 
            if(returnValue==-1) return -1; //exit point after first path
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Exit training because end of input");
        return 1;
    }
    @Override
    public String toString()
    {
        return " ---id["+id+"] value["+value+"] level["+level+"]---";
    }

    public int search(Reader stream)
    {   
        PushbackReader workInput=new PushbackReader(stream);
        int returnValue;
        int nextInput;
        Node n;
        try
        {
            System.out.println("I'm node: id["+id+"] value["+value+"] level["+level+"]");
            nextInput=workInput.read(); System.out.println("Read: "+(char)nextInput);
            if(nextInput==-1) return -1;  //END OF FILE (return -1)
            
            if((n=this.searchNext((char)nextInput))==null)    
            {   System.out.println("No such path ");
                if(level!=0)returnValue= nextInput; //END OF PATH (send back la read input)
                else returnValue = 1; //NO POSSIBLE PATH
            } 
            else 
            {   System.out.println("Such path exist, continue");
                if(n.atWordEnd) System.out.println("Word find by "+Thread.currentThread().getName()+" in "+workInput+"  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"); System.out.println("Call search() in node:"+n.toString());
                returnValue=n.search(workInput);
            }
            if(returnValue==-1) return -1; //trasmit EOF back in calls chain then the root node exit 
            if(level==0) 
            {
                if(returnValue!=1)
                {   System.out.println("Push back "+(char)returnValue);//push back last read input
                    workInput.unread(returnValue);
                } 
                returnValue=this.search(workInput);//restart seach from root
            }
            if(returnValue!=-1) return returnValue; //trasmit END OF PATH(with last input read) back to root 
            if(returnValue==-1) return -1; //exit point after first path
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 2;
    }

}
