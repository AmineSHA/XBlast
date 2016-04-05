package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player;

public enum Bonus {
	INC_BOMB {
		public Player applyTo(Player player) {

			if (player.maxBombs() < 9) {
				return new Player(player.id(), player.lifeStates(),
						player.directedPositions(), player.maxBombs() + 1,
						player.bombRange());
			} else {
				return new Player(player.id(), player.lifeStates(),
						player.directedPositions(), player.maxBombs(),
						player.bombRange());
			}
		}

	},
	INC_RANGE {
		public Player applyTo(Player player) {
			if (player.bombRange() < 9) {
				return new Player(player.id(), player.lifeStates(),
						player.directedPositions(), player.maxBombs(),
						player.bombRange()+1);
			} else {
				return new Player(player.id(), player.lifeStates(),
						player.directedPositions(), player.maxBombs(),
						player.bombRange());
			}
		}
	};
	abstract public Player applyTo(Player player);
}
