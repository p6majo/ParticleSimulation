package com.p6majo.octtree;

import com.p6majo.logger.Logger;
import com.p6majo.models.Model3D;
import com.p6majo.particlesimulation.Particle;

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
    public int n = 1;

    //List of all particles contained in the tree and its subtrees
    public List<O> content = null;

    //boundaries
    private Cuboid boundary;

    //center of mass
    private Vector3D centerOfMass;
    private double mass;

    private boolean divided;
    private final boolean root;
    private int level;

    //subtrees, each branch splits into eight branches which are represented by {000,001,010,011,100,101,110,111}
    private Octtree<O>[] subtrees = new Octtree[8];

    private Model3D model;

    /*********************/
    /***  constructors ***/
    /*********************/

    public Octtree(Cuboid boundary){
        this(null,boundary,true);
    }

    public Octtree(Cuboid boundary,int n){
        this(null,boundary,n,true);
    }

    public Octtree(Vector3D low,Vector3D high){
        this(null,new Cuboid(low,high),true);
    }

    public Octtree(Vector3D low,Vector3D high,int n){
        this(null,new Cuboid(low,high),n,true);
    }

    /**
     *  Construct an empty octtree representing a box in 3D space.
     *  The box is represented by the minValues and maxValues
     * @param low - collection of all the min values of the coordinates
     * @param high  collection of all the max values of the coordiantes
     */
    public Octtree(Model3D model,Vector3D low, Vector3D high){
        this(model,new Cuboid(low,high));
    }

    public Octtree(Model3D model,Vector3D low, Vector3D high,int n){
        this(model,new Cuboid(low,high),n,true);
    }

    public Octtree(Model3D model,Cuboid boundary){
        this(model,boundary,true);
    }

    public Octtree(Model3D model,int n,Cuboid boundary){
        this(model,boundary,n,true);
    }

    public Octtree(Model3D model,Vector3D low, Vector3D high,boolean root){
        this(model,new Cuboid(low,high),root);
    }

    public Octtree(Model3D model,Vector3D low, Vector3D high,int n,boolean root){
        this(model,new Cuboid(low,high),n,root);
    }

    private Octtree(Model3D model, Cuboid boundary, boolean root){
        this(model,boundary,1,root);
    }

    private Octtree(Model3D model, Cuboid boundary, int n, boolean root){
        this.model = model;
        this.boundary = boundary;
        this.root = true;
        this.n = n;
        this.divided = false;

        this.content = new ArrayList<>();
        for (int i = 0; i < subtrees.length; i++)
            subtrees[i]=null;
    }

    /************************/
    /****       getter    ***/
    /************************/

    public boolean isEmpty(){ return this.content.size()==0; }

    public double getMass(){
        return 0.;
    }

    public Vector3D getCenterOfMass(){
        return null;
    }

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
    public boolean addObject(O object){


        //particle doesn't fit into the container
        if (!this.boundary.contains(object.getPosition()))
            return false;

        //particle can be added without exceeding the particle limit of the container
        if(content.size()< n){
            //add to current tree
            this.content.add(object);
            return true;
        }

        //no space is left to add another object
        if (content.size()==n){

            this.divided =  true;
            this.subdivide();

            for (O o : content) {
                int count = 0;
                while(!this.subtrees[count].addObject(o) && count<8)
                    count++;

                if (count==8)
                    Logger.logging(Logger.Level.error,"Error during subdivision and redistribution.");
            }

            this.content.add(object); //add object to the node, since it will end up in one of the subtrees.

            int count = 0;
            while(!this.subtrees[count].addObject(object))
                count++;

            if (count==8)
                Logger.logging(Logger.Level.error,"Error during adding of object "+object);
            else
                return true;
        }
        else{
            this.content.add(object);
            int count = 0;
            while(!this.subtrees[count].addObject(object))
                count++;

            if (count==8)
                Logger.logging(Logger.Level.error,"Error during adding of object "+object);
            else
                return true;
        }

        System.err.println(this);
        Logger.logging(Logger.Level.error,"Object "+object.toString()+" could not be added to the tree.");
        return false;
    }

    public List<O> query(Cuboid range){
        List<O> found = new ArrayList<>();

        if (this.boundary.intersects(range))
            if (this.divided)
                for (Octtree<O> subtree : subtrees)
                    found.addAll(subtree.query(range));
            else
                for (O o : content)
                    if (range.contains(o.getPosition()))
                        found.add(o);

        return found;
    }

    /**
     * Calculates the center of masses for each subtree
     */
    public void computeMassDistribution(){
        if (content.size()==0) {
            this.centerOfMass = Vector3D.getNULL();
            this.mass = 0.;
        }
        else if (content.size()<n){
            for (O o : content) {
                //centerOfMass =
            }
        }

    }

    /**
     * The Octtree is designed for holding moving objects. Once objects leave their bounding boxes, they should be added to different branches of the tree.
     * This method updates the object content of the tree. Particles that have left their bounding boxes are removed and re-inserted at a second step.
     *
     *
     public void updateRootTree(){
     reInsertParticles(getMovedOutObjects());
     }

     public boolean deleteObject(O object){
     return this.content.remove(object);
     }
     */


    /******************************/
    /****     private methods   ***/
    /******************************/

    /**
     * This method scans the tree an checks for particles that have left their bounding boxes. These particles are removed from the tree and returned in a list
     * They should be re-registered with the method insert
     * @return list of particles that are removed

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
     */


    /**
     * Create a substructure for the existing container
     */
    private void subdivide(){
        Vector3D low = boundary.getLow();
        Vector3D high = boundary.getHigh();
        Vector3D mid = Vector3D.midPoint(low,high);

        Vector3D sh = low.directionTo(mid);

        subtrees[0] = new Octtree(model,low,mid,n,false); //000
        subtrees[1] = new Octtree(model,low.shift(0,0,sh.getZ()),mid.shift(0,0,sh.getZ()),n,false); //001
        subtrees[2] = new Octtree(model,low.shift(0,sh.getY(),0),mid.shift(0,sh.getY(),0),n,false); //010
        subtrees[3] = new Octtree(model,low.shift(0,sh.getY(),sh.getZ()),mid.shift(0,sh.getY(),sh.getZ()),n,false); //011
        subtrees[4] = new Octtree(model,low.shift(sh.getX(),0,0),mid.shift(sh.getX(),0,0),n,false); //100
        subtrees[5] = new Octtree(model,low.shift(sh.getX(),0,sh.getZ()),mid.shift(sh.getX(),0,sh.getZ()),n,false);//101
        subtrees[6] = new Octtree(model,low.shift(sh.getX(),sh.getY(),0),mid.shift(sh.getX(),sh.getY(),0),n,false);//110
        subtrees[7] = new Octtree(model,mid,high);//111

    }

    private boolean isContained(O object){
        Vector3D position = object.getPosition();
        return this.boundary.contains(position);
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
        String out = "inside the "+this.boundary.toString()+"\n";
        if (this.content!=null){
            out+=content.stream().map(n->n.toString()).collect(Collectors.joining("\n"))+"\n";
            if (subtrees!=null)
                for (int i = 0; i < subtrees.length; i++) {
                    if (subtrees[i]!=null)
                        if (subtrees[i].getContent()!=null) {
                            StringTokenizer nnntokens = new StringTokenizer(subtrees[i].toString(), "\n");
                            out += subtreestrings[i] + "\n";
                            while (nnntokens.hasMoreTokens()) {
                                out += "\t" + nnntokens.nextToken() + "\n";
                            }
                        }
                    else
                        break;
                }

        }
        return out;
    }
}
