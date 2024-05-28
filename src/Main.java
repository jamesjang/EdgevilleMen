import org.dreambot.api.Client;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.utilities.Sleep;

import java.awt.*;

@ScriptManifest(
        name = "Edgeville Massacre",
        description = "Kills men at Edgeville!",
        author = "Java Not Real",
        version = 1.0,
        category = Category.COMBAT,
        image = ""
        )

public class Main extends AbstractScript {

    State state;
    CombatHandler Combater = new CombatHandler();
    HealthHandler Health = new HealthHandler();
    BankHandler Bank = new BankHandler();
    Antiban antiBan = new Antiban(this);


    @Override
    public void onStart() {

        state = State.NONE;
    }

    @Override
    public int onLoop() {
        switch(getState()) {
            case NONE :
                if (!Combater.IsInCombat()) {

                    if (Dialogues.canContinue()) {
                        if (Dialogues.continueDialogue()) {
                            Sleep.sleepUntil(() -> !Dialogues.canContinue(), 3000);
                        }
                    }

                    if (Bank.IsInBankArea()) {
                        Bank.WalkToCombat();
                    }

                    if (!Health.IsAtOptimatlHealth())
                        Health.Eat();

                    sleep(antiBan.performAntiban());

                    Combater.AttackMan();
                    break;
                }
            case FIGHTING:
                if (Combater.IsInCombat()) {
                    if (!Health.IsAtOptimatlHealth()) {
                        if (Dialogues.canContinue()) {
                            if (Dialogues.continueDialogue()) {
                                Sleep.sleepUntil(() -> !Dialogues.canContinue(), 3000);
                            }
                        }

                        Health.Eat();
                        Combater.ResumeCombat();

                        sleep(antiBan.performAntiban());

                    }
                    if (Dialogues.canContinue()) {
                        if (Dialogues.continueDialogue()) {
                            Sleep.sleepUntil(() -> !Dialogues.canContinue(), 3000);
                        }
                    }
                }

            case BANKING:
            {
                if (Bank.ShouldBank()) {
                    if (Bank.IsInBankArea()) {
                        Bank.UseBank();
                    }
                    else {
                        Bank.WalkToBank();

                        sleep(antiBan.performAntiban());
                    }

                }
                else if (!Bank.ShouldBank() && !Bank.IsInManArea()){
                    Bank.WalkToCombat();

                    sleep(antiBan.performAntiban());

                    state = State.NONE;
                }

                break;
            }
        }
        return 0;
    }

    State getState() {
        if (!Health.HasFood()) {
            return state = State.BANKING;
        }
        if (Combater.IsInCombat() && Bank.IsInManArea()) {
            return state = State.FIGHTING;
        }
        else {
            return state = State.NONE;
        }
    }

    @Override
    public void onPaint(Graphics g) {
        g.setFont(new Font("Constantia", Font.PLAIN, 18));
        g.drawString("Current status" + state.toString(), 82, Client.getViewportHeight() - 110);

    }

}
