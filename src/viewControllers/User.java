package viewControllers;

public class User {
    private String cedula;
    private String nombre;
    private String apellidop;
    private String apellidom;
    private boolean isAdmin;
    private String username;

    public User(String cedula, String nombre, boolean isAdmin, String username){
        this.cedula = cedula;
        this.nombre = nombre;
        this.isAdmin = isAdmin;
        this.username = username;
    }
    public User(){

    }

    public void setCedula(String cedula){
        this.cedula = cedula;
    }
    public String getCedula(){
        return cedula;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getNombre(){
        return nombre;
    }
    public void setApellidop(String apellidop){
        this.apellidop = apellidop;
    }
    public String getApellidop(){
        return apellidop;
    }
    public void setApellidom(String apellidom){
        this.apellidom = apellidom;
    }
    public String getApellidom(){
        return apellidom;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
    public boolean getisAdmin(){
        return isAdmin;
    }
}
