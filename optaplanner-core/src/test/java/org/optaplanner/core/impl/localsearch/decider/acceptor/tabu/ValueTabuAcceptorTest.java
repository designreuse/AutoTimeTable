package org.optaplanner.core.impl.localsearch.decider.acceptor.tabu;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.localsearch.decider.acceptor.tabu.size.FixedTabuSizeStrategy;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchMoveScope;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchPhaseScope;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchStepScope;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValueTabuAcceptorTest {

    @Test
    public void tabuSize() {
        ValueTabuAcceptor acceptor = new ValueTabuAcceptor();
        acceptor.setTabuSizeStrategy(new FixedTabuSizeStrategy(2));
        acceptor.setAspirationEnabled(true);

        TestdataValue v0 = new TestdataValue("v0");
        TestdataValue v1 = new TestdataValue("v1");
        TestdataValue v2 = new TestdataValue("v2");
        TestdataValue v3 = new TestdataValue("v3");
        TestdataValue v4 = new TestdataValue("v4");

        DefaultSolverScope solverScope = new DefaultSolverScope();
        solverScope.setBestScore(SimpleScore.valueOf(0));
        LocalSearchPhaseScope phaseScope = new LocalSearchPhaseScope(solverScope);
        acceptor.phaseStarted(phaseScope);

        LocalSearchStepScope stepScope0 = new LocalSearchStepScope(phaseScope);
        LocalSearchMoveScope moveScope1 = buildMoveScope(stepScope0, v1);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v0)));
        assertEquals(true, acceptor.isAccepted(moveScope1));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v2))); // repeated call
        stepScope0.setStep(moveScope1.getMove());
        acceptor.stepEnded(stepScope0);
        phaseScope.setLastCompletedStepScope(stepScope0);

        LocalSearchStepScope stepScope1 = new LocalSearchStepScope(phaseScope);
        LocalSearchMoveScope moveScope2 = buildMoveScope(stepScope1, v2);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v0)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v1)));
        assertEquals(true, acceptor.isAccepted(moveScope2));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v2))); // repeated call
        stepScope1.setStep(moveScope2.getMove());
        acceptor.stepEnded(stepScope1);
        phaseScope.setLastCompletedStepScope(stepScope1);

        LocalSearchStepScope stepScope2 = new LocalSearchStepScope(phaseScope);
        LocalSearchMoveScope moveScope4 = buildMoveScope(stepScope2, v4);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope2, v0)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v1)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope2, v3)));
        assertEquals(true, acceptor.isAccepted(moveScope4));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v2))); // repeated call
        stepScope2.setStep(moveScope4.getMove());
        acceptor.stepEnded(stepScope2);
        phaseScope.setLastCompletedStepScope(stepScope2);

        LocalSearchStepScope stepScope3 = new LocalSearchStepScope(phaseScope);
        LocalSearchMoveScope moveScope3 = buildMoveScope(stepScope3, v3);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope3, v0)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope3, v1)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v2)));
        assertEquals(true, acceptor.isAccepted(moveScope3));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v2))); // repeated call
        stepScope3.setStep(moveScope3.getMove());
        acceptor.stepEnded(stepScope3);
        phaseScope.setLastCompletedStepScope(stepScope3);

        LocalSearchStepScope stepScope4 = new LocalSearchStepScope(phaseScope);
        LocalSearchMoveScope moveScope1Again = buildMoveScope(stepScope4, v1);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope4, v0)));
        assertEquals(true, acceptor.isAccepted(moveScope1Again));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope4, v2)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope4, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope4, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope4, v2))); // repeated call
        stepScope4.setStep(moveScope1Again.getMove());
        acceptor.stepEnded(stepScope4);
        phaseScope.setLastCompletedStepScope(stepScope4);

        acceptor.phaseEnded(phaseScope);
    }

    @Test
    public void tabuSizeMultipleEntitiesPerStep() {
        ValueTabuAcceptor acceptor = new ValueTabuAcceptor();
        acceptor.setTabuSizeStrategy(new FixedTabuSizeStrategy(2));
        acceptor.setAspirationEnabled(true);

        TestdataValue v0 = new TestdataValue("v0");
        TestdataValue v1 = new TestdataValue("v1");
        TestdataValue v2 = new TestdataValue("v2");
        TestdataValue v3 = new TestdataValue("v3");
        TestdataValue v4 = new TestdataValue("v4");

        DefaultSolverScope solverScope = new DefaultSolverScope();
        solverScope.setBestScore(SimpleScore.valueOf(0));
        LocalSearchPhaseScope phaseScope = new LocalSearchPhaseScope(solverScope);
        acceptor.phaseStarted(phaseScope);

        LocalSearchStepScope stepScope0 = new LocalSearchStepScope(phaseScope);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v0)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v1)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v0, v1)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v0, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v0, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v0, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v1, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v1, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v1, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v2, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v2, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope0, v3, v4)));
        stepScope0.setStep(buildMoveScope(stepScope0, v0, v2).getMove());
        acceptor.stepEnded(stepScope0);
        phaseScope.setLastCompletedStepScope(stepScope0);

        LocalSearchStepScope stepScope1 = new LocalSearchStepScope(phaseScope);
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v0)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v1)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v0, v1)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v0, v2)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v0, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v0, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v1, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v1, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v1, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v2, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, v2, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, v3, v4)));
        stepScope1.setStep(buildMoveScope(stepScope1, v1).getMove());
        acceptor.stepEnded(stepScope1);
        phaseScope.setLastCompletedStepScope(stepScope1);

        LocalSearchStepScope stepScope2 = new LocalSearchStepScope(phaseScope);
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v0)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v1)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v2)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope2, v3)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope2, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v0, v1)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v0, v2)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v0, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v0, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v1, v2)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v1, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v1, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v2, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope2, v2, v4)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope2, v3, v4)));
        stepScope2.setStep(buildMoveScope(stepScope2, v3, v4).getMove());
        acceptor.stepEnded(stepScope2);
        phaseScope.setLastCompletedStepScope(stepScope2);

        LocalSearchStepScope stepScope3 = new LocalSearchStepScope(phaseScope);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope3, v0)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v1)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope3, v2)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v0, v1)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope3, v0, v2)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v0, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v0, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v1, v2)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v1, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v1, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v2, v3)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v2, v4)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope3, v3, v4)));
        stepScope3.setStep(buildMoveScope(stepScope3, v0).getMove());
        acceptor.stepEnded(stepScope3);
        phaseScope.setLastCompletedStepScope(stepScope3);

        acceptor.phaseEnded(phaseScope);
    }

    @Test
    public void aspiration() {
        ValueTabuAcceptor acceptor = new ValueTabuAcceptor();
        acceptor.setTabuSizeStrategy(new FixedTabuSizeStrategy(2));
        acceptor.setAspirationEnabled(true);

        TestdataValue v0 = new TestdataValue("v0");
        TestdataValue v1 = new TestdataValue("v1");

        DefaultSolverScope solverScope = new DefaultSolverScope();
        solverScope.setBestScore(SimpleScore.valueOf(-100));
        LocalSearchPhaseScope phaseScope = new LocalSearchPhaseScope(solverScope);
        acceptor.phaseStarted(phaseScope);

        LocalSearchStepScope stepScope0 = new LocalSearchStepScope(phaseScope);
        stepScope0.setStep(buildMoveScope(stepScope0, v1).getMove());
        acceptor.stepEnded(stepScope0);
        phaseScope.setLastCompletedStepScope(stepScope0);

        LocalSearchStepScope stepScope1 = new LocalSearchStepScope(phaseScope);
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, -120, v0)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, -20, v0)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, -120, v1)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, -20, v1)));
        assertEquals(false, acceptor.isAccepted(buildMoveScope(stepScope1, -120, v0, v1)));
        assertEquals(true, acceptor.isAccepted(buildMoveScope(stepScope1, -20, v0, v1)));
        stepScope1.setStep(buildMoveScope(stepScope1, -20, v1).getMove());
        acceptor.stepEnded(stepScope1);
        phaseScope.setLastCompletedStepScope(stepScope1);

        acceptor.phaseEnded(phaseScope);
    }

    private LocalSearchMoveScope buildMoveScope(LocalSearchStepScope stepScope, TestdataValue... values) {
        return buildMoveScope(stepScope, 0, values);
    }

    private LocalSearchMoveScope buildMoveScope(LocalSearchStepScope stepScope, int score, TestdataValue... values) {
        LocalSearchMoveScope moveScope = new LocalSearchMoveScope(stepScope);
        Move move = mock(Move.class);
        when(move.getPlanningValues()).thenReturn((Collection) Arrays.asList(values));
        moveScope.setMove(move);
        moveScope.setScore(SimpleScore.valueOf(score));
        return moveScope;
    }

}
