package viewControllers;

import javafx.event.ActionEvent;

public class ProveedoresMateriales {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public ProveedoresMateriales(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }
    public void registerProveedor(ActionEvent event) {
        motor.showRegisterProveedorMateriales(event);
    }

    public void backToInventario(ActionEvent event) {
        motor.showMateriales(event);
    }
}
