package com.tasty.fish.text;

import org.junit.Assert;
import org.junit.Test;

public class TextEditorTests
{
    private TextEditor build(){
        return new TextEditor(new TextCursor());
    }

    @Test
    public void backspaceOnEnd() {
        TextEditor editor = build();
        editor.setText("hello");
        editor.backspace();
        Assert.assertEquals("hell", editor.getText());
        Assert.assertEquals(4, editor.getCursor().getPosition());
    }

    @Test
    public void backspaceFirstChar() {
        TextEditor editor = build();
        editor.setText("hello");
        editor.getCursor().goToStart();
        editor.getCursor().moveRight();
        editor.backspace();
        Assert.assertEquals("ello", editor.getText());
    }

    @Test
    public void backspaceAtZeroNotAllowed() {
        TextEditor editor = build();
        editor.setText("hello");
        editor.getCursor().goToStart();
        editor.backspace();
        Assert.assertEquals("hello", editor.getText());
    }
}