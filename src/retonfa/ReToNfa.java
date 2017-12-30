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
        Nfa d = new Nfa('d');
        Nfa c = Nfa.union(d, Nfa.concat(a, b));
        c.display();
        // TODO code application logic here
    }

}

class Edge {

    private int fromState;
    private int toState;
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

    public void setFromState(int fs) {
        fromState = fs;
    }

    public void setToState(int ts) {
        toState = ts;
    }

    public void display() {
        System.out.println("\nFrom State:" + fromState + "\tTranssion Value:" + transVal + "\tTo State:" + toState);
    }
}

class Nfa {

    private ArrayList<Edge> edges;
    private int edgeCount;

    public Nfa() {
        edgeCount = 0;
        edges = new ArrayList();
        Edge e = new Edge(0, 1);
        insertEdge(e);
    }

    public Nfa(char ch) {
        edgeCount = 0;
        edges = new ArrayList();
        Edge e = new Edge(0, ch, 1);
        insertEdge(e);
    }

    public boolean insertEdge(Edge e) {
        boolean status = edges.add(e);
        if (status) {
            edgeCount++;
        }
        return status;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> setedges) {
        edges = setedges;
    }

    public int getEndState() {
        return edgeCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public void setEdgeCount(int c) {
        edgeCount = c;
    }

    public static Nfa union(Nfa a, Nfa b) {
        ArrayList<Edge> edgesa = a.getEdges();
        ArrayList<Edge> edgesb = b.getEdges();
        int acount = a.getEdgeCount();
        int bcount = b.getEdgeCount();
        for (Edge e : edgesa) {
            e.setFromState(e.getFromState() + 1);
            e.setToState(e.getToState() + 1);
        }
        Edge starttoa = new Edge(0, 1);
        edgesa.add(starttoa);
        for (Edge e : edgesb) {
            e.setFromState(e.getFromState() + acount + 2);
            e.setToState(acount + e.getToState() + 2);
            edgesa.add(e);
        }

        Edge starttob = new Edge(0, acount + 2);
        edgesa.add(starttob);

        Edge atoend = new Edge(acount + 1, acount + bcount + 3);
        edgesa.add(atoend);

        Edge btoend = new Edge(acount + bcount + 2, acount + bcount + 3);
        edgesa.add(btoend);
        a.setEdges(edgesa);
        a.setEdgeCount(acount + bcount + 3);
        return a;
    }

    public static Nfa concat(Nfa a, Nfa b) {
        ArrayList<Edge> edgesa = a.getEdges();
        ArrayList<Edge> edgesb = b.getEdges();
        int acount = a.getEdgeCount();
        int bcount = b.getEdgeCount();
        for (Edge e : edgesb) {
            e.setFromState(e.getFromState() + acount);
            e.setToState(e.getToState() + acount);
            edgesa.add(e);
        }
        a.setEdges(edgesa);
        a.setEdgeCount(acount + bcount);
        return a;
    }

    public void closure() {

    }

    public void display() {
        edges.forEach((edge) -> edge.display());
    }
}
