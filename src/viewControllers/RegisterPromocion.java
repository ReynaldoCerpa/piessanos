package viewControllers;

import Model.Cita;
import com.mysql.cj.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterPromocion implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, descuentoInput;
    @FXML
    private DatePicker fechaInput;
    @FXML
    private Label alertText, selectedItemLabel;
    @FXML
    private Group alertGroup, requiredGroup;
    @FXML
    private ListView<String> itemList;
    private ArrayList<String> idArray = new ArrayList<>();
    private ArrayList<String> nameArray = new ArrayList<>();
    private String id = "", name = "";

    public RegisterPromocion(){

    }

    public void receiveMotorInstance(Motor m){
        this.motor = m;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadItems();
            itemList.getItems().addAll(nameArray);
            itemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                    String item = itemList.getSelectionModel().getSelectedItem();
                    for (int i = 0; i < nameArray.size(); i++){
                        if (item.equals(nameArray.get(i))){
                            id = idArray.get(i);
                            break;
                        }
                    }
                    try {
                        selectedItemLabel.setText(findItem(id));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveItem(ActionEvent event) throws SQLException {

        alertText.setText("");
        if (fechaInput.getValue() == null || nombreInput.getText().isEmpty() || descuentoInput.getText().isEmpty() || selectedItemLabel.getText().isEmpty()){
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
            boolean out = false, out2 = false, out3 = false, out4 = false, out5 = false;

            int size = 1;
            while(myRes.next()){
                size++;
                String nombre = myRes.getString("nombre");

                if (nombre.equals(nombreInput) && !out5){
                    notfound = false;
                    out5 = true;
                    alertText.setText(alertText.getText() + "Nombre en uso\n");
                    alertGroup.setVisible(true);
                    System.out.println("Nombre en uso");
                }
                if (!StringUtils.isStrictlyNumeric(descuentoInput.getText()) || (Integer.parseInt(descuentoInput.getText()) < 0) || (Integer.parseInt(descuentoInput.getText()) >100) && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Dato invalido para campo descuento\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input descuento");
                }

            }
            if (notfound){
                try{
                    String codigo = motor.generateID("PRM-",size);
                    String date = motor.formatDate(fechaInput);
                    String sql = "insert into promocion "+"(codigo, nombre, porcentaje_descuento, fecha, clave_tratamiento)"
                            +" values (?,?,?,?,(select clave from tratamiento where clave=?))";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, codigo);
                    stmt.setString(2, nombreInput.getText());
                    stmt.setFloat(3, Float.parseFloat(descuentoInput.getText()));
                    stmt.setDate(4, Date.valueOf(date));
                    stmt.setString(5, id);
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
        motor.showPromocion(event);
    }

    public void loadItems() throws SQLException {
        ResultSet myRes = null;
        String id, name;
        try{
            myRes = database.connectSQL("tratamiento");
        } catch (Exception e){
            e.printStackTrace();
        }
        while (myRes.next()){
            id = myRes.getString("clave");
            name = myRes.getString("clave")+"      "+myRes.getString("nombre")+"   $"+myRes.getString("precio");
            idArray.add(id);
            nameArray.add(name);
        }
    }

    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try{
            myRes = database.connectSQL("tratamiento");
        } catch (Exception e){
            e.printStackTrace();
        }
        while (myRes.next()){
            if (id.equals(myRes.getString("clave"))){
                name = myRes.getString("clave");
            }
        }
        return name;
    }
}
