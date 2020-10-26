package src;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Function;


class Estado {
    /**
     * Representa el estado del juego en un instante de tiempo.
     */
    private GrupoFichas m1;
    private GrupoFichas m2;
    private GrupoFichas sopate;
    private Tablero tablero;
    private int turno;
    
    public Estado(GrupoFichas m1, GrupoFichas m2, GrupoFichas sopa, Tablero tablero, int turno) {
        this.m1 = m1;
        this.m2 = m2;
        this.sopa = sopa;
        this.tablero = tablero;
        this.turno = turno;
    }

    public GrupoFichas getManoJugador1() {
        return this.m1;
    }

    public GrupoFichas getManoJugador2() {
        return this.m2;
    }

    public Tablero getTablero() {
        return this.tablero;
    }
}

// Define el comportamiento de un nodo en el algortimo Poda Alfa/Beta
enum Comportamiento {
    Maximizar,
    Minimizar
};

class Nodo {
    /**
     * Representa un nodo en el árbol de expansión
     */
    private Estado estado;
    private Vector<Nodo> childrens;
    private Comportamiento comportamiento;
    private Nodo mejorHijo;
    private double beta;
    private double theta;

    public Nodo(Estado estado, Comportamiento comportamiento, double beta, double theta) {
        this.estado = estado;
        this.comportamiento = comportamiento;
        this.theta = theta;
        this.beta = beta;
    }

    public Nodo(Estado estado, Comportamiento comportameinto) {
        this(estado, comportameinto, Double.MAX_VALUE, Double.MIN_VALUE);
    }

    public void setChildrens(Vector<Nodo> c) {
        this.childrens = c;
    }

    public Vector<Nodo> getHijos() {
        return this.childrens;
    }

    public Comportamiento getComportamiento() {
        return this.comportamiento;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public double getBeta() {
        return this.beta;
    }

    public double getTheta() {
        return this.theta;
    }

    public Nodo getMejorHijo() {
        return this.mejorHijo;
    }

    public void setMejorHijo(Nodo mejorHijo) {
        this.mejorHijo = mejorHijo;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}

class TreeIterator {
    /**
     * Iterator sobre un árbol de nodos
     */
    private Nodo raiz;
    private Nodo nodoActual;

    public TreeIterator(Nodo raiz) {
        this.raiz = raiz;
        this.nodoActual = raiz;
    }

    public Nodo getEstado() {
        return this.nodoActual;
    }

    public void avanzarA(Tablero tableroObjetivo) {
        // FIXME: Usuario elige una rama que no se expandio
        Vector<Nodo> hijos = this.nodoActual.getHijos();
        int index = 0;

        while(index < hijos.size() && hijos.get(index).getEstado().getTablero() != tableroObjetivo)
            index ++;

        if(index < hijos.size()) // Se encontro el hijo con el tablero objetivo
            this.nodoActual = hijos.get(index);
        else // No se encontro ningun hijo
            throw new IllegalArgumentException("no se encontro ningun hijo con el tablero objetivo esperado");
    }

    public void avanzarMejor() {
        // Utilizado para avanzar hacia el mejor hijo
        if(this.nodoActual.getComportamiento() != Comportamiento.Maximizar) {
            System.out.println("Warning. Esta implementacion no soporta el mejor movimiento para el nodo min");
        }

        this.nodoActual = this.nodoActual.getMejorHijo();
    }

    public boolean hasNext() {
        return !this.nodoActual.getHijos().isEmpty();
    }
}

class TreeBuilder {
    /**
     * Constructor de árboles a partir de una función de evaluación y un horizonte limitado
     * Hace uso del algortimo poda alfa/beta para hacer la expansión del árbol.
     */
    private int HORIZONTE_LIMITADO;
    private FuncionEvaluacion funEval;

    public TreeBuilder(FuncionEvaluacion funEval, int horizonteLimitado) {
        /**
         * Crea un constructor de árboles construido a partir de un horizonte limitado
         * y una función de evaluación
         */
        if(horizonteLimitado%2 != 0) {
            System.err.println("Warning. El horizonte limitado debe ser un número par.");
        }
        this.funEval = funEval;
        this.HORIZONTE_LIMITADO = horizonteLimitado;
    }

    public void build(Nodo raiz) {
        /**
         * Construye un árbol a partir del algortimo poda
         * alpha beta.
         */
        podaAlphaBeta(raiz, 0, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public List<Nodo> expandir(Nodo nodo) {
        /**
         * Expande los hijos nodo a partir de un nodo.
         */
        Estado estado = nodo.getEstado();
        Tablero tablero = estado.getTablero();
        GrupoFichas manoActual;

        if(nodo.getComportamiento() == Comportamiento.Maximizar)
            manoActual = estado.getManoJugador1();
        else
            manoActual = estado.getManoJugador2();


    }

    private void podaAlphaBeta(Nodo raiz, int nivel, double theta, double beta) {
        // Se encontro con el nodo hoja
        if(nivel == this.HORIZONTE_LIMITADO) {
            this.funEval.ejecutar(raiz);
        }

        List<Nodo> hijos = expandir(raiz);
        if(hijos.isEmpty()) {
            this.funEval.ejecutar(raiz);
        }

        for(Nodo hijo: hijos) {
            if(hijo.getComportamiento() == Comportamiento.Maximizar) {
                podaAlphaBeta(hijo, nivel+1, theta, beta);
                beta = Math.max(beta, hijo.getBeta());
                raiz.setBeta(beta);
            } else {
                podaAlphaBeta(hijo, nivel+1, theta, beta);
                theta = Math.min(theta, hijo.getTheta());
                raiz.setTheta(theta);
            }
        }
        
        // return (raiz.getComportamiento() == Comportamiento.Maximizar)
        //     ? theta
        //     : beta;
    }
}
