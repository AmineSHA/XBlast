package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlockTest {

    @Test
    public void castshadowtest() {
        assertTrue(Block.CRUMBLING_WALL.castsShadow());
        assertTrue(Block.INDESTRUCTIBLE_WALL.castsShadow());
        assertTrue(Block.DESTRUCTIBLE_WALL.castsShadow());
    }

}
