package com.p6majo.octtree;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * The Octtree is a tree that contains eight subtrees at each non-empty node. It is used to effectively store particles
 * of a three dimensional space. The space is divided into eight subspaces, represented by the subtrees.
 * @author p6majo
 * @version 1.0
 * @date 2019-08-26
 */
public class Octtree<O extends ObjectIn3DSpace>{


    /**********************/
    /***   attributes   ***/
    /**********************/


    //number of objects per node
    public int N = 4;

    //List of all particles contained in the tree and its subtrees
    public List<O> content = null;

    //boundaries
    private Vector3D low;
    private Vector3D high;

    //center of mass
    private Vector3D centerOfMass;

    private final boolean root;
    private int level;

    //subtrees, each branch splits into eight branches which are represented by {000,001,010,011,100,101,110,111}
    private Octtree<O>[] subtrees = new Octtree[8];

    /*********************/
    /***  constructors ***/
    /*********************/

    /**
     *  Construct an empty octtree representing a box in 3D space.
     *  The box is represented by the minValues and maxValues
     * @param low - collection of all the min values of the coordinates
     * @param high  collection of all the max values of the coordiantes
     */
    public Octtree(Vector3D low, Vector3D high){

        this.low = low;
        this.high = high;
        this.root = true;

        this.content = null;
        for (int i = 0; i < subtrees.length; i++)
            subtrees[i]=null;
    }

    /**
     *  Construct an empty octtree representing a box in 3D space.
     *  The box is represented by the minValues and maxValues
     * @param low  collection of all the min values of the coordinates
     * @param high  collection of all the max values of the coordiantes
     * @param root explicitly state, whether the node is root or not
     */
    private Octtree(Vector3D low, Vector3D high, boolean root){

        this.low = low;
        this.high = high;
        this.root = root;

        this.content = null;
        for (int i = 0; i < subtrees.length; i++)
            subtrees[i]=null;
    }

    /************************/
    /****       getter    ***/
    /************************/

    public boolean isEmpty(){ return this.content.size()==0; }


    /**
     * Returns the list of objects of the tree and null if the tree is empty
     * @return
     */
    public List<O>  getContent(){
        return content;
    }

    /************************/
    /****       setter    ***/
    /************************/


    /******************************/
    /****     public methods    ***/
    /******************************/

    /**
     * this is the only possibility to add something into the tree
     * If there is space, the object is added otherwise it is transferred to the appropriate subtree
     *
     * If the first object is added, the corresponding subtrees are automatically generated
     *
     *
     * @param object
     * @return
     */
    public void addObject(O object){
        Vector3D mid = Vector3D.midPoint(low,high);

        if (this.content==null){
            this.content = new ArrayList<O>();
            this.content.add(object);

            Vector3D sh = low.directionTo(mid);

            subtrees[0] = new Octtree(low,mid,false); //000
            subtrees[1] = new Octtree(low.shift(0,0,sh.getZ()),mid.shift(0,0,sh.getZ()),false); //001
            subtrees[2] = new Octtree(low.shift(0,sh.getY(),0),mid.shift(0,sh.getY(),0),false); //010
            subtrees[3] = new Octtree(low.shift(0,sh.getY(),sh.getZ()),mid.shift(0,sh.getY(),sh.getZ()),false); //011
            subtrees[4] = new Octtree(low.shift(sh.getX(),0,0),mid.shift(sh.getX(),0,0),false); //100
            subtrees[5] = new Octtree(low.shift(sh.getX(),0,sh.getZ()),mid.shift(sh.getX(),0,sh.getZ()),false);//101
            subtrees[6] = new Octtree(low.shift(sh.getX(),sh.getY(),0),mid.shift(sh.getX(),sh.getY(),0),false);//110
            subtrees[7] = new Octtree(mid,high);//111

        }
        else if(content.size()<N){
            //add to current tree
            this.content.add(object);
        }
        else{
            //find correct subtree
            Vector3D position = object.getPosition();
            int index  = 0;
            if (position.getX()>mid.getX()) index+=4;
            if (position.getY()>mid.getY()) index+=2;
            if (position.getZ()>mid.getZ()) index++;

            subtrees[index].addObject(object);
        }

    }

    /**
     * The Octtree is designed for holding moving objects. Once objects leave their bounding boxes, they should be added to different branches of the tree.
     * This method updates the object content of the tree. Particles that have left their bounding boxes are removed and re-inserted at a second step.
     *
     */
    public void updateRootTree(){
        reInsertParticles(getMovedOutObjects());
    }

    public boolean deleteObject(O object){
        return this.content.remove(object);
    }


    /******************************/
    /****     private methods   ***/
    /******************************/

    /**
     * This method scans the tree an checks for particles that have left their bounding boxes. These particles are removed from the tree and returned in a list
     * They should be re-registered with the method insert
     * @return list of particles that are removed
     */
    private List<O> getMovedOutObjects(){
        final List<O> movedOutObjects=new ArrayList();
        if (content!=null){
            final List<O> toDelete = new ArrayList<>();
            content.stream().forEach(x-> {if (!isContained(x))toDelete.add(x);});
            toDelete.stream().forEach(x->{
                movedOutObjects.add(x);
                deleteObject(x);});

            for (int i = 0; i < subtrees.length; i++)
                movedOutObjects.addAll(subtrees[i].getMovedOutObjects());

        }
        return movedOutObjects;
    }

    private void reInsertParticles(List<O> movedOutObject){
        movedOutObject.stream().forEach(x->this.addObject(x));
    }

    private boolean isContained(O object){
        Vector3D position = object.getPosition();
        return (position.getX()<this.high.getX() && position.getX()>=this.low.getX()
        && position.getY()<this.high.getY() && position.getY()>=this.low.getY()
                && position.getZ()<this.high.getZ() && position.getZ()>=this.low.getZ());
    }
    /******************************/
    /****     overrides         ***/
    /******************************/

    /******************************/
    /****     toString()        ***/
    /******************************/

    @Override
    public String toString() {
        String[] subtreestrings=new String[]{"000","001","010","011","100","101","110","111"};
        String out = "from "+this.low.toString()+" to "+this.high.toString()+"\n";
        if (this.content!=null){
            out+=content.stream().map(n->n.toString()).collect(Collectors.joining("\n"))+"\n";
            for (int i = 0; i < subtrees.length; i++) {
                if (subtrees[i].getContent()!=null) {
                    StringTokenizer nnntokens = new StringTokenizer(subtrees[i].toString(), "\n");
                    out += subtreestrings[i] + "\n";
                    while (nnntokens.hasMoreTokens()) {
                        out += "\t" + nnntokens.nextToken() + "\n";
                    }
                }
            }

        }
        return out;
    }
}
