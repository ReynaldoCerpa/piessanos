package viewControllers;

import javafx.event.ActionEvent;

public class RegisterProveedor {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public RegisterProveedor(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void cancelRegisterProveedor(ActionEvent event) {
        motor.showProveedores(event);
    }
}
