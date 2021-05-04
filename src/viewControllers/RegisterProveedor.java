package viewControllers;

import com.mysql.cj.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class RegisterProveedor {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput;

    @FXML
    private TextField calleInput;

    @FXML
    private TextField num_extInput;

    @FXML
    private TextField num_intInput;

    @FXML
    private TextField coloniaInput;

    @FXML
    private TextField cpInput;

    @FXML
    private TextField ciudadInput;
    @FXML
    private Group alertGroup;

    @FXML
    private Label alertText;

    @FXML
    private Group requiredGroup;


    public RegisterProveedor(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    public void saveRegisterProveedor(ActionEvent event) throws SQLException {
        alertText.setText("");
        if (nombreInput.getText().equals("") || calleInput.getText().equals("") || num_extInput.getText().equals("") || coloniaInput.getText().equals("") || cpInput.getText().equals("") || ciudadInput.getText().equals("")){
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
            boolean out = false;

            int size = 1;
            while(myRes.next()){
                size++;
                String nombre = myRes.getString("nombre");
                if(nombre.equals(nombreInput.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Nombre de proveedor existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("nombre de proveedor existente");
                }
            }
            if (notfound){
                if (num_intInput.getText().equals("")){
                    num_intInput.setText("");
                }
                try{
                    String sql = "insert into proveedor "+"(id, nombre, calle, num_ext, num_int, colonia, codigopostal, ciudad)"
                            +" values (?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, ("PVD-"+size));
                    stmt.setString(2, nombreInput.getText());
                    stmt.setString(3, calleInput.getText());
                    stmt.setString(4, num_extInput.getText());
                    stmt.setString(5, num_intInput.getText());
                    stmt.setString(6, coloniaInput.getText());
                    stmt.setString(7, cpInput.getText());
                    stmt.setString(8, ciudadInput.getText());
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

    public void cancelRegisterProveedor(ActionEvent event) {
        motor.showProveedores(event);
    }
}
