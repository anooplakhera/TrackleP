package com.example.tracklep.Utils;

import com.example.tracklep.DataModels.ResponseModelClasses;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class MyBarDataSet extends BarDataSet {

    ArrayList<ResponseModelClasses.BarChart> allowValue;

    public MyBarDataSet(List<BarEntry> yVals, ArrayList<ResponseModelClasses.BarChart> allowValue, String label) {
        super(yVals, label);
        this.allowValue = allowValue;
    }

    @Override
    public int getColor(int index) throws IndexOutOfBoundsException, NullPointerException {

        if (getEntryForIndex(index).getY() <= allowValue.get(index).getCount()) // less than 95 green
//        if (getEntryForIndex(index).getY() <= 10.0f) // less than 95 green
            return mColors.get(0);
        else // greater or equal than 100 red
            return mColors.get(1);


    }

}
