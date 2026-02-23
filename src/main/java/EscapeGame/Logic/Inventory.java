package EscapeGame.Logic;

import EscapeGame.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (item != null && !hasItem(item.getName())) {
            this.items.add(item);
        }
    }

    public boolean hasItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return this.items.size();
    }

    public void clear() { items.clear(); }
}
