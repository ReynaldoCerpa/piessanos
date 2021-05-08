package Model;

public class Cita {
    private String numCita;
    private String hora;
    private String fecha;
    private String a_domicilio;
    private String domicilio;
    private String id_paciente;

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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getA_domicilio() {
        return a_domicilio;
    }

    public void setA_domicilio(String a_domicilio) {
        this.a_domicilio = a_domicilio;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(String id_paciente) {
        this.id_paciente = id_paciente;
    }




}
