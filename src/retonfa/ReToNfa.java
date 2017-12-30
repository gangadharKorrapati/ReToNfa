/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retonfa;

import java.util.ArrayList;

/**
 *
 * @author "gangadhar";
 */
public class ReToNfa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Nfa a = new Nfa('a');
        Nfa b = new Nfa('b');
        
        a.display();
        b.display();
        Nfa c = Nfa.union(a, Nfa.closure(Nfa.concat(a,b)));
        c.display();
        
        a.display();
        b.display();
        // TODO code application logic here
    }

}

class Edge {

    private final int fromState;
    private final int toState;
    private final char transVal;

    public Edge(int fs, char t, int ts) {
        fromState = fs;
        transVal = t;
        toState = ts;
    }

    public Edge(int fs, int ts) {
        fromState = fs;
        transVal = 'e';
        toState = ts;
    }

    public int getFromState() {
        return fromState;
    }

    public int getToState() {
        return toState;
    }

    public char getTransVal() {
        return transVal;
    }

    public void display() {
        System.out.println("\nFrom State:" + fromState + "\tTranssion Value:" + transVal + "\tTo State:" + toState);
    }
}

class Nfa {

    private final ArrayList<Edge> edges;
    private final int stateCount;

    public Nfa() {
        stateCount = 1;
        edges = new ArrayList();
        Edge e = new Edge(0, 1);
        edges.add(e);
    }

    public Nfa(char ch) {
        stateCount = 1;
        edges = new ArrayList();
        Edge e = new Edge(0, ch, 1);
        edges.add(e);
    }

    public Nfa(ArrayList<Edge> es, int c) {
        edges = es;
        stateCount = c;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public int getStateCount() {
        return stateCount;
    }

    public static Nfa union(Nfa a, Nfa b) {
        ArrayList<Edge> edgesa = new ArrayList<>(a.getEdges());
        ArrayList<Edge> edgesb = new ArrayList<>(b.getEdges());
        ArrayList<Edge> edgesc = new ArrayList<>();
        int acount = a.getStateCount();
        int bcount = b.getStateCount();

        for (Edge e : edgesa) {
            Edge p = new Edge(e.getFromState() + 1, e.getTransVal(), e.getToState() + 1);
            edgesc.add(p);
        }

        Edge starttoa = new Edge(0, 1);
        edgesc.add(starttoa);

        for (Edge e : edgesb) {
            Edge p = new Edge(e.getFromState() + acount + 2, e.getTransVal(), e.getToState() + acount + 2);
            edgesc.add(p);
        }

        Edge starttob = new Edge(0, acount + 2);
        edgesc.add(starttob);

        Edge atoend = new Edge(acount + 1, acount + bcount + 3);
        edgesc.add(atoend);

        Edge btoend = new Edge(acount + bcount + 2, acount + bcount + 3);
        edgesc.add(btoend);

        Nfa c = new Nfa(edgesc, acount + bcount + 3);
        return c;
    }

    public static Nfa concat(Nfa a, Nfa b) {
        ArrayList<Edge> edgesa = new ArrayList<>(a.getEdges());
        ArrayList<Edge> edgesb = new ArrayList<>(b.getEdges());
        ArrayList<Edge> edgesc = new ArrayList<>();

        int acount = a.getStateCount();
        int bcount = b.getStateCount();

        for (Edge e : edgesa) {
            Edge p = new Edge(e.getFromState(), e.getTransVal(), e.getToState());
            edgesc.add(p);
        }
        for (Edge e : edgesb) {
            Edge p = new Edge(e.getFromState() + acount, e.getTransVal(), e.getToState() + acount);
            edgesc.add(p);
        }

        Nfa c = new Nfa(edgesc, acount + bcount);
        return c;
    }

    public static Nfa closure(Nfa a) {
        ArrayList<Edge> edgesa = new ArrayList<>(a.getEdges());
        ArrayList<Edge> edgesc = new ArrayList<>();
        int acount = a.getStateCount();

        for (Edge e : edgesa) {
            Edge p = new Edge(e.getFromState() + 1, e.getTransVal(), e.getToState() + 1);
            edgesc.add(p);
        }
        
        Edge endtostart = new Edge(acount+1, 1);
        edgesc.add(endtostart);

        Edge nstarttostart = new Edge(0, 1);
        edgesc.add(nstarttostart);

        Edge endtonend = new Edge(acount + 1, acount + 2);
        edgesc.add(endtonend);

        Edge nstarttonend = new Edge(0, acount + 2);
        edgesc.add(nstarttonend);

        Nfa c = new Nfa(edgesc, acount + 2);

        return c;
    }

    public void display() {
        System.out.println("************************NFA************************");
        System.out.println("\nStart state\t:0");
        System.out.println("\nEnd State\t:" + stateCount);
        edges.forEach((edge) -> edge.display());
    }
}
