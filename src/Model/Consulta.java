package Model;

public class Consulta {
    private String numCita;
    private String fecha;
    private String atendido;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumCita() {
        return numCita;
    }

    public void setNumCita(String numCita) {
        this.numCita = numCita;
    }

    public String getAtendido() {
        return atendido;
    }

    public void setAtendido(String atendido) {
        this.atendido = atendido;
    }
}
