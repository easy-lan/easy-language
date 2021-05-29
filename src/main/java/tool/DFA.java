package tool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DFA extends NFA{

    public DFA(NFA nfa) throws NotConfiguredException, StateNotFoundException{
        nfa.beforeCompileCheck();
        makeDFA(nfa);
    }

    public Integer match(Object[] objects, Integer start, Boolean maxMatch){
        Set<Move> moves = this.getMoves();
        Set<State> allState = this.getAllState();
        Set<State> startState = this.getStartState();
        Set<State> finalState = this.getFinalState();

        if(start >= objects.length)return objects.length;

        State nowState = startState.iterator().next();
        Integer solved = null;

        while(true){
            if(finalState.contains(nowState)){
                solved = start+1;

                if(!maxMatch)return start+1;
            }
            else if(start+1 >= objects.length){
                if(solved!=null)return solved;
                return null;
            }
            start ++;

            Object nowCondition = objects[start];

            State finalNowState = nowState;
            Set<Move> available = moves.stream().filter(
                    m ->
                    {
                        return m.getCondition().equals(nowCondition) && finalNowState.equals(m.getStart());

                    }
            ).collect(Collectors.toSet());

            if(available.size() != 0){
                nowState = available.iterator().next().getEnd();
            }
            else{
                if(solved!=null)return solved;
                return null;
            }
        }



    }

    public Integer match(Object[] objects, Boolean maxMatch){
        return this.match(objects, 0, maxMatch);
    }

    public Integer match(Object[] objects){
        return this.match(objects, 0, true);
    }

    private void makeDFA(NFA nfa){

        Map<Set<State>, State> stateMaps = new HashMap<Set<State>, State>();
        Set<State> oldStartState = nfa.getStartState();
        Set<State> oldFinalState = nfa.getFinalState();
        Set<Move> oldMoves = nfa.getMoves();
        Set<State> oldAllStates = nfa.getAllState();

        int nextStateNumber = 0;

        Set<State> newStartStateSet = new HashSet<State>();
        Set<State> newFinalStateSet = new HashSet<State>();
        Set<Move> newMoves = new HashSet<Move>();

        Set<State> firstStateSet = new HashSet<State>();
        firstStateSet.addAll(oldStartState);
        firstStateSet = getEmptyPackage(firstStateSet, oldMoves);
        State firstNewState = new State(nextStateNumber);nextStateNumber ++;
        stateMaps.put(firstStateSet, firstNewState);

        Set<Set<State>> checkedStateSet = new HashSet<Set<State>>();
        while(true){
            Set<State> nowStateSet = getUncheckedStateSet(checkedStateSet, stateMaps);
            if(nowStateSet == null){
                break;
            }

            State nowNewState = stateMaps.get(nowStateSet);
            nextStateNumber = createNewState(nowStateSet, nowNewState, stateMaps, nextStateNumber, oldMoves, newMoves);
            checkedStateSet.add(nowStateSet);
        }

        Set<State> allNewStates = new HashSet<State>(stateMaps.values());
        Set<Set<State>> keySets = stateMaps.keySet();
        Set<State> compareSet = new HashSet<>();

        for(Set<State> key : keySets){
            compareSet.clear();
            compareSet.addAll(key);
            compareSet.retainAll(oldFinalState);

            if(compareSet.size()!=0){
                newFinalStateSet.add(stateMaps.get(key));
            }

        }
        newStartStateSet.add(stateMaps.get(firstStateSet));

        this.setStartState(newStartStateSet);
        this.setFinalState(newFinalStateSet);
        this.setAllState(allNewStates);
        this.setMoves(newMoves);


    }

    private Set<State> getEmptyPackage(Set<State> stateSet, Set<Move> moves){
        Set<State> newStateSet = new HashSet<State>();
        newStateSet.addAll(stateSet);

        boolean newElementAdded;
        while(true){
            newElementAdded = false;
            Set<State> newElementSet = new HashSet<>();
            for(State state : newStateSet){
                for(Move move : moves){
                    if(move.getStart().equals(state)
                            && move.getCondition()==null
                            && !newStateSet.contains(move.getEnd())){

                        newElementSet.add(move.getEnd());
                        newElementAdded = true;
                    }
                }
            }
            newStateSet.addAll(newElementSet);

            if(newElementAdded == false){
                return newStateSet;
            }
        }

    }

    private Set<State> getUncheckedStateSet(Set<Set<State>> checkedStateSet, Map<Set<State>, State> map){
        return getUncheckedStateSet(checkedStateSet, map.keySet());
    }

    private Set<State> getUncheckedStateSet(Set<Set<State>> checkedStateSet, Set<Set<State>> allStateSets){
        for(Set<State> states : allStateSets){
            if(!checkedStateSet.contains(states))return states;
        }

        return null;
    }

    private int createNewState (Set<State> nowStateSet,
                                State nowNewState,
                                Map<Set<State>, State> stateMaps,
                                int nextStateNumber,
                                Set<Move> oldMoves,
                                Set<Move> newMoves
    ){

        for(Move move : oldMoves){//找到一个move使得source在nowStateSet里并且side在newMove中的side未出现

            if(nowStateSet.contains(move.getStart()) && move.getCondition()!=null &&
                    !sideAndStartInMoves(nowNewState, move.getCondition(), newMoves)){
                //确定后得到 side，这个side决定了接下来搜索整个集合中内容的方向
                Set<State> nextStateSet = new HashSet<State>();
                State nextNewState = null;
                for(State state : nowStateSet){
                    Set<Move> toMoves = getMoveBySideAndSource(state, move.getCondition(), oldMoves);
                    //拿到从当前state集合中的元素出发，且满足上一move的side的move
                    for(Move tomove : toMoves){
                        nextStateSet.add(tomove.getEnd());
                    }
                }
                //传导添加完成
                nextStateSet = getEmptyPackage(nextStateSet, oldMoves);
                //推导闭包
                if(!stateMaps.containsKey(nextStateSet)){//新状态
                    nextNewState = new State(nextStateNumber);nextStateNumber ++;
                    stateMaps.put(nextStateSet, nextNewState);
                    newMoves.add(new Move(nowNewState, nextNewState, move.getCondition()));
                }
                else{//老状态
                    newMoves.add(new Move(nowNewState, stateMaps.get(nextStateSet), move.getCondition()));
                }


            }

        }

        return nextStateNumber;

    }

    private boolean sideAndStartInMoves(State source, Object condition, Set<Move> moves){

        for(Move move : moves){
            if(source.equals(move.getStart()) && condition.equals(move.getCondition())){
                return true;
            }
        }
        return false;

    }

    private Set<Move> getMoveBySideAndSource(State source, Object condition, Set<Move> moves){
        Set<Move> result = new HashSet<Move>();

        for(Move move : moves){
            if(source.equals(move.getStart()) && condition.equals(move.getCondition())){
                result.add(move);
            }
        }

        return result;
    }

    public boolean checkIsDFA(NFA nfa){
        Set<Move> moves = nfa.getMoves();
        Map<State, Object> map = new HashMap<State, Object>();
        for(Move move : moves){
            if(move.getCondition()==null)return false;

            if(map.containsKey(move.getStart()) && map.get(move.getStart()).equals(move.getCondition()) ){
                return false;
            }else{
                map.put(move.getStart(), move.getCondition());
            }
        }

        return true;
    }

    @Override
    public void addState(State state) {

    }

    @Override
    public void addMove(Move move) {

    }


}
