package com.example.database_pj.ObserverTool;

import java.util.Iterator;
import java.util.List;

public class Subject {
    private List<Observer> ObserverList;

    /**
     * 用于增加收藏者（Observer）
     *
     * @param a 指要被增加的Observer
     */
    public void attachObserver(Observer a) {
        ObserverList.add(a);
    }

    /**
     * 用于减少收藏者（Observer）
     *
     * @param b 指要被减少的Observer
     */
    public void deleteObserver(Observer b) {
        String name = b.getName();
        Iterator<Observer> iterator = ObserverList.iterator();
        while (iterator.hasNext()) {
            Observer observer = iterator.next();
            if (observer.getName().equals(name)) {
                iterator.remove();
            }
        }
    }

    /**
     * 让所有的Observer update
     */
    public void notifyObserver() {
        Iterator<Observer> iterator = ObserverList.iterator();
        while (iterator.hasNext()) {
            Observer observer = iterator.next();
            observer.update(this);
        }
    }
}
