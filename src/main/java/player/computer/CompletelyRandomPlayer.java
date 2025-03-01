package player.computer;

import player.Choice;
import player.Player;
import player.Strategy;

import static player.Strategy.RANDOM;

public class CompletelyRandomPlayer extends Player {

    @Override
    public float play(float bid) {
        Choice result = Choice.random();
        if (result == Choice.BID) {
            return bid + 5f;
        } else {
            return bid + 0f;
        }
    }

    @Override
    public Strategy getStrategy() {
        return RANDOM;
    }
}
