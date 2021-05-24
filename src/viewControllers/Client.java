package viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private Motor motor;
    private SQLconnector database = new SQLconnector();
    @FXML
    private Group promocionGroup;
    @FXML
    private Label promocionItemsLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button administrarMedicosButton, tratamientosButton, promocionesButton;
    @FXML
    private ImageView tratamientoicon, promocionicon;

    public void receiveMotorInstance(Motor m) throws SQLException {
        this.motor = m;
        ResultSet myRes = null, promos = null;
        try{
            myRes = database.connectSQL("medico");
            promos = database.connectSQL("promocion");
            myRes.next();
        } catch (Exception e){
            e.printStackTrace();
        }
        String name = motor.getNombre()+" "+motor.getApellidop()+" "+motor.getApellidom();
        usernameLabel.setText(name);

        if (!motor.isAdmin()){
            promocionicon.setVisible(false);
            tratamientoicon.setVisible(false);
            administrarMedicosButton.setVisible(false);
            tratamientosButton.setVisible(false);
            promocionesButton.setVisible(false);
        }
        if (!motor.dontShow()) {
            String datequery = "select fecha from promocion where fecha = ? limit 1";
            PreparedStatement dateq = database.updateData(datequery);
            dateq.setString(1, String.valueOf(motor.formatCurrDate()));
            ResultSet promodate = dateq.executeQuery();
            String date = "";
            while (promodate.next()) {
                System.out.println("printed fecha");
                date = promodate.getString("fecha");
                String promoitem = "select * from promocion where fecha = ?";
                PreparedStatement itemStmt = database.updateData(promoitem);
                itemStmt.setString(1, date);
                ResultSet getItems = itemStmt.executeQuery();
                while (getItems.next()) {
                    promocionGroup.setVisible(true);
                    String item = getItems.getString("clave_tratamiento");
                    System.out.println("added "+item);
                    String itemname = getItems.getString("codigo") + "   " + getItems.getString("nombre");
                    motor.addPromoItem(item);
                    promocionItemsLabel.setText(promocionItemsLabel.getText() + itemname + "\n");
                    Timer timer = new Timer();
                    TimerTask tarea = new TimerTask() {
                        @Override
                        public void run() {
                            promocionGroup.setVisible(false);
                            timer.cancel();
                        }
                    };
                    timer.schedule(tarea, 10000);
                }

            }
            motor.setDontShow(true);
        }

    }

    public void logout(ActionEvent event) {
        String text = "¿Está seguro que desea cerrar sesión?";
        if (motor.confirmAction(text, "")){
            motor.setDontShow(false);
            motor.showLogin(event);
        }
    }

    public void displayPacients(ActionEvent event) {
        motor.showPacientes(event);
    }

    public void displayPromociones(ActionEvent event) {
        motor.showPromocion(event);
    }

    public void displayInventario(ActionEvent event) {
        motor.showInventory(event);
    }

    public void displayExpedientes(ActionEvent event) {
        motor.showExpedienteUser(event);
    }

    public void displayCitas(ActionEvent event) {
        motor.showCita(event);
    }

    public void displayTratamientos(ActionEvent event) {
        motor.showTratamientos(event);
    }

    public void displayAdministrarMedicos(ActionEvent event) {
        motor.showAdministrarMedicos(event);
    }

    public void closePromocionesPopup(ActionEvent event) {
        promocionGroup.setVisible(false);
    }

}
