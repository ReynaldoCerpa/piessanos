package viewControllers;

import javafx.event.ActionEvent;

public class NewCita {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public NewCita(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }
    public void cancelNewCita(ActionEvent event) {
        motor.showClient(event);
    }
}
