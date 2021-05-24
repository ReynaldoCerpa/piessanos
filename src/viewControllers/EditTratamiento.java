package viewControllers;

import com.mysql.cj.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class EditTratamiento {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, precioInput, descripcionInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public EditTratamiento(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("tratamiento");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("clave"))){
                nombreInput.setText(myRes.getString("nombre"));
                String descuento = myRes.getString("precio");
                descuento = descuento.substring(0, descuento.length() - 2);
                precioInput.setText(descuento);
                descripcionInput.setText(myRes.getString("descripcion"));
                break;
            }
        }
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
            boolean out = false, out2 = false;

            int size = 1;
            while (myRes.next()){
                size++;
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
                    String sql = "update tratamiento set nombre = ?, precio = ?, descripcion = ?"
                            +" where clave = ?";
                    String id = motor.getSelectedItem();
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1,nombreInput.getText());
                    stmt.setFloat(2, Float.parseFloat(precioInput.getText()));
                    stmt.setString(3,descripcionInput.getText());
                    stmt.setString(4,id);
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
