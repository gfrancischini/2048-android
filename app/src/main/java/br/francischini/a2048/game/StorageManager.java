package br.francischini.a2048.game;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by gabriel on 12/14/15.
 */
public class StorageManager {
    private static final String PREF_STRING_BEST_SCORE = "pref_string_best_score";
    private static final String PREF_STRING_GAME_STATE = "pref_string_game_state";

    /**
     * Game state class
     */
    public static class GameState {
        Grid grid;
        int score;
        boolean isGameOver;
        boolean isWon;
        public boolean keepPlaying;

        GameState(Grid grid, int score, boolean isGameOver, boolean isWon, boolean keepPlaying) {
            this.grid = grid;
            this.score = score;
            this.isGameOver = isGameOver;
            this.isWon = isWon;
            this.keepPlaying = keepPlaying;
        }
    }

    /**
     * save the current game score as the best one
     *
     * @param bestScore
     */
    public void updateBestScore(int bestScore) {
        Prefs.putInt(PREF_STRING_BEST_SCORE, bestScore);
    }

    /**
     * @return game best score
     */
    public int getBestScore() {
        return Prefs.getInt(PREF_STRING_BEST_SCORE, 0);
    }

    /**
     * Clear game state
     */
    public void clearGameState() {
        Prefs.remove(PREF_STRING_GAME_STATE);
    }

    /**
     * Save the current game state
     *
     * @param manager the game manager
     */
    public void setGameState(Manager manager) {
        Gson gson = new Gson();

        GameState gameState = new GameState(manager.getGrid(), manager.getScore(), manager.isOver(), manager.isWon(), manager.isKeepPlaying());
        String jsonGameState = gson.toJson(gameState);
        Prefs.putString(PREF_STRING_GAME_STATE, jsonGameState);
    }

    /**
     * @return the saved game staste
     */
    public GameState getGameState() {
        Gson gson = new Gson();
        String jsonGrid = Prefs.getString(PREF_STRING_GAME_STATE, null);
        if (jsonGrid == null) {
            return null;
        }
        GameState gameState = gson.fromJson(jsonGrid, GameState.class);
        return gameState;
    }

}
