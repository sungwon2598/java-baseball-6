package baseball.Controller;

import baseball.Model.GameData;
import baseball.View.InputView;
import baseball.View.OutputView;
import camp.nextstep.edu.missionutils.Console;

import java.util.ArrayList;

public class MainController {
    OutputView outputView = new OutputView();
    InputView inputView = new InputView();
    GameData gameData = new GameData();
    Validator validator = new Validator();
    RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGenerator();
    PlayerInputController playerInputController = new PlayerInputController(validator);
    Comparator comparator = new Comparator();


    private static int state = 1;
    public MainController() {
        state = 1;
    }

    public void playGame(){
        outputView.printGameStart();
        while (state == 1){
            gameData.setComputerNumber(randomNumbersGenerator.generateNumbers());
            processInputAndCompare();
        }
    }

    private void processInputAndCompare() {
        while (true) {
            processPlayerInput();
            if (comparator.isEndGame(gameData.getComputerNumber(), gameData.getPlayerInput())){
                checkReplay();
                return;
            }
            processComperater();
        }
    }

    private void processPlayerInput() {
        inputView.printNumberRequest();
        String playerFirstInput = Console.readLine();
        ArrayList<Integer> playerInput = playerInputController.handlePlayerInput(playerFirstInput);
        gameData.setPlayerInput(playerInput);
    }

    private void checkReplay() {
        outputView.printHint(0,3);
        inputView.printGameEnd();
        String endnumber = Console.readLine();
        validator.validateGameEndInput(endnumber);

        if (endnumber.equals("1")) {
            MainController.state = 1;
        } else if (endnumber.equals("2")) {
            MainController.state = 0;
        }
    }

    private void processComperater() {
        boolean nothing = comparator.isNothing(gameData.getComputerNumber(), gameData.getPlayerInput());
        int ballCount = 0;
        int strikeCount = 0;

        if (!nothing) {
            ballCount = comparator.countBalls(gameData.getComputerNumber(), gameData.getPlayerInput());
            strikeCount = comparator.countStrikes(gameData.getComputerNumber(), gameData.getPlayerInput());
            gameData.setBallCount(ballCount);
            gameData.setStrikeCount(strikeCount);
        }

        outputView.printHint(ballCount, strikeCount);
    }

}
