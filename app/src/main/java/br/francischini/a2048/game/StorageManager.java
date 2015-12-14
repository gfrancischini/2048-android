package br.francischini.a2048.game;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by gabriel on 12/14/15.
 */
public class StorageManager {
    private static final String PREF_STRING_BEST_SCORE = "pref_string_best_score";
    private static final String PREF_STRING_GAME_STATE = "pref_string_game_state";

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

    public void updateBestScore(int bestScore) {
        Prefs.putInt(PREF_STRING_BEST_SCORE, bestScore);
    }

    public int getBestScore() {
        return Prefs.getInt(PREF_STRING_BEST_SCORE, 0);
    }

    public void clearGameState() {
        Prefs.remove(PREF_STRING_GAME_STATE);
    }

    public void setGameState(Manager manager) {
        Gson gson = new Gson();

        GameState gameState = new GameState(manager.getGrid(), manager.getScore(), manager.isOver(), manager.isWon(), manager.isKeepPlaying());
        String jsonGameState = gson.toJson(gameState);
        Prefs.putString(PREF_STRING_GAME_STATE, jsonGameState);
    }


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
