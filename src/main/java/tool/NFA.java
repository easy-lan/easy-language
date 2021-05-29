package tool;


import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class NFA {

    private Set<State> startState = null;
    private Set<State> finalState = null;
    private Set<State> allStates = null;
    private Set<Move> moves = null;

    protected void beforeCompileCheck() throws NotConfiguredException, StateNotFoundException{
        if(startState == null || startState.size()==0){
            throw new NotConfiguredException("StartState Not Configured.");
        }
        else if(finalState == null || finalState.size()==0){
            throw new NotConfiguredException("FinalState Not Configured.");
        }else if(allStates == null){
            throw new NotConfiguredException("AllStates Not Configured.");
        }else if(moves == null){
            throw new NotConfiguredException("Moves Not Configured.");
        }
        for(Move m : moves){
            if((!allStates.contains(m.getStart())) || (!allStates.contains(m.getEnd()))){
                throw new StateNotFoundException("State in move not found.");
            }
        }

    }

    public NFA(){

    }

    public void setAllState(Set<State> allStates) {
        this.allStates = allStates;
    }

    public void setFinalState(State... finalState) {

        for(State s : finalState){
            this.finalState.add(s);
        }
    }

    public void setFinalState(Set<State> finalState) {
        this.finalState = finalState;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    public void setStartState(Set<State> startState) {
        this.startState = startState;
    }

    public void setStartState(State... startState) {

        for(State s : startState){
            this.startState.add(s);
        }

    }

    public Set<State> getStartState() {
        return startState;
    }

    public Set<State> getFinalState() {
        return finalState;
    }

    public Set<State> getAllState() {
        return allStates;
    }

    public Set<Move> getMoves() {
        return moves;
    }

    protected class NotConfiguredException extends Exception{
        public NotConfiguredException(){}
        public NotConfiguredException(String msg){super(msg);}
    }

    protected class StateNotFoundException extends Exception{
        public StateNotFoundException(){}
        public StateNotFoundException(String msg){super(msg);}
    }

    public void addState(State state){

        if(this.allStates == null){
            this.allStates = new HashSet<>();
        }
        this.allStates.add(state);
    }

    public void addState(Set<State> state){

        if(this.allStates == null){
            this.allStates = new HashSet<>();
        }
        this.allStates.addAll(state);
    }

    public void addState(Integer number){
        this.addState(new State(number));
    }

    public void addState(Integer... number){
        Set<State> states = new HashSet<>();
        for(Integer i : number){
            states.add(new State(i));
        }
        this.addState(states);
    }

    public void addMove(Move move){
        if(this.moves == null){
            this.moves = new HashSet<>();
        }
        if(this.allStates == null){
            this.allStates = new HashSet<>();
        }
        this.moves.add(move);
        this.allStates.add(move.getEnd());
        this.allStates.add(move.getStart());
    }

    public void addMove(Set<Move> moveSet){
        if(this.moves == null){
            this.moves = new HashSet<>();
        }
        if(this.allStates == null){
            this.allStates = new HashSet<>();
        }
        for(Move move : moveSet){
            this.moves.add(move);
            this.allStates.add(move.getEnd());
            this.allStates.add(move.getStart());
        }

    }

    public void addMove(Integer start, Integer end, Object condition){
        this.addMove(new Move(new State(start), new State(end), condition));
    }

    public void addStartState(State start){
        if(this.startState == null){
            this.startState = new HashSet<>();
        }

        this.startState.add(start);
        this.addState(start);
    }

    public void addStartState(Set<State> start){
        if(this.startState == null){
            this.startState = new HashSet<>();
        }

        this.startState.addAll(start);
        this.addState(start);
    }

    public void addStartState(Integer number){

        this.addStartState(new State(number));
    }

    public void addStartState(Integer... number){
        Set<State> states = new HashSet<>();
        for(Integer i : number){
            states.add(new State(i));
        }
        this.addStartState(states);
    }

    public void addFinalState(State end){
        if(this.finalState == null){
            this.finalState = new HashSet<>();
        }

        this.finalState.add(end);
        this.addState(end);
    }

    public void addFinalState(Set<State> end){
        if(this.finalState == null){
            this.finalState = new HashSet<>();
        }

        this.finalState.addAll(end);
        this.addState(end);
    }

    public void addFinalState(Integer number){
        this.addFinalState(new State(number));

    }

    public void addFinalState(Integer... number){
        Set<State> states = new HashSet<>();
        for(Integer i : number){
            states.add(new State(i));
        }
        this.addFinalState(states);
    }

    public void clean(){

        this.allStates = new HashSet<>();
        this.moves = new HashSet<>();
        this.startState = new HashSet<>();
        this.finalState = new HashSet<>();
    }
}
