package com.tasty.fish.text;

public class TextEditor {
    private final TextCursor _cursor;
    private String _text;

    public TextEditor(TextCursor textCursor) {
        _cursor = textCursor;
    }

    public void setText(String text) {
        _text = text;
        _cursor.setRange(text.length());
        _cursor.goToEnd();
    }

    public TextCursor getCursor() {
        return _cursor;
    }

    public String getText() {
        return _text;
    }

    private String[] splitOnCursor(){
        int pos = _cursor.getPosition();
        return new String[] {_text.substring(0, pos),_text.substring(pos)};
    }

    public void backspace() {
        int pos = _cursor.getPosition();
        if (pos > 0) {
            String before = _text.substring(0, pos - 1);
            String after = _text.substring(pos);
            _text = before + after;
            _cursor.setRange(_text.length());
            _cursor.moveLeft();
        }
    }

    public void add(String element) {
        int pos = _cursor.getPosition();
        String[] halves = splitOnCursor();
        _text = halves[0] + element + halves[1];
        _cursor.setRange(_text.length());
        _cursor.advance(element.length());
    }
}
