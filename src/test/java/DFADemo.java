import tool.DFA;
import tool.Move;
import tool.NFA;
import tool.State;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DFADemo {
    public static void main(String[] args) throws Exception{
        NFA nfa = new NFA();
        nfa.addStartState(1);
        nfa.addFinalState(5);
        nfa.addMove(1, 2, null);
        nfa.addMove(2, 3, "123");
        nfa.addMove(3, 4, null);
        nfa.addMove(4, 1, null);
        nfa.addMove(4, 5, "1");
        nfa.addMove(5, 5, "1");

        DFA dfa = new DFA(nfa);
        System.out.println(dfa.match(new Object[]{"123", "123", "1", "1", "1","2", "3"}));







    }
}
