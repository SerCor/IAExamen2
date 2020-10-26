package src;

import java.util.Arrays;

public class Ficha {
    /**
     * Representa una ficha del juego con dos valores(v1, v2) en el rango [0-6]
     */
    private byte[] valores;

    public Ficha(byte vI, byte vD) {
        this.valores = new byte[]{vI, vD};

        // Verificacion de rango
        byte[] aux = this.valores.clone(); Arrays.sort(aux);
        if(aux[0] < 0 || aux[1] > 6) {
            throw new IllegalArgumentException("el valor de las fichas debe se 0 <= n <= 6");
        }
    }

    public byte[] getValores() {
        return this.valores;
    }

    public byte valorIzq() {
        return this.valores[0];
    }

    public byte valorDer() {
        return this.valores[1];
    }

    public void rotar() {
        byte aux = this.valores[0];
        this.valores[0] = this.valores[1];
        this.valores[1] = aux;
    }

    @Override
    public boolean equals(Object o) {
        Ficha f2 = (Ficha) o;
        return this.valores.equals(f2.getValores());
    }
}