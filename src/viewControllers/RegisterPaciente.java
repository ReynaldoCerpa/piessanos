package viewControllers;

import com.mysql.cj.util.StringUtils;
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

public class RegisterPaciente {
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

    public RegisterPaciente(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void saveItem(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("") || apellidopInput.getText().equals("") || apellidomInput.getText().equals("") || tel1Input.getText().equals("") || alergiasInput.getText().equals("") || enfermedadesInput.getText().equals("") || medPrescritosInput.getText().equals("")){
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
            boolean out = false, out2 = false, out3 = false;

            int size = 1;
            while(myRes.next() && telRes.next()){
                size++;
                String tel = telRes.getString("numTelefono");
                if (tel1Input.equals(tel) && !out){
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
                    String id = "P-"+size;
                    String sql = "insert into paciente "+"(id, nombre, nomPaterno, nomMaterno)"
                            +" values (?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.setString(2, nombreInput.getText());
                    stmt.setString(3, apellidopInput.getText());
                    stmt.setString(4, apellidomInput.getText());
                    stmt.executeUpdate();

                    String telefono = "insert into paciente_telefono (numTelefono, tipo, id_paciente)"
                            + "values (?,?, (select id from paciente where id=?))";
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
