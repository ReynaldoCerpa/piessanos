package viewControllers;

import javafx.event.ActionEvent;

public class RegisterMedicamentos {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public RegisterMedicamentos(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void cancelRegisterMedicamento(ActionEvent event) {
        motor.showMedicamentos(event);
    }
}
