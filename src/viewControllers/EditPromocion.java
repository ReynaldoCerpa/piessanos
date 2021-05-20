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
    private TextField nombreInput, precioInput, cantidadInput, descripcionInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public EditPromocion(){

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
                precioInput.setText(myRes.getString("precio"));
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
            boolean out = false;

            int size = 1;
            while (myRes.next()){
                size++;
            }
            while(myRes.next()){

                String nombre = myRes.getString("nombre");


                if(nombre.equals(nombreInput.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Nombre de usuario existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("username already taken");
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
        motor.showMedicamentos(event);
    }
}
