/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retonfa;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author "gangadhar";
 */
public class ReToNfa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("enter regexp:");
        Scanner s = new Scanner(System.in);
        String str = s.nextLine();
        Nfa c = new Nfa(str);
        c.display();
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
        System.out.println("\nFrom State:" + fromState + "\tTransition Value:" + transVal + "\tTo State:" + toState);
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

        Edge endtostart = new Edge(acount + 1, 1);
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
        System.out.println("************************Nfa************************");
        System.out.println("\nStart state\t:0");
        System.out.println("\nEnd State\t:" + stateCount);
        edges.forEach((edge) -> edge.display());
    }

    public Nfa(String str) {
        Nfa c = Nfa.reToNfa(str);
        edges = c.getEdges();
        stateCount = c.getStateCount();
    }

    public static Nfa reToNfa(String re) {
        Nfa newnfa = new Nfa();
        int rl = re.length();
        if (rl == 0) {
            return new Nfa();
        } else if (rl == 1) {
            return new Nfa(re.charAt(0));
        } else if (!(re.contains("*") || re.contains("|") || re.contains("(") || re.contains(")"))) {
            return Nfa.concat(reToNfa(re.substring(0, 1)), reToNfa(re.substring(1, rl)));
        } else {
            int br = 0;
            for (int i = 0; i < rl; i++) {
                switch (re.charAt(i)) {
                    case '(': {
                        br++;
                        if (i != 0 && br == 1) {
                            System.out.println(re + " is concat of " + re.substring(0, i) + " and " + re.substring(i, rl));
                            return Nfa.concat(reToNfa(re.substring(0, i)), reToNfa(re.substring(i, rl)));
                        }
                        if (re.charAt(rl - 1) != ')') {
                            int li = re.lastIndexOf(')');
                            System.out.println(" the char at " + (rl - 1) + " is " + re.charAt(rl - 1) + "and the last index of ) is " + li);
                            if (re.charAt(li+1) == '*') {
                                System.out.println(re + " is concat of " + re.substring(0, li + 1) + " and " + re.substring(li + 2, rl));
                                return Nfa.concat(reToNfa(re.substring(0, li + 1)), reToNfa(re.substring(li + 2, rl)));
                            } else {
                                System.out.println(re + " is concat of " + re.substring(0, li + 1) + " and " + re.substring(li + 1, rl));
                                return Nfa.concat(reToNfa(re.substring(0, li + 1)), reToNfa(re.substring(li + 1, rl)));
                            }
                        }
                        break;
                    }
                    case ')': {
                        br--;
                        if (i == rl - 1) {
                            System.out.println(" removing ( ) " + re + " is same as " + re.substring(1, i));
                            return reToNfa(re.substring(1, i));
                        }
                        break;
                    }
                    case '|': {
                        if (i == 0) {
                            System.out.println("Wrong RE i = " + i + "br =" + br + " char at i is " + re.charAt(i) + " re is " + re);
                        } else if (br == 0) {
                            System.out.println(re + " is union of " + re.substring(0, i) + " and " + re.substring(i + 1, rl));
                            return Nfa.union(reToNfa(re.substring(0, i)), reToNfa(re.substring(i + 1, rl)));
                        }
                        break;
                    }
                    case '*': {
                        if (br == 0) {
                            if (i == rl - 1) {
                                System.out.println(re + " is star of " + re.substring(0, i));
                                return Nfa.closure(reToNfa(re.substring(0, i)));
                            }
                            return Nfa.concat(Nfa.closure(reToNfa(re.substring(0, i - 1))), reToNfa(re.substring(i + 1, rl)));
                        }
                        break;
                    }

                }

            }
        }
        return newnfa;
    }
}
