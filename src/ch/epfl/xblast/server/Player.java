package ch.epfl.xblast.server;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.Cell;

public class Player {

    PlayerID id;
    Sq<LifeState> lifeStates;
    Sq<DirectedPosition> directedPos;
    int maxBombs;
    int bombRange;

    public Player(PlayerID id, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) {
        if (id == null || lifeStates == null || directedPos == null) {
            throw new NullPointerException();
        }
        if (maxBombs < 0 || bombRange < 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.lifeStates = lifeStates;
        this.directedPos = directedPos;
        this.maxBombs = maxBombs;
        this.bombRange = bombRange;

    }

    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) {
        if (maxBombs < 0 || bombRange < 0 || lives < 0) {
            throw new IllegalArgumentException();
        }
        
    }

    public PlayerID id() {
        return id;
    }

    public Sq<LifeState> lifeStates() {
        return lifeStates;
    }

    private Sq<LifeState> constructLifeStateSequence(int lives) {
        if (lives == 0) {
            return Sq.constant(new LifeState(lives, LifeState.State.DEAD));
        } else {
            return Sq.repeat(Ticks.INVULNERABLE_TICKS,
                    new LifeState(lives, LifeState.State.INVULNERABLE))
                    .concat(Sq.constant(new LifeState(lives,
                            LifeState.State.VULNERABLE)));
        }
    }

    public Sq<LifeState> statesForNextLife() {

        return constructLifeStateSequence(lifeStates.head().lives());

    }
    public int lives(){
        return lifeStates.head().lives();
    }
    public boolean isAlive()
    {
        return (lives()>0);
    }
    public Sq<DirectedPosition> directedPositions(){
        return directedPos;
    }
    public SubCell position(){
        return directedPos.head().position();
    }
    public Direction direction(){
        return directedPos.head().direction();
    }
    public int maxBombs(){
        return maxBombs;
    }
    public Player withMaxBombs(int newMaxBombs){
        return new  Player(id,Sq.constant(new LifeState(0,LifeState.State.DEAD)),Sq.constant(new DirectedPosition(new SubCell(0,0),Direction.S )),0,0);
    }
    public int bombRange(){
        return bombRange;
    }
    public Player withBombRange(int newBombRange){
        return new  Player(id,Sq.constant(new LifeState(0,LifeState.State.DEAD)),Sq.constant(new DirectedPosition(new SubCell(0,0),Direction.S )),0,0);
    }
    public Bomb newBomb(){
        return new Bomb(id(),position().containingCell(),Ticks.BOMB_FUSE_TICKS,bombRange);
    }
    
    public static class LifeState {
        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }

        int lives;
        State state;

        public LifeState(int lives, State state) {//TODO ici il manque un truc voir avec Joachim
            this.lives = lives;
            this.state = state;
        }

        public boolean canMove() {
            if (state == State.VULNERABLE || state == State.INVULNERABLE) {
                return true;
            }
            return false;
        }

        public int lives() {
            return lives;
        }

        public State state() {
            return state;
        }

    }

    public static class DirectedPosition {
        private SubCell position;
        private Direction direction;

        public DirectedPosition(SubCell position, Direction direction) {

            if (position == null || direction == null) {
                throw new NullPointerException();
            }

            this.direction = direction;
            this.position = position;
        }

        public static Sq<DirectedPosition> stopped(DirectedPosition p) {
            return Sq.constant(p);
        }

        public static Sq<DirectedPosition> moving(DirectedPosition p) {
            return Sq.iterate(p, c -> {
                return new DirectedPosition(c.position.neighbor((c.direction)),
                        p.direction);
            });
        }

        public SubCell position() {
            return position;
        }

        public Direction direction() {
            return direction;
        }

        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, this.direction);
        }

        public DirectedPosition withDirection(Direction newDirection) {
            return new DirectedPosition(this.position, newDirection);
        }

    }
}