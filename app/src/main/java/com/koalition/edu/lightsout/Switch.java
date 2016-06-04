package com.koalition.edu.lightsout;

/**
 * Created by Kingston on 4/2/2016.
 */
public class Switch {
    private int switchNumber;
    private int roomNumber;
    private boolean switchState;
    private boolean roomState;

    private boolean isSwitchedByAI;

    public boolean isRoomState() {
        return roomState;
    }

    public Switch(int switchNumber, int roomNumber, boolean switchState, boolean roomState) {
        this.switchNumber = switchNumber;
        this.roomNumber = roomNumber;
        this.switchState = switchState;
        this.roomState = roomState;
    }

    public boolean getIsSwitchedByAI() {
        return this.isSwitchedByAI;
    }

    public void setIsSwitchedByAI(Boolean switchedByAI) {
        this.isSwitchedByAI= switchedByAI;
    }

    public boolean getRoomState() {
        return this.roomState;
    }

    public boolean getSwitchState() {
        return this.switchState;
    }

    public int getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(int switchNumber) {
        this.switchNumber = switchNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    public void setRoomState(boolean roomState) {
        this.roomState = roomState;
    }
}
