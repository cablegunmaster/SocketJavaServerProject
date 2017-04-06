package Controller;

/**
 * Created by jasper wil.lankhorst on 12-3-2017.
 */
public interface commandModule {

    /**
     * Created by jasper wil.lankhorst on 20-12-2016.
     * ingame Moves.
     */
    void sendMove(int move); //send a move to server.
    void sendWin(); //send win to server. (server checks if Human won or not.)

    //Chat move
    void sendChat(String message); //sends a chat message.

    void receiveChat(String message); //receives PM message from other person.

    //Duel request //Host moves.
    void requestDuel(String playerName, String game); //request a duel from client to playername.

    void acceptDuel(String duelRequestName); //Accept duelrequest from client to Human X. -> Starts the game at both ends?

    void cancelRequestDuel(String duelRequestName); //cancel duelrequest from client to Human X.

    //extra functions.
    void help(); //gives rules / information about the current game.

    void quit(); //removes module from the board , returning to mainscreen.

    //gives a current status of the Human , this message could be  //playing game X or idle.
    void status(String playerName); //sends current status of the Human to other people.

}
