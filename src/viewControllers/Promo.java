package viewControllers;

import javafx.event.ActionEvent;

import java.sql.SQLException;

public class Promo {
    private Motor motor;
    private SQLconnector database = new SQLconnector();

    public Promo(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void displayHome(ActionEvent event) {
        motor.showClient(event);
    }

    public void createPromo(ActionEvent event) {
    motor.showCreatePromo(event);
    }
}
