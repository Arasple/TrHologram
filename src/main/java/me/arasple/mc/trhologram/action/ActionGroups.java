package me.arasple.mc.trhologram.action;

import me.arasple.mc.trhologram.action.base.AbstractAction;

import java.util.List;

/**
 * @author Arasple
 * @date 2020/2/15 12:07
 */
public class ActionGroups {

    private int priority;
    private String requirement;
    private List<AbstractAction> actions;

    public ActionGroups(int priority, String requirement, List<AbstractAction> actions) {
        this.priority = priority;
        this.requirement = requirement;
        this.actions = actions;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public List<AbstractAction> getActions() {
        return actions;
    }

    public void setActions(List<AbstractAction> actions) {
        this.actions = actions;
    }

}
