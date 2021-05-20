package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class EditPaciente {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, apellidopInput, apellidomInput, tel1Input, tel2Input;
    @FXML
    private TextArea alergiasInput, enfermedadesInput, medPrescritosInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public EditPaciente(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {

        this.motor = m;

        ResultSet myRes = null, telRes = null, enfRes = null, medRes = null, aleRes = null;
        try{
            myRes = database.connectSQL("paciente");
            telRes = database.connectSQL("paciente_telefono");
            enfRes = database.connectSQL("expediente_enfermedad");
            medRes = database.connectSQL("expediente_medicamentoPrescrito");
            aleRes = database.connectSQL("expediente_alergia");
        } catch (Exception e){
            e.printStackTrace();
        }

        while(myRes.next() && telRes.next() && enfRes.next() && medRes.next() && aleRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("id"))){
                nombreInput.setText(myRes.getString("nombre"));
                apellidopInput.setText(myRes.getString("nomPaterno"));
                apellidomInput.setText(myRes.getString("nomMaterno"));
                tel1Input.setText(telRes.getString("numTelefono"));
                nombreInput.setText(myRes.getString("nombre"));
                break;
            }
        }
    }

    public void saveItem(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("paciente");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;

            int size = 1;
            while (myRes.next()){
                size++;
            }
            if (notfound){
                try{
                    String sql = "update paciente set nombre = ?, precio = ?, descripcion = ?"
                            +" where clave = ?";
                    String id = motor.getSelectedItem();
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1,nombreInput.getText());
                    stmt.setString(4,id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showPacientes(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegister(ActionEvent event) {
        motor.showPacientes(event);
    }
}
