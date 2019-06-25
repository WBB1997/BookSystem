package util;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class MyUitl {
    public static String DealWithErrMesage(String message){
        String[] strings = message.split("\\$");
        if(strings.length < 3)
            return message;
        else
            return strings[1];
    }

    public static int count(String tableName) throws SQLException {
        Connection connection = DatabaseBean.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{ ?=call count_Data_Of_Table(?)}");
        callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
        callableStatement.setString(2, tableName);
        callableStatement.execute();
        int count = callableStatement.getInt(1);
        DatabaseBean.close(null, callableStatement, connection);
        return count;
    }
}
