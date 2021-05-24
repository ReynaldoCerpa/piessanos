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

public class RegisterTratamientos {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, precioInput, cantidadInput;
    @FXML
    private TextArea descripcionInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public RegisterTratamientos(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void saveRegistrarTratamiento(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("") || precioInput.getText().equals("") || descripcionInput.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("tratamiento");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out = false, out2 = false, out3 = false;

            int size = 1;
            while(myRes.next()){
                size++;
                String nombre = myRes.getString("nombre");
                if(nombre.equals(nombreInput.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Nombre de tratamiento existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("nombre de tratamiento existente");
                }
                if (!StringUtils.isStrictlyNumeric(precioInput.getText()) && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Solo valores numéricos para campo precio\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input precio");
                }
            }
            if (notfound){
                try{
                    String clave = motor.generateID("TRT-",size);
                    String sql = "insert into tratamiento "+"(clave, nombre, precio, descripcion)"
                            +" values (?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, clave);
                    stmt.setString(2,nombreInput.getText());
                    stmt.setFloat(3, Float.parseFloat(precioInput.getText()));
                    stmt.setString(4,descripcionInput.getText());
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showTratamientos(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegisterTratamiento(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showTratamientos(event);
        }
    }
}
