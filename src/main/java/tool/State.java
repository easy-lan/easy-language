package tool;

public class State{
    private Integer number;

    public State(Integer number){
        this.number = number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof State))return false;
        State s = (State)obj;
        if(this.number == s.getNumber()){
            return true;
        }
        return false;
    }

    public Integer getNumber() {
        return number;
    }
}
