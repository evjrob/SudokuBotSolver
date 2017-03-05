package com.everettsprojects.sudokubotsolver;

import org.junit.Test;

import java.util.ArrayList;

import static com.everettsprojects.sudokubotsolver.Utility.convertStringToArray;
import static junit.framework.Assert.assertEquals;

/**
 * Created by everett on 05/03/17.
 */
public class UtilityTest {
    @Test
    public void conversionTests() throws Exception {
        String inputString = "0,15,0,1,0,2,10,14,12,0,0,0,0,0,0,0,0,6,3,16,12,0,8,4,14,15,1,0,2,0,0,0,14,0,9,7,11,3,15,0,0,0,0,0,0,0,0,0,4,13,2,12,0,0,0,0,6,0,0,0,0,15,0,0,0,0,0,0,14,1,11,7,3,5,10,0,0,8,0,12,3,16,0,0,2,4,0,0,0,14,7,13,0,0,5,15,11,0,5,0,0,0,0,0,0,9,4,0,0,6,0,0,0,0,0,0,13,0,16,5,15,0,0,12,0,0,0,0,0,0,0,0,9,0,1,12,0,8,3,10,11,0,15,0,2,12,0,11,0,0,14,3,5,4,0,0,0,0,9,0,6,3,0,4,0,0,13,0,0,11,9,1,0,12,16,2,0,0,10,9,0,0,0,0,0,0,12,0,8,0,6,7,12,8,0,0,16,0,0,10,0,13,0,0,0,5,0,0,5,0,0,0,3,0,4,6,0,1,15,0,0,0,0,0,0,9,1,6,0,14,0,11,0,0,2,0,0,0,10,8,0,14,0,0,0,13,9,0,4,12,11,8,0,0,2,0";
        ArrayList<String> convertedArray = convertStringToArray(inputString);
        String outputString = Utility.convertGridToString(convertedArray);

        assertEquals("The string resulting from a conversion to an ArrayList and back was the same as the input.", inputString, outputString);
    }
}