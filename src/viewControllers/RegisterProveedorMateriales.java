package viewControllers;

import javafx.event.ActionEvent;

public class RegisterProveedorMateriales {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public RegisterProveedorMateriales(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }
    public void cancelRegisterProveedor(ActionEvent event) {
        motor.showProveedoresMateriales(event);
    }
}
