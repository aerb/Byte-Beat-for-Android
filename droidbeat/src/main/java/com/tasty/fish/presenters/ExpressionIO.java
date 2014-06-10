package com.tasty.fish.presenters;

import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.FileSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class ExpressionIO {
    private final FileSystem _fileSystem;

    public ExpressionIO(FileSystem fileSystem) {
        _fileSystem = fileSystem;
    }

    private File getExpressionPath(ByteBeatExpression expression) throws IOException {
        String name = expression.getName();
        return new File(_fileSystem.getSavedDir(), name + FileSystem.ext);
    }

    public void save(ByteBeatExpression expression) throws IOException {
        if(expression.isReadOnly())
            throw new IllegalArgumentException("Cannot save readonly expression.");

        File filename = getExpressionPath(expression);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(filename);
            pw.println(expression.getExpressionString());
        }
        finally {
            if(pw != null) pw.close();
        }
    }

    public void delete(ByteBeatExpression expression) throws IOException {
        File file = getExpressionPath(expression);
        file.delete();
    }
}
