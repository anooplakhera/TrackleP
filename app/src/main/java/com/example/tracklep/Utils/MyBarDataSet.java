package com.example.tracklep.Utils;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class MyBarDataSet extends BarDataSet {

    List<BarEntry> yVals;

    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
        this.yVals = yVals;
    }

    @Override
    public int getColor(int index) {
        int position = 0;
        for (int i = 0; i < yVals.size(); i++) {
            if (yVals.get(index).getX() < yVals.get(index).getY()) {
                position = mColors.get(0);
            } else {
                position = mColors.get(1);
            }
        }
        return position;
    }

}
