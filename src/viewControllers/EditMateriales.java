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

public class EditMateriales {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, precioInput, cantidadInput, descripcionInput;
    @FXML
    private Label alertText;
    @FXML
    private Group alertGroup, requiredGroup;

    public EditMateriales(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;

        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("materiales_consulta");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(myRes.next()){
            if (motor.getSelectedItem().equals(String.valueOf(myRes.getString("numInventario")))){
                nombreInput.setText(myRes.getString("nombre"));
                cantidadInput.setText(myRes.getString("cantidadinventario"));
                break;
            }
        }
    }

    public void saveRegisterMaterial(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("") || cantidadInput.getText().equals("")){
            alertGroup.setVisible(true);
            requiredGroup.setVisible(true);
            alertText.setText("Rellene todos los campos obligatorios\n");
        }else {
            ResultSet myRes = null;
            try{
                myRes = database.connectSQL("materiales_consulta");
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
                    alertText.setText(alertText.getText() + "Nombre de material existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("nombre de material already taken");
                }
            }
            if (notfound){
                try{
                    String sql = "update materiales_consulta set nombre = ?, cantidadinventario = ?"
                            +" where numinventario = ?";
                    String id = motor.getSelectedItem();
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1,nombreInput.getText());
                    stmt.setInt(2, Integer.parseInt(cantidadInput.getText()));
                    stmt.setString(3,id);
                    stmt.executeUpdate();
                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showMateriales(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegisterMaterial(ActionEvent event) {
        motor.showMedicamentos(event);
    }
}
