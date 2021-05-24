package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowConsulta {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private Label noconsulta, temperatura, monto, nombreMedico, indicaciones, observaciones, fecha;
    @FXML
    private Group alertGroup, requiredGroup;

    public ShowConsulta(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null, medRes = null;
        try {
            myRes = database.connectSQL("consulta");
            medRes = database.connectSQL("medico_consulta");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next() && medRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("idCita"))){

                String cedulaMed = medRes.getString("cedula_profesional");
                String medicoName = "";

                String expquery = "select * from medico where cedula_profesional = ?";
                PreparedStatement nombreM = database.updateData(expquery);
                nombreM.setString(1, cedulaMed);
                ResultSet name = nombreM.executeQuery();
                while(name.next()){
                    medicoName = name.getString("nombre")+" "+name.getString("nomPaterno")+" "+name.getString("nomMaterno");
                }

                noconsulta.setText(myRes.getString("idconsulta"));
                temperatura.setText(myRes.getString("temperatura_covid"));
                monto.setText("$"+myRes.getString("costo_total"));
                nombreMedico.setText(medicoName);
                indicaciones.setText(myRes.getString("indicaciones"));
                observaciones.setText(myRes.getString("observaciones"));
                fecha.setText(medRes.getString("fecha_edicion"));
                break;
            }
        }
        motor.setSelectedItem(motor.getAuxItem());
    }

    public void cerrar(ActionEvent event) {
        motor.closeConsulta(event);
    }
}
