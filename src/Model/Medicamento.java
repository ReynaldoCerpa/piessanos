package Model;

public class Medicamento {
    private String codigo, nombre, precio, cantidadInventario, proveedor, descripcion, precioproveedor;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidadInventario() {
        return cantidadInventario;
    }

    public void setCantidadInventario(String cantidadInventario) {
        this.cantidadInventario = cantidadInventario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
