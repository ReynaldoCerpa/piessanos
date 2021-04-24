package viewControllers;

import javafx.event.ActionEvent;

public class Proveedores {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Proveedores(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void registerProveedor(ActionEvent event) {
        motor.showRegisterProveedor(event);
    }

    public void backToInventario(ActionEvent event) {
        motor.showMedicamentos(event);
    }
}
