package models;

public class PossibleAnswers {
    public String[] answers;

    public PossibleAnswers(String... answers) {
        this.answers = answers;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
    
    
}