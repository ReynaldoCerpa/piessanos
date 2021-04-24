package viewControllers;

import javafx.event.ActionEvent;

public class Citas {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Citas(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }


    public void newCita(ActionEvent event) {
        motor.showNewCita(event);
    }

    public void displayHome(ActionEvent event) {
        motor.showClient(event);
    }
}
