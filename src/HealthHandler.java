import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

public class HealthHandler {
    String Salmon = "Salmon";

    private final int BaseEatPercentage = 40;
    private final int EatPercentageVariance = 10;
    private int EatPercentage;

    HealthHandler()  {
        EatPercentage = GetEatPercentage();

        Logger.log("Eat Percentage " + EatPercentage);
    }

    int GetEatPercentage() {
        return Calculations.random(BaseEatPercentage - EatPercentageVariance, BaseEatPercentage + EatPercentageVariance);
    }

    public void Eat() {
        if (!Inventory.isOpen())
            Inventory.open();

        if (Inventory.contains(Salmon)) {
            Inventory.interact(Salmon, "Eat");

            Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 4000);

            EatPercentage = GetEatPercentage();

            Logger.log("Next Eat Percentage " + EatPercentage);
        }
    }

    boolean IsAtOptimatlHealth() {
       return Players.getLocal().getHealthPercent() >= EatPercentage;

    }

    public boolean HasFood() {
        return Inventory.contains(Salmon);
    }
}
