package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowConsulta {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private Label noconsulta, temperatura, monto, nombreMedico, indicaciones, observaciones;
    @FXML
    private Group alertGroup, requiredGroup;

    public ShowConsulta(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("consulta");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("numCita"))){
                noconsulta.setText(myRes.getString("numconsulta"));
                temperatura.setText(myRes.getString("temperatura_covid"));
                monto.setText(myRes.getString("costo_total"));
                nombreMedico.setText(myRes.getString("numconsulta"));
                break;
            }
        }
    }

    public void cerrar(ActionEvent event) {
        motor.closeConsulta(event);
    }
}
