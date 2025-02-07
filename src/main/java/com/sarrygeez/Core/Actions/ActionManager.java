package com.sarrygeez.Core.Actions;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("unused")
public class ActionManager {

    private final Queue<Action> actionQueue = new LinkedList<>();

    /**
     * Appends an action to the last of the line(end of queue)
     * @param action - the action added in the queue
     */
    public void addAction(Action action) {
        actionQueue.offer(action);
    }

    /**
     * Calls the <code>execute()</code> function of the Action
     * that is called of <code>pop()</code> on the queue
     */
    public Action triggerAction() {
        Action act = actionQueue.remove();
        act.execute();
        return act;
    }

    public Action peek() {
        return actionQueue.peek();
    }

    public Queue<Action> getActionQueue() {
        return actionQueue;
    }
}
