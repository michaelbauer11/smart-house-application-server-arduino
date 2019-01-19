package com.example.owner.smarthouse;

import android.app.Application;

/**
 * Created by Owner on 11/05/2017.
 */
public class Session extends Application {
    Connection connection;
    public Session(Connection c){
        connection = c;
    }
    public Session(){
        connection = null;
    }
    public void setConnection(Connection connect){
        this.connection = connect;
    }
    public Connection getConnection(){
        if(connection == null){

        }
        return connection;
    }
}
