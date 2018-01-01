/* 
 * The MIT License
 *
 * Copyright 2017 "gangadhar";.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package retonfa;

import java.util.ArrayList;
import java.util.Stack;
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

    private static Nfa reToNfa(String re) {
        re = "(" + re + ")";
        Stack<Object> stack = new Stack<>();
        for (int i = 0; i < re.length(); i++) {
            switch (re.charAt(i)) {
                case '(':
                    stack.push('(');
                    break;
                case '|':
                    stack.push('|');
                    break;
                case '*':
                    Nfa x = Nfa.closure((Nfa) stack.pop());
                    stack.push(x);
                    break;
                case ')':
                    Stack<Object> hold = new Stack<>();
                    Object r = stack.pop();
                    while (r instanceof Nfa || (char) r != '(') {
                        hold.push(r);
                        r = stack.pop();
                    }
                    Object p = hold.pop();
                    while (!hold.empty()) {
                        Object q = hold.pop();
                        if (!(q instanceof Nfa)) {
                            p = Nfa.union((Nfa) p, (Nfa) hold.pop());
                        } else {
                            p = Nfa.concat((Nfa) p, (Nfa) q);
                        }
                    }
                    stack.push((Nfa) p);
                    break;
                default:
                    Nfa one = new Nfa(re.charAt(i));
                    stack.push(one);
                    break;
            }
        }
        return (Nfa) stack.pop();
    }
}
