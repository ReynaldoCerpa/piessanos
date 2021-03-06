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
    private String currentTel = "";

    public EditPaciente(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {

        this.motor = m;

        ResultSet myRes = null, telRes = null, enfRes = null, medRes = null, aleRes = null;
        try{
            myRes = database.connectSQL("paciente");
            telRes = database.connectSQL("paciente_telefono");
        } catch (Exception e){
            e.printStackTrace();
        }

        while(myRes.next() && telRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("id"))){
                nombreInput.setText(myRes.getString("nombre"));
                apellidopInput.setText(myRes.getString("nomPaterno"));
                apellidomInput.setText(myRes.getString("nomMaterno"));
                tel1Input.setText(telRes.getString("numTelefono"));
                currentTel = telRes.getString("numTelefono");
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
            ResultSet myRes = null, telRes = null;
            try{
                myRes = database.connectSQL("paciente");
                telRes = database.connectSQL("paciente_telefono");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out2 = false, out = false;

            int size = 1;
            while (myRes.next() && telRes.next()){
                size++;
                String tel = telRes.getString("numTelefono");
                if (tel1Input.getText().equals(tel) && !tel1Input.getText().equals(currentTel) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Telefono ya existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("Telefono ya existente");
                }
                if (tel1Input.getText().length() != 10 && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Solo telefonos de 10 digitos\n");
                    alertGroup.setVisible(true);
                    System.out.println("solo telefonos de 10 digitos");
                }
            }
            if (notfound){
                try{
                    String sql = "update paciente set nombre = ?, nomPaterno = ?, nomMaterno = ?"
                            +" where id = ?";
                    PreparedStatement stmt = database.updateData(sql);
                    String id = motor.getSelectedItem();
                    stmt.setString(1, nombreInput.getText());
                    stmt.setString(2, apellidopInput.getText());
                    stmt.setString(3, apellidomInput.getText());
                    stmt.setString(4, id);
                    stmt.executeUpdate();

                    String telefono = "update paciente_telefono set numTelefono = ?, tipo = ? "
                            + "where id_paciente = ?";
                    PreparedStatement telStmt = database.updateData(telefono);
                    telStmt.setString(1, tel1Input.getText());
                    telStmt.setString(2, "celular");
                    telStmt.setString(3, id);
                    telStmt.executeUpdate();

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
