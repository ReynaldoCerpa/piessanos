package viewControllers;

import com.mysql.cj.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterMateriales implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, precioproveedorInput, cantidadInput;
    @FXML
    private TextArea descripcionInput;
    @FXML
    private Label alertText, selectedItemLabel;
    @FXML
    private Group alertGroup, requiredGroup;
    @FXML
    private ListView<String> itemList;
    private ArrayList<String> idArray = new ArrayList<>();
    private ArrayList<String> nameArray = new ArrayList<>();
    private String a_domicilio = "0";
    private String id = "", name = "";

    public RegisterMateriales(){

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

    public void loadItems() throws SQLException {
        ResultSet myRes = null;
        String id, name;
        try{
            myRes = database.connectSQL("proveedor");
        } catch (Exception e){
            e.printStackTrace();
        }
        while (myRes.next()){
            id = myRes.getString("id");
            name = myRes.getString("id")+"      "+myRes.getString("nombre");
            idArray.add(id);
            nameArray.add(name);
        }
    }
    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try{
            myRes = database.connectSQL("proveedor");
        } catch (Exception e){
            e.printStackTrace();
        }
        while (myRes.next()){
            if (id.equals(myRes.getString("id"))){
                name = myRes.getString("nombre");
            }
        }
        return name;
    }

    public void saveRegistrarMedicamento(ActionEvent event) throws SQLException {
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
            boolean out = false, out2 = false, out3 = false;

            int size = 1;
            while(myRes.next()){
                size++;
                String nombre = myRes.getString("nombre");
                if(nombre.equals(nombreInput.getText()) && !out){
                    notfound = false;
                    out = true;
                    alertText.setText(alertText.getText() + "Nombre de material existente\n");
                    alertGroup.setVisible(true);
                    System.out.println("nombre de material existente");
                }
                if(selectedItemLabel.getText().isEmpty() && !out2){
                    notfound = false;
                    out2 = true;
                    alertText.setText(alertText.getText() + "Seleccione un proveedor\n");
                    alertGroup.setVisible(true);
                    System.out.println("Seleccione un proveedor");
                }
                if (!StringUtils.isStrictlyNumeric(precioproveedorInput.getText()) && !out3){
                    notfound = false;
                    out3 = true;
                    alertText.setText(alertText.getText() + "Solo valores numéricos para campo precio proveedor\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input precio proveedor");
                }
            }
            if (notfound){
                try{
                    String materialid  = motor.generateID("MT-",size);
                    String sql = "insert into materiales_consulta "+"(id, nombre, cantidadinventario)"
                            +" values (?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, materialid);
                    stmt.setString(2,nombreInput.getText());
                    stmt.setInt(3, Integer.parseInt(cantidadInput.getText()));
                    stmt.executeUpdate();

                    String pmquery = "insert into proveedor_materialesconsulta (precio_proveedor, fecha_venta, cantidad, id, id_proveedor)"
                            +" values (?,?,?,(select id from materiales_consulta where id=?),(select id from proveedor where id=?))";
                    PreparedStatement pmstmt = database.updateData(pmquery);
                    pmstmt.setFloat(1, Float.parseFloat(precioproveedorInput.getText()));
                    pmstmt.setDate(2, Date.valueOf(motor.formatCurrDate()));
                    pmstmt.setInt(3, Integer.parseInt(cantidadInput.getText()));
                    pmstmt.setString(4, materialid);
                    pmstmt.setString(5, id);
                    pmstmt.executeUpdate();

                    alertText.setVisible(false);
                    requiredGroup.setVisible(false);

                    motor.showMateriales(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelRegisterMedicamento(ActionEvent event) {
        String text = "¿Está seguro que desea cancelar el registro?";
        if (motor.confirmAction(text, "")){
            motor.showMateriales(event);
        }
    }
}
