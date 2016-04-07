package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

/**
 * rewrite of the GameStatePrinter debug class.
 * 
 * @author Filipe Fortunato.
 * @License GPL V2 License.
 */
public final class GameStatePrinter {

    /**
     * UseANISChar, set to "true" to use ANIS escape characters or set to
     * "false" not to use ANSI escape characters
     */
    private static boolean UseANISChar = false;

    /**
     * DebugPlayers, set to "true" to have the player data be printed on the
     * right of the console screen or set to "false" to not have them printed.
     */
    private static boolean DebugPlayers = true;

    private static String BlackTXT = (UseANISChar) ? "\u001b[30m" : "";
    private static String BlueTXT = (UseANISChar) ? "\u001b[34m" : "";
    private static String WhiteTXT = (UseANISChar) ? "\u001b[37m" : "";
    private static String BlackBG = (UseANISChar) ? "\u001b[40m" : "";
    private static String RedBG = (UseANISChar) ? "\u001b[41m" : "";
    private static String GreenBG = (UseANISChar) ? "\u001b[42m" : "";
    private static String BlueBG = (UseANISChar) ? "\u001b[44m" : "";
    private static String CyanBG = (UseANISChar) ? "\u001b[46m" : "";
    private static String WhiteBG = (UseANISChar) ? "\u001b[47m" : "";
    private static String Default = (UseANISChar) ? "\u001b[m" : "";
    

    private GameStatePrinter() {
    }

    public static void printGameState(GameState s) {
        List<String> ToPrint = new ArrayList<String>();
        for (int i = 0; i < Cell.ROWS; i++)
            ToPrint.add("");
        List<Player> ps = s.alivePlayers();
        Board board = s.board();

        for (int i = 0; i < Cell.COUNT; i++) {
            String st = "";
            Cell c = Cell.ROW_MAJOR_ORDER.get(i);
            Block b = board.blockAt(c);
            for (Player p : ps)
                if (p.position().containingCell().rowMajorIndex() == i) {
                    st = stringForPlayer(p);
                }
            if (s.bombedCells().containsKey(c) && st == "")
                st = stringForBomb(s.bombedCells().get(c));
            if (s.blastedCells().contains(c) && st == "" && !b.canHostPlayer())
                st = BlackTXT + GreenBG + "**" + Default;
            if (st == "")
                st = stringForBlock(b);

            String str = ToPrint.get(i / Cell.COLUMNS);
            ToPrint.remove(i / Cell.COLUMNS);

            ToPrint.add((i / Cell.COLUMNS), str + st);
        }
        if (DebugPlayers)
            ToPrint = addPlayerDebug(ToPrint, ps, s.remainingTime());
        clearConsole();
        for (String st : ToPrint)
            System.out.println(st);
        System.out.println("done!");
    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N:
            b.append("\u2191");
            break;
        case E:
            b.append("\u2192");
            break;
        case S:
            b.append("\u2193");
            break;
        case W:
            b.append("\u2190");
            break;
        }
        return BlackTXT + CyanBG + b.toString() + Default;
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE:
            return WhiteBG + WhiteTXT + "  " + Default;
        case INDESTRUCTIBLE_WALL:
            return BlackBG + BlackTXT + "##" + Default;
        case DESTRUCTIBLE_WALL:
            return BlackBG + WhiteTXT + "??" + Default;
        case CRUMBLING_WALL:
            return BlackBG + WhiteTXT + "¿¿" + Default;
        case BONUS_BOMB:
            return RedBG + WhiteTXT + "+b" + Default;
        case BONUS_RANGE:
            return RedBG + WhiteTXT + "+r" + Default;
        default:
            throw new Error();
        }
    }

    private static String stringForBomb(Bomb b) {
        int fuseLength = b.fuseLength();

        return ((((int) Math.sqrt(fuseLength)) % 3 == 0) ? BlueTXT + WhiteBG
                : WhiteTXT + BlueBG) + "\u00F2 " + Default;
    }

    private static List<String> addPlayerDebug(List<String> lst,
            List<Player> lp, double rt) {
        List<String> NewString = new ArrayList<>(lst);
        List<String> DataToAdd = new ArrayList<String>();

        for (Player p : lp) {
            DataToAdd.add("  P"
                    + p.id().toString()
                            .substring(p.id().toString().length() - 1)
                    + " : " + p.lives() + " vies ("
                    + p.lifeState().state().toString() + ")             ");
            DataToAdd.add("       Bombes Max : " + p.maxBombs() + ", portée : "
                    + p.bombRange() + "                ");
            DataToAdd.add("       Position : "
                    + p.position().containingCell().toString()
                    + "                          ");
        }
        DataToAdd.add("   Temps restant : " + rt + " s                 ");

        for (int i = 0; i < NewString.size(); i++) {
            String tmp = NewString.get(i);
            NewString.remove(i);
            NewString.add(i, tmp + WhiteBG + BlackTXT
                    + DataToAdd.get(i).substring(0, 36) + Default);
        }
        return NewString;
    }
    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }
}