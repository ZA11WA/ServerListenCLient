package sample;

import java.io.Serializable;

public class Answer implements Serializable {
    private String answer;
    private String nick;

    public Answer(String answer, String nick){
        this.answer=answer;
        this.nick=nick;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
