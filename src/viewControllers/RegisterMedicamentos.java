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

public class RegisterMedicamentos implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private TextField nombreInput, precioInput, precioproveedorInput, cantidadInput;
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
    private String id = "", name = "";

    public RegisterMedicamentos(){

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
            boolean out = false, out2 = false, out3 = false, out4 = false, out5 = false;

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
                if(selectedItemLabel.getText().isEmpty() && !out4){
                    notfound = false;
                    out4 = true;
                    alertText.setText(alertText.getText() + "Seleccione un proveedor\n");
                    alertGroup.setVisible(true);
                    System.out.println("Seleccione un proveedor");
                }
                if (!StringUtils.isStrictlyNumeric(precioproveedorInput.getText()) && !out5){
                    notfound = false;
                    out5 = true;
                    alertText.setText(alertText.getText() + "Solo valores numéricos para campo precio proveedor\n");
                    alertGroup.setVisible(true);
                    System.out.println("invalid data input precio proveedor");
                }
            }
            if (notfound){
                try{
                    String codigo = motor.generateID("MDT-",size);
                    String sql = "insert into medicamento "+"(codigo, nombre, precio, cantidadinventario, descripcion)"
                            +" values (?,?,?,?,?)";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, codigo);
                    stmt.setString(2,nombreInput.getText());
                    stmt.setFloat(3, Float.parseFloat(precioInput.getText()));
                    stmt.setInt(4, Integer.parseInt(cantidadInput.getText()));
                    stmt.setString(5,descripcionInput.getText());
                    stmt.executeUpdate();

                    String pmquery = "insert into proveedor_medicamento "+"(precio_proveedor, fecha_venta, cantidad, codigo_medicamento, id_proveedor)"
                            +" values (?,?,?,(select codigo from medicamento where codigo=?),(select id from proveedor where id=?))";
                    PreparedStatement pmstmt = database.updateData(pmquery);
                    pmstmt.setFloat(1, Float.parseFloat(precioproveedorInput.getText()));
                    pmstmt.setDate(2, Date.valueOf(motor.formatCurrDate()));
                    pmstmt.setInt(3, Integer.parseInt(cantidadInput.getText()));
                    pmstmt.setString(4, codigo);
                    pmstmt.setString(5, id);
                    pmstmt.executeUpdate();

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
