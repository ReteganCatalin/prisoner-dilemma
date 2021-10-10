import player.Choice;
import player.Player;
import player.Round;
import player.computer.ComputerPlayer;
import player.computer.Strategy;
import result.PayoffCalculator;
import result.Result;
import result.RoundPayoff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class Tournament {

  public static final long ROUNDS_PER_TOURNAMENT = 1000;

  private final PayoffCalculator calculator;
  private final Player player;

  private final Result result = new Result();

  public Tournament(PayoffCalculator calculator, Player player) {
    this.calculator = calculator;
    this.player = player;
  }

  public void play() {

    ComputerPlayer strategy = getRandomStrategy();
    Collection<RoundPayoff> payoffs = play(strategy);
    Result strategyResult = new Result();
    strategyResult.addPayoffs(payoffs);

    this.result.addPayoffs(payoffs);
    this.result.normalize(ROUNDS_PER_TOURNAMENT);
  }

  public Result getResult() {
    return result;
  }

  private Collection<RoundPayoff> play(Player computer) {
    List<RoundPayoff> payoffs = new ArrayList<>();

    for (int i = 0; i < ROUNDS_PER_TOURNAMENT; i++) {
      payoffs.add(playRound(computer));
    }

    return payoffs;
  }

  private RoundPayoff playRound(Player computer) {
    Choice p1Choice = ofNullable(player.play()).orElse(Choice.random());
    Choice p2Choice = computer.play();
    player.addRound(Round.builder().playerChoice(p1Choice).computerChoice(p2Choice).build());
    return calculator.computePayoff(p1Choice, p2Choice);
  }

  private ComputerPlayer getRandomStrategy() {
    final List<Strategy> strategies = enabledStrategies();
    final int index = new Random().nextInt(strategies.size());
    return strategies.get(index).getPlayer();
  }

  private List<Strategy> enabledStrategies() {
    return Arrays.stream(Strategy.values())
        .filter(Strategy::isEnabled)
        .collect(Collectors.toList());
  }
}
