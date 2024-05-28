import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

public class CombatHandler {

    public String Man = "Man";
    public Area CombatArea = new Area(3092, 3512, 3099, 3508);
    NPC interactingNPC = null;

    public boolean IsInCombat() {
        return Players.getLocal().isInCombat();
    }

    public boolean AttackMan() {


        NPC man = NPCs.closest( m-> Man.equalsIgnoreCase(m.getName())
                                && m.canReach()
                                && m.canAttack()
                                && CombatArea.contains(m.getTile()));

        if (man != null) {
            man.interact("Attack");

            if (Sleep.sleepUntil( ()-> IsInCombat(), 3000)) {
                interactingNPC = man;
                return true;
            }

            return false;
        }
        return false;
    }

    public void ResumeCombat() {
        if (interactingNPC != null) {
            interactingNPC.interact("Attack");

            if (Dialogues.canContinue()) {
                if (Dialogues.continueDialogue()) {
                    Sleep.sleepUntil(() -> !Dialogues.canContinue(), 3000);
                }
            }

            Sleep.sleepUntil( ()-> IsInCombat(), 3000);

        }
    }



}
