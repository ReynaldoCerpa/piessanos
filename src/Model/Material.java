package Model;

public class Material {
    private String  nombre, cantidadInventario, id, proveedor, precioproveedor;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getPrecioproveedor() {
        return precioproveedor;
    }

    public void setPrecioproveedor(String precioproveedor) {
        this.precioproveedor = precioproveedor;
    }
}
