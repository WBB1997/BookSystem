package util;

public class MyUitl {
    public static String DealWithErrMesage(String message){
        String[] strings = message.split("\\$");
        if(strings.length < 3)
            return message;
        else
            return strings[1];
    }
}
