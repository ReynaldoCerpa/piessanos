package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.sql.*;

public class Login {
    private SQLconnector database = new SQLconnector();
    private Motor motor;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private ImageView invalidUserAlert, fillBlanksAlert;

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void login(ActionEvent event) throws SQLException {

        if (usernameInput.getText().equals("") && passwordInput.getText().equals("")){
            invalidUserAlert.setVisible(false);
            fillBlanksAlert.setVisible(true);
        } else {

            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("medico");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;

            while(myRes.next()){
                String username = myRes.getString("usuario");
                String password = myRes.getString("contrasena");

                if(username.equals(usernameInput.getText()) && password.equals(passwordInput.getText())){

                    motor.setCedula(myRes.getString("cedula_profesional"));
                    motor.setNombre(myRes.getString("nombre"));
                    motor.setUsername(myRes.getString("usuario"));
                    motor.setApellidop(myRes.getString("nomPaterno"));
                    motor.setApellidom(myRes.getString("nomMaterno"));
                    motor.setAdmin(false);
                    if (myRes.getInt("isAdmin") == 1){
                        motor.setAdmin(true);
                    }
                    notfound = false;


                    System.out.println("Logged in as: "+username);
                    motor.showClient(event);
                    break;
                }
            }
            if (notfound){
                invalidUserAlert.setVisible(true);
                fillBlanksAlert.setVisible(false);
            }
        }
    }
}
