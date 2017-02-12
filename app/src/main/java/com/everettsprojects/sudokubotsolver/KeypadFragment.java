package com.everettsprojects.sudokubotsolver;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class KeypadFragment extends Fragment {

    private Button mKeyPad1;
    private Button mKeyPad2;
    private Button mKeyPad3;
    private Button mKeyPad4;
    private Button mKeyPad5;
    private Button mKeyPad6;
    private Button mKeyPad7;
    private Button mKeyPad8;
    private Button mKeyPad9;
    private Button mKeyPadClear;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_keypad, container, false);

        mKeyPad1 = (Button) rootView.findViewById(R.id.keypad_1);
        mKeyPad2 = (Button) rootView.findViewById(R.id.keypad_2);
        mKeyPad3 = (Button) rootView.findViewById(R.id.keypad_3);
        mKeyPad4 = (Button) rootView.findViewById(R.id.keypad_4);
        mKeyPad5 = (Button) rootView.findViewById(R.id.keypad_5);
        mKeyPad6 = (Button) rootView.findViewById(R.id.keypad_6);
        mKeyPad7 = (Button) rootView.findViewById(R.id.keypad_7);
        mKeyPad8 = (Button) rootView.findViewById(R.id.keypad_8);
        mKeyPad9 = (Button) rootView.findViewById(R.id.keypad_9);
        mKeyPadClear = (Button) rootView.findViewById(R.id.clear_cell_button);

        return rootView;
    }
}
