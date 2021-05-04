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

public class RegisterMedicamentos {
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

    public RegisterMedicamentos(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
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
            boolean out = false, out2 = false, out3 = false;

            int size = 1;
            while(myRes.next()){
                size++;
                String nombre = myRes.getString("nombre");
                if(nombre.equals(nombreInput.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Nombre de medicamento existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("nombre de medicamento existente");
                }
                if (!StringUtils.isStrictlyNumeric(precioInput.getText()) && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Solo valores numéricos para campo precio\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input precio");
                }
                if (!StringUtils.isStrictlyNumeric(cantidadInput.getText()) && !out3){
                    notfound = false;
                    out3 = true;
                    alertText.setText(alertText.getText() + "Solo valores numéricos para campo cantidad\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input cantidad");
                }
            }
            if (notfound){
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                formatter.format(date);
                System.out.println(date);
                try{
                    String sql = "insert into medicamento "+"(codigo, nombre, precio, cantidadinventario, descripcion)"
                            +" values (?,?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, ("MDT-"+size));
                    stmt.setString(2,nombreInput.getText());
                    stmt.setFloat(3, Float.parseFloat(precioInput.getText()));
                    stmt.setInt(4, Integer.parseInt(cantidadInput.getText()));
                    stmt.setString(5,descripcionInput.getText());
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
