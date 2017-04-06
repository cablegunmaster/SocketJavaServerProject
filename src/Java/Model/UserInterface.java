package Java.Model;

/**
 * Created by jasper wil.lankhorst on 6-3-2017.
 */
public interface UserInterface {
    String getNickname();
    int getMatchStatus(); // 0 is free 1 is occupied. 2 is requesting a match (auto accept?)
}
