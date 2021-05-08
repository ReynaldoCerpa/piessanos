package Model;

public class Material {
    private String  nombre, cantidadInventario;
    private int numInventario;

    public int getNumInventario() {
        return numInventario;
    }

    public void setNumInventario(int numInventario) {
        this.numInventario = numInventario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidadInventario() {
        return cantidadInventario;
    }

    public void setCantidadInventario(String cantidadInventario) {
        this.cantidadInventario = cantidadInventario;
    }

}
