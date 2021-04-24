package viewControllers;

import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Motor motor = new Motor();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        Login controller = loader.<Login>getController();
        controller.receiveMotorInstance(motor);


        primaryStage.setTitle("Pies Sanos DB");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {

        //Connection to database
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/practica6","root","pass");

            Statement myStmt = myConn.createStatement();

            ResultSet myRs = myStmt.executeQuery("select * from alumno");

            while(myRs.next()){
                System.out.println(myRs.getString("matricula"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        launch(args);
    }
}
