package com.emse.warehouselego.legosupply;

import com.emse.warehouselego.legosupply.server.model.StockGroup;
import com.emse.warehouselego.legosupply.server.model.StockEntry;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<StockGroup> stockEntriesToGroup(List<StockEntry> stockEntries) {
        List<StockGroup> res = new ArrayList<>();
        for(StockEntry entry : stockEntries) {
            int pos = findExistingColor(res, entry.getColor());
            if(pos == -1) {
                StockGroup newItem = new StockGroup(entry.getColor(), 1);
                res.add(newItem);
            }
            else {
                res.get(pos).setQuantity(res.get(pos).getQuantity() + 1);
            }
        }
        return res;
    }

    private static int findExistingColor(List<StockGroup> list, String color) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getColor().equalsIgnoreCase(color)) {
                return i;
            }
        }
        return -1;
    }
}
