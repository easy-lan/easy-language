package tool;

public class Move{
    private State start;
    private State end;
    private Object condition;

    public Move(State start, State end, Object condition){
        this.start = start;
        this.end = end;
        this.condition = condition;
    }

    public Object getCondition() {
        return condition;
    }

    public State getEnd() {
        return end;
    }

    public State getStart() {
        return start;
    }

    public void setCondition(Object condition) {
        this.condition = condition;
    }

    public void setEnd(State end) {
        this.end = end;
    }

    public void setStart(State start) {
        this.start = start;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Move))return false;
        Move m = (Move)obj;
        if(((m.getCondition() == null && this.condition == null) || (m.getCondition().equals(this.condition)))
                &&
                m.getEnd().equals(this.end)
                &&
                m.getStart().equals(this.start)){
            return true;

        }
        return false;
    }

    @Override
    public int hashCode() {
        return start.hashCode() << 16 + end.hashCode() << 8;
    }
}
