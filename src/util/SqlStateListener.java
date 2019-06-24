package util;

public interface SqlStateListener {
    void Error(int ErrorCode, String ErrorMessage);

    void Correct();
}
