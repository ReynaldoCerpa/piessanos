package viewControllers;

import javafx.event.ActionEvent;

public class RegisterMateriales {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public RegisterMateriales(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }
    public void cancelRegisterMateriales(ActionEvent event) {
        motor.showMateriales(event);
    }
}
