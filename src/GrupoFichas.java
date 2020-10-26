package src;

import java.util.List;

public class GrupoFichas {
    private List<Ficha> fichas;

    public GrupoFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    public List<Ficha> getFichas() {
        return this.fichas;
    }
    
    public boolean estaVacio() {
        return this.fichas.isEmpty();
    }
}
