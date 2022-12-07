package programa3;

import org.jzy3d.colors.Color;

public class Punto {
    int coordena[] = new int[3];
    int clase;
    Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
    
    public void setCoordena(int x, int y, int z) {
        this.coordena[0] = x;
        this.coordena[1] = y;
        this.coordena[2] = z;
    }

    public void setClase(int clase) {
        this.clase = clase;
    }
    public int[] getCoordena() {
        return coordena;
    }

    public int getClase() {
        return clase;
    }


}
