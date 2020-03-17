package com.p6majo.particlesimulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Beschreiben Sie hier die Klasse BQuadTree.
 *
 * @author AlexK
 * @version 2020-01-18
 * @author p6majo
 * @version 2020-03-04
 * Uebergang zu double, damit auch Teilchen, die sich sehr nahe kommen, in zwei getrennte Zweige gepackt werden koennen

 */
public class QuadTree
{
    private Rect boundary;
    private int n;
 
    private List<Particle> points = new ArrayList<Particle>();
    private List<BarnesHutCenter> gravityCenters = new ArrayList<>();

    private QuadTree northwest;
    private QuadTree northeast;
    private QuadTree southwest;
    private QuadTree southeast;


    private boolean divided;
    private QuadTree root;

    private Vector centerOfMass;
    private int mass;
    private Model model;

    //p6majo
    private int level; //for debugging

    public QuadTree(Model model, Rect boundary, int capacity){
        this(model,boundary,capacity,null,0);
    }

    /**
     * Constructor
     */
    public QuadTree(Model model,Rect boundary, int capacity, QuadTree root, int level)
    {
        this.model = model;
        this.boundary = boundary;
        this.n = capacity;
        this.root = root;
        this.divided = false;
        this.level = level;
         this.centerOfMass = new Vector();

    }




    public boolean IsRoot(){
        return root==null;
    }


    public QuadTree getRoot(){
        return this.root;
    }

    public Vector getCenterOfMass(){
        return this.centerOfMass;
    }

    public int getMass(){
        return this.mass;
    }


    public List<BarnesHutCenter> getBarnesHutCenters(){
        if (this.gravityCenters.size()>0)
            return this.gravityCenters;
        else if (divided) {
            List<BarnesHutCenter> centers = new ArrayList<>();
            centers.addAll(this.northeast.getBarnesHutCenters());
            centers.addAll(this.northwest.getBarnesHutCenters());
            centers.addAll(this.southeast.getBarnesHutCenters());
            centers.addAll(this.southwest.getBarnesHutCenters());
            return centers;
        }
        return new ArrayList<BarnesHutCenter>();
    }
    /**
     * p6majo veraendert Einfuegen entsprechend der Vorschlaege in Barnes-Hut-Artikel
     * @param point
     * @return
     */
    public boolean insert(Particle point){
        //fuege Punkt nicht hinzu, wenn er nicht in der Box liegt
        if(!this.boundary.contains(point)){
            return false;
        }

        //in der Box ist noch Platz, der Punkt wird einfach eingefuegt
        if(this.points.size() < this.n ){
            this.points.add(point);
            return true;//p6majo
        }
        else if(this.points.size()==this.n) {
            //In der Box ist kein Platz mehr.
            //Die Box wird unterteilt und alle bestehenden Teilchen der Box werden auf die neuen Boxen umverteilt
            //der bereits existierende Punkt wird auch in einen entsprechenden Unterknoten eingefuegt


            this.subdivide();
            for (Particle existingPoint : points) {
                if (!this.northeast.insert(existingPoint))
                    if (!this.northwest.insert(existingPoint))
                        if (!this.southeast.insert(existingPoint))
                            if (!this.southwest.insert(existingPoint))
                                System.err.println("Fehler beim Umverteilen"); //sollte nicht passieren
            }

            this.points.add(point);//fuege Punkt zur Box als Referenz hinzu, da er der Punkt in einer Unterbox landen wird

            if(this.northeast.insert(point))
                return true;
            else if(this.northwest.insert(point))
                return true;
            else if(this.southeast.insert(point))
                return true;
            else if(this.southwest.insert(point))
                return true;

        }
        else{
            this.points.add(point);//fuege Punkt zur Box als Referenz hinzu, da er der Punkt in einer Unterbox landen wird

            //in diesem Fall existieren auf alle Faelle Unterbaeume, in einem von denen das Teilchen eingefuegt werden muss
            if(this.northeast.insert(point))
                return true;
            else if(this.northwest.insert(point))
                return true;
            else if(this.southeast.insert(point))
                return true;
            else if(this.southwest.insert(point))
                return true;
        }

        System.err.println("Punkt "+point.toString()+" konnte nicht hinzugefuegt werden.");//sollte nicht passieren
        return false;
    }
    private void subdivide(){
        double x = this.boundary.getX();
        double y = this.boundary.getY();
        double w = this.boundary.getW();
        double h = this.boundary.getH();
        double w2 =w/2;
        double h2= h/2;

        Rect ne = new Rect(x + w2, y - h2, w2, h2);
        //ne.show();
        this.northeast = new QuadTree(model,ne, n, root,level+1);
        Rect nw = new Rect(x - w2, y - h2, w2, h2);
        //nw.show();
        this.northwest = new QuadTree(model,nw, n, root,level+1);
        Rect se = new Rect(x + w2, y + h2, w2, h2);
        //se.show();
        this.southeast = new QuadTree(model,se, n, root,level+1);
        Rect sw = new Rect(x - w2, y + h2, w2, h2);
        //sw.show();
        this.southwest = new QuadTree(model,sw, n, root,level+1);
        this.divided = true;

    }

    /**
     * p6majo: angepasst an die neue Struktur. Da die Punkte auf jeder Ebene referenziert werden, duerfen sie nur auf der
     * untersten Ebene hinzugefuegt werden, da sie sonst mehrfach gelistet werden.
     * @param range
     * @return
     */
    public ArrayList<Particle> query(Rect range){
        ArrayList<Particle> found = new ArrayList<>();

        if (this.boundary.intersects(range)) {

            if (this.divided) {
                if (northwest.boundary.intersects(range))
                    found.addAll(this.northwest.query(range));
                if (northeast.boundary.intersects(range))
                    found.addAll(this.northeast.query(range));
                if (southwest.boundary.intersects(range))
                    found.addAll(this.southwest.query(range));
                if (southeast.boundary.intersects(range))
                    found.addAll(this.southeast.query(range));
            } else
                for (int i = 0; i < this.points.size(); i++) {
                    Particle p = points.get(i);
                    if (range.contains(p)) {
                        found.add(p);
                    }
                }
        }
        return found;
    }

    public ArrayList<Particle> circularquery(Circle range){
        ArrayList<Particle> found = new ArrayList<>();

        if(!this.boundary.cIntersects(range)){
            return found;
        }else{
            for(int i = 0; i < this.points.size(); i++){
                Particle p = points.get(i);
                if(range.contains(p)){
                    found.add(p);
                }
            }
            if(this.divided){
                found.addAll(this.northwest.circularquery(range));
                found.addAll(this.northeast.circularquery(range));
                found.addAll(this.southwest.circularquery(range));
                found.addAll(this.southeast.circularquery(range));
            }
            return found;
        }
    }
    public void ComputeMassDistribution()
    {
        if (points.size()==0){
            centerOfMass = new Vector(0.,0.);
            mass = 0;
        }
        else if (points.size() == 1){
            Particle p = points.get(0);
            centerOfMass = new Vector(p.getX(), p.getY());
            //centerOfMass = new AVector(p.getX(), p.getX()); //p6majo: dummer Fehler, der hat die Schwerpunkte alle auf die Diagonale gebracht.
            mass = p.getMass();
        }
        else {
            //centerOfMass wird rekursiv berechnet
            if (this.centerOfMass==null)
                this.centerOfMass=new Vector(0,0);
            southeast.ComputeMassDistribution();
            if (southeast.mass>0) {
                centerOfMass = new Vector(this.mass * this.centerOfMass.getX() + southeast.centerOfMass.getX() * southeast.mass, this.mass * this.centerOfMass.getY() + southeast.centerOfMass.getY() * southeast.mass);
                this.mass += southeast.mass;
                centerOfMass.mult(1. / this.mass);
            }

            southwest.ComputeMassDistribution();
            if (southwest.mass>0) {
                centerOfMass = new Vector(this.mass * this.centerOfMass.getX() + southwest.centerOfMass.getX() * southwest.mass, this.mass * this.centerOfMass.getY() + southwest.centerOfMass.getY() * southwest.mass);
                this.mass += southwest.mass;
                centerOfMass.mult(1. / this.mass);
            }

            northwest.ComputeMassDistribution();
            if (northwest.mass>0) {
                centerOfMass = new Vector(this.mass * this.centerOfMass.getX() + northwest.centerOfMass.getX() * northwest.mass, this.mass * this.centerOfMass.getY() + northwest.centerOfMass.getY() * northwest.mass);
                this.mass += northwest.mass;
                centerOfMass.mult(1. / this.mass);
            }

            northeast.ComputeMassDistribution();
            if (northeast.mass>0) {
                centerOfMass = new Vector(this.mass * this.centerOfMass.getX() + northeast.centerOfMass.getX() * northeast.mass, this.mass * this.centerOfMass.getY() + northeast.centerOfMass.getY() * northeast.mass);
                this.mass += northeast.mass;
                centerOfMass.mult(1. / this.mass);
            }
        }
    }

    /**
     * Hier wird der eigentliche Barnes-Hut-Algorithmus implementiert.
     * @param punkt Punkt, fuer den die Beschleunigung berechnet werden soll.
     * @return die Beschleunigung des Punktes
     */
    public Vector calculateAcceleration(Particle punkt, double thetaMax, boolean show){
        double r = punkt.distanceTo(this.centerOfMass);
        double theta = 2.*this.boundary.getW()/r;
        //kein Teilchen
        if (this.points.size()==0)
            return Vector.ZERO;

        //weit genug entfernt oder nur ein Teilchen
        if (theta<thetaMax||this.points.size()==1) {
            if (r==0)
                return Vector.ZERO;//das Teilchen selbst wird ausgelassen
            else {
                if (show) this.gravityCenters.add(new BarnesHutCenter(centerOfMass.getX(), centerOfMass.getY(), mass)); //visualisiere das Gravitationszentrum und dessen Groesse
                return this.centerOfMass.minus(punkt.getPos()).mal(1 / r).mal(model.acceleration(mass, r)); //Staubscheibe, ebene Anordnung im 3D
            }
        }
        else {
            //Berechnung aus Unterteilung
            Vector acc = Vector.ZERO;
            acc = acc.plus(this.southeast.calculateAcceleration(punkt,thetaMax,show));
            acc = acc.plus(this.southwest.calculateAcceleration(punkt,thetaMax,show));
            acc = acc.plus(this.northeast.calculateAcceleration(punkt,thetaMax,show));
            acc = acc.plus(this.northwest.calculateAcceleration(punkt,thetaMax,show));
            return acc;
        }
    }

    public ArrayList<Vector>concat(ArrayList<Vector> a, ArrayList<Vector> b){
        ArrayList<Vector> out = new ArrayList<Vector>();
        for(int i=0;i>a.size();i++)
            out.add(a.get(i));
        for(int i=0;i>b.size();i++)
            out.add(b.get(i));
        return out;
    }



    /**
     * rekursive Ausgabe des Baums auf der Konsole
     * @return
     */
    public String toString(){
        String out = "";
        String tabs = "";
        for (int i = 0; i < level; i++) {
            tabs+="\t";
        }
        if (!this.divided && this.points.size()>0){
            out+=this.points.get(0).toString()+"\n"; //Knotenteilchen
        }
        else{
            out+=this.points.size()+":\n";
            if (this.southeast.points.size()>0) out+=tabs+"se:"+this.southeast.toString();
            if (this.southwest.points.size()>0) out+=tabs+"sw:"+this.southwest.toString();
            if (this.northeast.points.size()>0) out+=tabs+"ne:"+this.northeast.toString();
            if (this.northwest.points.size()>0) out+=tabs+"nw:"+this.northwest.toString();
        }
        return out;
    }
}
