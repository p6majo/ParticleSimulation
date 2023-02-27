package com.p6majo.dynamicpolyhedra;

import com.p6majo.linalg.Vector;
import com.p6majo.octtree.Particle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class NNParticle
 *
 * @author p6majo
 * @version 2021-02-04
 */
public class NNParticle extends Particle {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private List<NNParticle> neighbours; //nearest neighbours
    private List<NNParticle> secondClassNeighbour; //not so nearest neighbours
    private HashMap<NNParticle,Double> equiDistance;

    private int index=-1;



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public NNParticle(Vector position, int index) {
        super(position);
        init(index);
    }

    public NNParticle(Vector position, Vector velocity) {
        super(position, velocity);
        init(-1);
    }

    public NNParticle(Vector position, Vector velocity, double mass,int index) {
        super(position, velocity, mass);
        init(index);
    }

    public NNParticle(Vector position, Vector velocity, double mass, double radius,int index) {
        super(position, velocity, mass, radius,false);
        init(index);
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public List<NNParticle> getNeighbours(){return this.neighbours;}
    public List<NNParticle> getSecondClassNeighbours(){return this.secondClassNeighbour;}
    public double getDistanceTo(NNParticle neighbour){ return this.equiDistance.get(neighbour); }

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    /**
     * Add a new neighbour.
     * It is not possible to add this as a neighbour and each neighbour can only be added once.
     * The current distance between this and the neighbour is set to be the equilibrium distance.
     * @param neighbour
     */
    public boolean addNeighbour(NNParticle neighbour){
        if (!this.equiDistance.containsKey(neighbour) && neighbour!=this) {
            this.neighbours.add(neighbour);
            this.equiDistance.put(neighbour, this.getPosition().sub(neighbour.getPosition()).length());
            return true;
        }
        return false;
    }

    /**
     * Add a new second class neighbour.
     * The current distance between this and the neighbour is set to be the equilibrium distance.
     * @param neighbour
     */
    public void addSecondClassNeighbour(NNParticle neighbour){
        if (!this.equiDistance.containsKey(neighbour) && neighbour!=this) {
            this.secondClassNeighbour.add(neighbour);
            this.equiDistance.put(neighbour, this.getPosition().sub(neighbour.getPosition()).length());
        }
    }


    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    public int getIndex(){return this.index;}

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    private void init(int index){
        this.neighbours = new ArrayList<>();
        this.secondClassNeighbour = new ArrayList<>();

        this.equiDistance = new HashMap<>();
        this.index = index;
    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    public String listNeighbours(){
        return "["+neighbours.stream().map(x->x.getIndex()+"").collect(Collectors.joining(","))+"]";
    }

    public String listSecondClassNeighbours(){
        return "["+secondClassNeighbour.stream().map(x->x.getIndex()+"").collect(Collectors.joining(","))+"]";
    }

    @Override
    public String toString() {
        return "Particle No. "+index+" with "+ neighbours.size()+" nn: "+listNeighbours()+" and "+secondClassNeighbour.size()+" nnn: "+listSecondClassNeighbours();
    }


}
