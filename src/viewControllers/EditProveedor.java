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

public class EditProveedor {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, calleInput, numExtInput, numIntInput, coloniaInput, cpInput, ciudadInput, telefonoInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public EditProveedor(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("proveedor");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("id"))){
                nombreInput.setText(myRes.getString("nombre"));
                calleInput.setText(myRes.getString("calle"));
                numExtInput.setText(myRes.getString("num_ext"));
                numIntInput.setText(myRes.getString("num_int"));
                coloniaInput.setText(myRes.getString("colonia"));
                cpInput.setText(myRes.getString("codigopostal"));
                ciudadInput.setText(myRes.getString("ciudad"));
                telefonoInput.setText(myRes.getString("telefono"));
                break;
            }
        }
    }

    public void saveEditProveedor(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("") || calleInput.getText().equals("") || numExtInput.getText().equals("") || coloniaInput.getText().equals("") || cpInput.getText().equals("") || ciudadInput.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("proveedor");
            } catch (Exception e){
                e.printStackTrace();
            }

            boolean notfound = true;
            boolean out = false, out2 = false, out3 = false;

            while(myRes.next()){

                String nombre = myRes.getString("nombre");
                if (numIntInput.getText().length() > 5 || numExtInput.getText().length() > 5 && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Numeros interior/exterior incorrectos\n");
                    alertGroup.setVisible(true);
                    System.out.println("proveedor name already taken");
                }
                if (telefonoInput.getText().length() != 10 && !out3){
                    notfound = false;
                    out3 = true;
                    alertText.setText(alertText.getText() + "Solo telefonos de 10 digitos\n");
                    alertGroup.setVisible(true);
                    System.out.println("solo telefonos de 10 digitos");
                }
            }
            if (notfound){
                try{
                    String sql = "update proveedor set nombre = ?, calle = ?, num_ext = ?, num_int = ?, colonia = ?, codigopostal = ?, ciudad = ?, telefono = ?"
                            +" where id = ?";
                    String id = motor.getSelectedItem();
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, nombreInput.getText());
                    stmt.setString(2, calleInput.getText());
                    stmt.setString(3, numExtInput.getText());
                    stmt.setString(4, numIntInput.getText());
                    stmt.setString(5, coloniaInput.getText());
                    stmt.setString(6, cpInput.getText());
                    stmt.setString(7, ciudadInput.getText());
                    stmt.setString(8, telefonoInput.getText());
                    stmt.setString(9, id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showProveedores(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelEditProveedor(ActionEvent event) {
        motor.showProveedores(event);
    }
}
