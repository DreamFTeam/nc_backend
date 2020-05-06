package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.data.rowmappers.GameSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.UUID;

@Repository
public class GameSessionDaoImpl implements GameSessionDao {

    private final JdbcTemplate jdbcTemplate;
    private final GameDao gameDao;

    @Autowired
    public GameSessionDaoImpl(JdbcTemplate jdbcTemplate, GameDao gameDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.gameDao = gameDao;
    }

    @Override
    public GameSession getSessionByAccessId(String accessId, String userId) {

        GameSession gameSession;

        try{
            gameSession = jdbcTemplate.queryForObject("SELECT * " +
                    "FROM users_games WHERE user_id = UUID(?) AND game_id IN (" +
                    "SELECT game_id FROM games WHERE access_code = ?);",
                    new Object[]{userId, accessId}, new GameSessionMapper());
        }catch (EmptyResultDataAccessException e){
            gameSession = null;
        }


        //IF SESSION CREATED
        if (gameSession != null) {
            return gameSession;
        }

        Game game = gameDao.getGameByAccessId(accessId);

        gameSession = GameSession.builder()
                .userId(userId)
                .gameId(game.getId())
                .score(0)
                .winner(false)
                .creator(false)
                .savedByUser(true)   // REFACTOR FORM ANONYMOUS
                .durationTime(gameDao.getGameDuration(game.getId()))
                .build();

        return createSession(gameSession);
    }


    @Override
    public GameSession createSession(GameSession gameSession) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users_games" +
                    "(user_id, game_id, score," +
                    "is_winner, is_creator, saved_by_user, duration_time)" +
                    " VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, UUID.fromString(gameSession.getUserId()));
            ps.setObject(2, UUID.fromString(gameSession.getGameId()));
            ps.setInt(3, gameSession.getScore());
            ps.setBoolean(4, gameSession.isWinner());
            ps.setBoolean(5, gameSession.isCreator());
            ps.setBoolean(6, gameSession.isSavedByUser());
            ps.setObject(7, gameSession.getDurationTime());
            return ps;
        }, keyHolder);

        gameSession.setId(Objects.requireNonNull(keyHolder.getKeys()).get("game_session_id").toString());

        return gameSession;
    }

    @Override
    public void updateDurationTime(int durationTime, String gameId) {
        jdbcTemplate.update("UPDATE users_games SET duration_time = ? " +
                "WHERE game_id = UUID(?)", durationTime, gameId);
    }

    @Override
    public boolean gameHasAvailableSlots(String accessId) {

        int playersJoined = jdbcTemplate.queryForObject("SELECT COUNT(*)" +
                "FROM users_games WHERE game_id IN (" +
                "SELECT game_id FROM games WHERE access_code = ?);", new Object[]{accessId}, Integer.class);

        int allSlots = jdbcTemplate.queryForObject("SELECT max_num_of_users " +
                "FROM games " +
                "WHERE access_code = ?;", new Object[]{accessId}, Integer.class);

        return playersJoined != allSlots;
    }
}
