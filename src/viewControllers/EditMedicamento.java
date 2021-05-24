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

public class EditMedicamento {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, precioInput, cantidadInput, descripcionInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public EditMedicamento(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("medicamento");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(myRes.getString("codigo"))){
                nombreInput.setText(myRes.getString("nombre"));
                String precio = myRes.getString("precio");
                precio = precio.substring(0, precio.length() - 2);
                precioInput.setText(precio);
                cantidadInput.setText(myRes.getString("cantidadinventario"));
                descripcionInput.setText(myRes.getString("descripcion"));
                break;
            }
        }
    }

    public void saveRegistrarMedicamento(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("") || precioInput.getText().equals("") || cantidadInput.getText().equals("") || descripcionInput.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("medicamento");
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
                if (!StringUtils.isStrictlyNumeric(cantidadInput.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Solo valores numéricos para campo cantidad\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input cantidad");
                }
            }
            if (notfound){
                try{
                    String sql = "update medicamento set nombre = ?, precio = ?, cantidadinventario = ?, descripcion = ?"
                            +" where codigo = ?";
                    String id = motor.getSelectedItem();
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1,nombreInput.getText());
                    stmt.setFloat(2, Float.parseFloat(precioInput.getText()));
                    stmt.setInt(3, Integer.parseInt(cantidadInput.getText()));
                    stmt.setString(4,descripcionInput.getText());
                    stmt.setString(5,id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showMedicamentos(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegisterMedicamento(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showMedicamentos(event);
        }
    }
}
