package viewControllers;

import Model.Cita;
import Model.Medicamento;
import Model.Medico;
import Model.Promocion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Promociones implements Initializable {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private VBox itemsLayout;
    @FXML
    private TextField searchInput;
    private List<Promocion> itemList = new ArrayList<>();
    private List<Promocion> items = null;
    private List<Promocion> searchList = new ArrayList<>();
    private Listener listener;
    private String name;

    public Promociones(){

    }

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            items();
            listener = new Listener(){
                @Override
                public void editListener(String id, ActionEvent event){
                    setChosenItem(id);
                    motor.showEditPromocion(event);
                }
                @Override
                public void deleteListener(String id, ActionEvent event) throws SQLException{

                    setChosenItem(id);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("¿Está seguro que desea eliminar a: "+findItem(id)+"?");

                    if (alert.showAndWait().get() == ButtonType.OK){
                        System.out.println("cita eliminado");
                        deleteItem(id, event);
                    }

                }

                @Override
                public void showListener(String id, ActionEvent event) {

                }

                @Override
                public void selectListener(String id,  boolean isSelected, ActionEvent event) {

                }
            };
            loadItems(itemList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setChosenItem(String id){
        System.out.println("selected: "+ id);
        motor.setSelectedItem(id);
    }
    private List<Promocion> items() throws SQLException {

        ResultSet myRes = null, telRes = null;
        try {
            myRes = database.connectSQL("promocion");

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*assert telRes != null;
        assert myRes != null;*/
        while (myRes.next()) {

            String codigo = myRes.getString("codigo");
            String nombre = myRes.getString("nombre");
            String fecha = myRes.getString("fecha");
            String descuento = myRes.getFloat("porcentaje_descuento")+"%";
            String tratamiento = myRes.getString("clave_tratamiento");

            Promocion newItem = defineItem(codigo, nombre, fecha, descuento, tratamiento);
            itemList.add(newItem);
        }
        return itemList;
    }

    public Promocion defineItem(String codigo, String nombre, String fecha, String descuento, String tratamiento) {
        Promocion item = new Promocion();
        item.setCodigo(codigo);
        item.setNombre(nombre);
        item.setFecha(fecha);
        item.setDescuento(descuento);
        item.setClave_tratamiento(tratamiento);
        return item;
    }

    public void loadItems(List array) throws SQLException {

        items = new ArrayList<>(array);
        for (int i = 0; i < items.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("PromocionItem.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                PromocionItem mi = fxmlLoader.getController();
                mi.setData(items.get(i),listener);
                itemsLayout.getChildren().add(hbox);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerItem(ActionEvent event) {
        motor.showRegisterPromocion(event);
    }

    public void backHome(ActionEvent event) {
        motor.showClient(event);
    }

    public void searchItem(ActionEvent event) throws SQLException {
        String search = searchInput.getText();
        if (searchInput.equals("")) {

        } else {
            boolean found = false;
            searchList.clear();
            itemsLayout.getChildren().clear();
            for (int i = 0; i < itemList.size(); i++) {
                if (search.contains(itemList.get(i).getClave_tratamiento()) || search.contains(itemList.get(i).getCodigo()) || search.contains(itemList.get(i).getNombre())) {
                    found = true;
                    Promocion foundItem = itemList.get(i);
                    searchList.add(foundItem);
                }
            }
            loadItems(searchList);
            if (!found){
                System.out.println("no matches found");
            }
        }
    }

    public void deleteSearch(MouseEvent mouseEvent) throws SQLException {
        searchInput.clear();
        itemsLayout.getChildren().clear();
        loadItems(itemList);
    }

    public void deleteItem(String id, ActionEvent event) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("promocion");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (myRes.next()) {
            if (id.equals(myRes.getString("codigo"))){
                try{
                    String sql = "delete from promocion where codigo= ? ";
                    PreparedStatement stmt = database.updateData(sql);
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    motor.showPromocion(event);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    public String findItem(String id) throws SQLException {
        ResultSet myRes = null;
        try {
            myRes = database.connectSQL("promocion");
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(myRes.next()){
            if (id.equals(myRes.getString("codigo"))){
                name = myRes.getString("codigo");
                break;
            }
        }
        return name;
    }

    public void displayProveedores(ActionEvent event) {

    }
}
