package com.gitonway.countzero.model;

import java.security.InvalidParameterException;

import com.gitonway.countzero.model.numberUtils.Eight;
import com.gitonway.countzero.model.numberUtils.Five;
import com.gitonway.countzero.model.numberUtils.Four;
import com.gitonway.countzero.model.numberUtils.Nine;
import com.gitonway.countzero.model.numberUtils.One;
import com.gitonway.countzero.model.numberUtils.Seven;
import com.gitonway.countzero.model.numberUtils.Six;
import com.gitonway.countzero.model.numberUtils.Three;
import com.gitonway.countzero.model.numberUtils.Two;
import com.gitonway.countzero.model.numberUtils.Zero;

public class NumberUtils {

    public static float[][] getControlPointsFor(int start) {
        switch (start) {
            case 0:
                return Zero.getInstance().getControlPoints();
            case 1:
                return One.getInstance().getControlPoints();
            case 2:
                return Two.getInstance().getControlPoints();
            case 3:
                return Three.getInstance().getControlPoints();
            case 4:
                return Four.getInstance().getControlPoints();
            case 5:
                return Five.getInstance().getControlPoints();
            case 6:
                return Six.getInstance().getControlPoints();
            case 7:
                return Seven.getInstance().getControlPoints();
            case 8:
                return Eight.getInstance().getControlPoints();
            case 9:
                return Nine.getInstance().getControlPoints();
            default:
                throw new InvalidParameterException("Unsupported number requested");
        }
    }
}
