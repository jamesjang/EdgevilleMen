import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.pathfinding.impl.local.LocalPathFinder;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;

public class BankHandler {

    public Area BankArea = new Area(3092, 3497, 3097, 3494);
    public Area CombatArea = new Area(3092, 3512, 3099, 3508);

    PassableObstacle manDoor = new PassableObstacle("Large Door", "Open", null, null, null);

    BankHandler()  {
        LocalPathFinder.getLocalPathFinder().addObstacle(manDoor);
    }

    public void WalkToBank() {
        if (!IsInBankArea()) {
            Walking.walk(BankArea.getRandomTile());

            Sleep.sleepUntil(() -> IsInBankArea(), 8000);
        }
    }

    public void WalkToCombat() {
        if (!IsInManArea()) {
            if (Bank.isOpen())
                Sleep.sleepUntil( ()-> Bank.close(), 4000);

            Walking.walk(CombatArea.getRandomTile());

            Sleep.sleepUntil(() -> IsInManArea(), 8000);
        }
    }

    public void UseBank() {
        if (!Bank.isOpen() || !Players.getLocal().isMoving()) {
            GameObject bankBooth = GameObjects.closest("Bank booth");
            bankBooth.interact("Bank");
            Sleep.sleepUntil(() -> Bank.isOpen(), 4000);

            if (Bank.isOpen()) {
                Bank.withdrawAll("Salmon");
            }
        }
    }


    public boolean IsInBankArea() {
        return BankArea.contains(Players.getLocal().getTile());
    }

    public boolean IsInManArea() {
        return CombatArea.contains(Players.getLocal().getTile());
    }

    public boolean ShouldBank() {
        return !Inventory.contains("Salmon");
    }

}
