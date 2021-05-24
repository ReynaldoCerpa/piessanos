package viewControllers;

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

public class EditPromocion {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, descuentoInput;
    @FXML
    private Label alertText, selectedItemLabel;
    @FXML
    private Group alertGroup, requiredGroup;
    @FXML
    private DatePicker fechaInput;

    public EditPromocion(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("promocion");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("codigo"))){

                String id = myRes.getString("clave_tratamiento");
                String pacientequery = "select * from tratamiento where clave = ?";
                PreparedStatement paciente = database.updateData(pacientequery);
                paciente.setString(1, id);
                ResultSet nombreItem = paciente.executeQuery();

                String item= "";
                while(nombreItem.next()) {
                    item = nombreItem.getString("clave")+" "+nombreItem.getString("nombre");
                }
                selectedItemLabel.setText(item);
                nombreInput.setText(myRes.getString("nombre"));
                descuentoInput.setText(myRes.getString("porcentaje_descuento"));
                break;
            }
        }
    }

    public void saveItem(ActionEvent event) throws SQLException {
        String date = motor.formatDate(fechaInput);
        alertText.setText("");
        if (nombreInput.getText().isEmpty() || descuentoInput.getText().isEmpty()){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("promocion");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out = false;

            int size = 1;
            while (myRes.next()){
                size++;
            }
            if (notfound){
                try{
                    String sql = "update promocion set nombre = ?, fecha = ?, porcentaje_descuento = ?"
                            +" where codigo = ?";
                    String id = motor.getSelectedItem();
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, nombreInput.getText());
                    stmt.setDate(2, Date.valueOf(date));
                    stmt.setString(3, descuentoInput.getText());
                    stmt.setString(4, id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showPromocion(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegister(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showPromocion(event);
        }
    }

}
