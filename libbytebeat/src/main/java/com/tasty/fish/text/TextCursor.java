package com.tasty.fish.text;

public class TextCursor {
    private int _max;
    private int _position;

    public void setRange(int length) {
        _max = length;
    }

    public int getPosition() {
        return _position;
    }

    public void advance(int spaces) {
        _position += spaces;

        if(_position > _max)
            _position = _max;
        else if(_position < 0)
            _position = 0;
    }

    public int moveLeft() {
        advance(-1);
        return _position;
    }

    public int moveRight(){
        advance(1);
        return _position;
    }

    public void goToEnd() {
        _position = _max;
    }

    public void goToStart() {
        _position = 0;
    }
}
