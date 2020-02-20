package me.arasple.mc.trhologram.action;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.lite.Numbers;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.action.acts.*;
import me.arasple.mc.trhologram.action.base.AbstractAction;
import me.arasple.mc.trhologram.action.base.EnumOption;
import me.arasple.mc.trhologram.utils.JavaScript;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;

/**
 * @author Arasple
 * @date 2019/12/21 20:33
 * TrHologram internal part of TrAction
 */
public class TrAction {

    private static List<AbstractAction> actions = Arrays.asList(
            new ActionUnknow(),
            new ActionCommand(),
            new ActionActionbar(),
            new ActionBreak(),
            new ActionCatcher(),
            new ActionCleanCatchers(),
            new ActionClose(),
            new ActionCommandConsole(),
            new ActionCommandOp(),
            new ActionConnect(),
            new ActionDelay(),
            new ActionGiveMoney(),
            new ActionJs(),
            new ActionSound(),
            new ActionTakeItem(),
            new ActionTakeMoney(),
            new ActionTell(),
            new ActionTellraw(),
            new ActionTitle()
    );

    public static void runActions(List<AbstractAction> actions, Player player) {
        long delay = 0;
        for (AbstractAction action : actions) {
            if (action instanceof ActionBreak) {
                if (action.getOptions().containsKey(EnumOption.CHANCE) && !Numbers.random(NumberUtils.toDouble(action.getOptions().get(EnumOption.CHANCE), 1))) {
                    break;
                }
                if (action.getOptions().containsKey(EnumOption.REQUIREMENT) && (boolean) JavaScript.run(player, action.getOptions().get(EnumOption.REQUIREMENT))) {
                    break;
                }
            } else if (action instanceof ActionDelay) {
                delay += NumberUtils.toDouble(action.getContent(), 0);
            } else {
                if (delay > 0) {
                    Bukkit.getScheduler().runTaskLater(TrHologram.getPlugin(), () -> action.run(player), delay);
                } else {
                    action.run(player);
                }
            }
        }
    }

    /**
     * 同行并列多个动作, 动作选项共享
     * 分割符号 “_||_”
     *
     * @param lines 行
     * @return 动作
     */
    public static List<AbstractAction> readActions(List<String> lines) {
        List<AbstractAction> actions = new ArrayList<>();
        lines.forEach(line -> actions.addAll(readActions(line)));
        return actions;
    }

    public static List<AbstractAction> readActions(String line) {
        List<AbstractAction> actions = new ArrayList<>();
        HashMap<EnumOption, String> options = new HashMap<>();
        for (String s : line.split("_\\|\\|_")) {
            AbstractAction read = readSingleAction(s);
            if (read != null) {
                actions.add(read);
                if (read.getOptions() != null) {
                    options.putAll(read.getOptions());
                }
            }
        }
        if (actions.size() > 1) {
            actions.forEach(act -> act.setOptions(options));
        }
        return actions;
    }

    /**
     * 读单个动作
     *
     * @param line 行
     * @return 动作
     */
    private static AbstractAction readSingleAction(String line) {
        String tag = line.replaceAll("<([^<>].+)>", "").split(":", 2)[0];
        AbstractAction action = actions.stream().filter(act -> tag.matches("(?i)" + act.getName())).findFirst().orElse(actions.get(0));
        HashMap<EnumOption, String> options = new HashMap<>();

        if (action == null) {
            return null;
        } else {
            action = action.create();
            line = line.replaceFirst(tag + "( )?:( )?", "");
        }

        for (EnumOption option : EnumOption.values()) {
            Matcher matcher = option.matcher(line);
            while (matcher.find()) {
                String find = matcher.group();
                String[] opts = find.split(":", 2);
                String value = opts.length >= 2 ? opts[1].substring(0, opts[1].length() - 1) : null;
                options.put(option, value);
                line = line.replace(find, "");
            }
        }

        action.setContent(line);
        action.setOptions(options);
        return action;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<ActionGroups> readActionGroups(Object actions) {
        List<ActionGroups> actionGroups = Lists.newArrayList();
        if (actions instanceof List && !((List) actions).isEmpty()) {
            boolean isConditionalGroups = ((List) actions).get(0) instanceof LinkedHashMap;
            if (isConditionalGroups) {
                ((List) actions).forEach(obj -> {
                    LinkedHashMap section = (LinkedHashMap) obj;
                    actionGroups.add(new ActionGroups(NumberUtils.toInt(String.valueOf(section.getOrDefault("priority", -1))), section.containsKey("condition") ? String.valueOf(section.get("condition")) : null, TrAction.readActions((List<String>) section.getOrDefault("list", Lists.newArrayList()))));
                });
            } else {
                actionGroups.add(new ActionGroups(-1, null, TrAction.readActions((List<String>) actions)));
            }
            return actionGroups;
        }
        return actionGroups;
    }

}
