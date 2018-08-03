package metronomePackage;

import java.sql.*;
import java.util.ArrayList;

public class DataBaseConnect {
    private final String JDBC_DRIVER ="org.apache.derby.JDBC.EmbeddedDriver";
    private final String URL="jdbc:derby:metronomeDB;create=true";
    private final String USERNAME="";
    private final String PASSWORD="";
    private final String tableName = "parameters";

    private Connection conn=null;
    private Statement createStatement=null;
    private DatabaseMetaData metaData=null;

    public DataBaseConnect() {
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("ok");
        } catch (SQLException e) {
            System.out.println("Database is not available.");
        }

        if(conn!=null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException e) {
                System.out.println("Error" + e);
            }
        }

        try {
            metaData = conn.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet resultSet = metaData.getTables(null, "App", "Parameters", null);
            if (!resultSet.next()) {
                createStatement.execute("create table Parameters(name varchar(20) primary key , " +
                        "factor int, bpm int, numberofbeats int," +
                        "typeofbeats int, sound int, pitch int, volume int )");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addParameters(Parameters parameters){
        String sql = "insert into Parameters values(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareCall(sql);
            preparedStatement.setString(1, parameters.getName());
            preparedStatement.setInt(2, parameters.getFactor());
            preparedStatement.setInt(3, parameters.getBpm());
            preparedStatement.setInt(4, parameters.getNumberOfBeats());
            preparedStatement.setInt(5, parameters.getTypeOfBeats());
            preparedStatement.setInt(6, parameters.getSound());
            preparedStatement.setDouble(7, parameters.getPitch());
            preparedStatement.setDouble(8, parameters.getVolume());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }


    }

    public ArrayList<Parameters> getAllParameters() {
        String sql = "select * from parameters";
        ArrayList<Parameters> parameterSets = null;
        try {
            ResultSet resultSet = createStatement.executeQuery(sql);
            parameterSets = new ArrayList<>();

            while (resultSet.next()) {
                Parameters actualSets = new Parameters(resultSet.getString("name"), resultSet.getInt("factor"),
                        resultSet.getInt("bpm"), resultSet.getInt("numberOfBeats"),
                        resultSet.getInt("typeOfBeats"), resultSet.getInt("sound"),
                        resultSet.getDouble("pitch"), resultSet.getDouble("volume"));
                parameterSets.add(actualSets);
            }
        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return parameterSets;
    }

    public Parameters getParameters(String selectedName){
        String sql = "select * from Parameters where name =" + "'" + selectedName + "'";
        try {
            ResultSet resultSet = createStatement.executeQuery(sql);
            if (resultSet.next()) {
                Parameters set = new Parameters(resultSet.getString("name"), resultSet.getInt("factor"),
                        resultSet.getInt("bpm"), resultSet.getInt("numberOfBeats"),
                        resultSet.getInt("typeOfBeats"), resultSet.getInt("sound"),
                        resultSet.getDouble("pitch"), resultSet.getDouble("volume"));
                return set;
            }
            else return null;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }



    }

    public void recordDelete(String newName){
        try {
            createStatement = conn.createStatement();
            createStatement.execute("delete from Parameters where name=" + "'" + newName + "'" );
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
