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
    private TextField ciudadInput, telefonoInput;
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
        if (nombreInput.getText().isEmpty() || calleInput.getText().isEmpty() || num_extInput.getText().isEmpty() || coloniaInput.getText().isEmpty() || cpInput.getText().isEmpty() || ciudadInput.getText().isEmpty() || telefonoInput.getText().isEmpty()){
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
            boolean out = false, out2 = false, out3 = false, out4 = false;

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
                if (telefonoInput.getText().length() != 10 && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Solo telefonos de 10 digitos\n");
                    alertGroup.setVisible(true);
                    System.out.println("solo telefonos de 10 digitos");
                }
                if (num_intInput.getText().length() > 5 || num_extInput.getText().length() > 5 && !out3){
                    notfound = false;
                    out3 = true;
                    alertText.setText(alertText.getText() + "Numeros interior/exterior incorrectos\n");
                    alertGroup.setVisible(true);
                    System.out.println("proveedor name already taken");
                }
                if (!StringUtils.isStrictlyNumeric(cpInput.getText()) && !out4){
                    notfound = false;
                    out4 = true;
                    alertText.setText(alertText.getText() + "Numeros interior/exterior incorrectos\n");
                    alertGroup.setVisible(true);
                    System.out.println("proveedor name already taken");
                }
            }
            if (notfound){
                if (num_intInput.getText().equals("")){
                    num_intInput.setText("");
                }
                try{
                    String id = motor.generateID("PVD-",size);
                    String sql = "insert into proveedor "+"(id, nombre, calle, num_ext, num_int, colonia, codigopostal, ciudad, telefono)"
                            +" values (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.setString(2, nombreInput.getText());
                    stmt.setString(3, calleInput.getText());
                    stmt.setString(4, num_extInput.getText());
                    stmt.setString(5, num_intInput.getText());
                    stmt.setString(6, coloniaInput.getText());
                    stmt.setString(7, cpInput.getText());
                    stmt.setString(8, ciudadInput.getText());
                    stmt.setString(9, telefonoInput.getText());
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
