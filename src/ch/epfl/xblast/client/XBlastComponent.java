package ch.epfl.xblast.client;

import java.awt.*;

import javax.swing.JComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

public final class XBlastComponent extends JComponent {
	GameState gs;
	PlayerID id;
	List<Integer> textPoss = new ArrayList<>();

	public Dimension getPreferredSize() {
		return new Dimension(960, 388);
	}

	protected void paintComponent(Graphics g0) {
		Graphics2D g = (Graphics2D) g0;
		int cumulativeBoardCoord = 0;
		int cumulativeScoreCoord = 0;
		int cumulativeTimeCoord = 0;
		// We can easily notice a pattern : Should I put this in a method ?

		for (int i = 0; i < gs.boardImages().size(); i++) {
			g.drawImage(gs.boardImages().get(i), cumulativeBoardCoord, 0, null);
			if (gs.bombsAndExplosionsImage().get(i) != null)
				g.drawImage(gs.bombsAndExplosionsImage().get(i),
						cumulativeBoardCoord, 0, null);
			cumulativeBoardCoord += gs.boardImages().get(i).getWidth(null);

		}

		for (int i = 0; i < gs.scoreLine().size(); i++) {
			g.drawImage(gs.scoreLine().get(i), cumulativeScoreCoord, 0, null);
			cumulativeScoreCoord += gs.timeLine().get(i).getWidth(null);

		}

		textPoss.addAll(Arrays.asList(96, 240, 768, 912));
		Font font = new Font("Arial", Font.BOLD, 25);
		g.setColor(Color.WHITE);
		g.setFont(font);

		for (int i = 0; i < textPoss.size(); i++) {
			g.drawString(Integer.toString(gs.players().get(i).lives()),
					textPoss.get(i), 659);

		}

		for (int i = 0; i < gs.timeLine().size(); i++) {

			g.drawImage(gs.timeLine().get(i), cumulativeTimeCoord, 0, null);
			cumulativeTimeCoord += gs.timeLine().get(i).getWidth(null);

		}
		// PLAYAS
		List<Player> playersCopy = new ArrayList<Player>(gs.players());

		Comparator<Player> sameVertCordCriteria = (a, b) -> a.id() == this.id ? -1
				: b.id() == this.id ? 1 : 0;
		Comparator<Player> vertCordCriteria = (a, b) -> a.position().y() < b
				.position().y() ? -1 : a.position().y() == b.position().y() ? 0
				: 1;
		Collections.sort(playersCopy,
				vertCordCriteria.thenComparing(sameVertCordCriteria));
		for (Player p : playersCopy) {
			g.drawImage(p.image(), 4 * p.position().x() - 24, 3 * p.position()
					.y() - 52, null);
		}

	}

	public void setGameState(GameState gs, PlayerID id) {
		this.gs = gs;
		this.id = id;
		repaint();
	}

}