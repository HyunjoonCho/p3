package com.example.p3;

import android.widget.Switch;

public class TabThreeItem {
    private boolean aSwitch;
    private String warning;

    public void setIcon(boolean b1){
        aSwitch = b1;
    }
    public void setWarning(String tab3_text){
        warning = tab3_text;
    }

    public boolean getIcon(){
        return this.aSwitch;
    }
    public String getWarning(){
        return this.warning;
    }
}
