package com.tasty.fish.presenters;

import com.tasty.fish.domain.IExpressionsRepository;
import com.tasty.fish.domain.implementation.ByteBeatExpression;
import com.tasty.fish.utils.FileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ExpressionIO {
    private final FileSystem _fileSystem;
    private final IExpressionsRepository _repo;

    public ExpressionIO(
            FileSystem fileSystem,
            IExpressionsRepository repo) {
        _fileSystem = fileSystem;
        _repo = repo;
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
            pw.print(expression.getExpressionString());
        }
        finally {
            if(pw != null) pw.close();
        }
    }

    public void delete(ByteBeatExpression expression) throws IOException {
        File file = getExpressionPath(expression);
        file.delete();
    }

    public void loadAll() throws IOException {
        String errorLoading = null;
        File dir = new File(_fileSystem.getSavedDir());
        File[] files = dir.listFiles();
        if(files == null) files = new File[0];
        for(File file : files){
            if(!file.getName().endsWith(FileSystem.ext)) continue;

            try {
                String expression = readFile(file).trim();
                String name = file.getName();
                name = name.substring(0, name.indexOf(FileSystem.ext));
                _repo.addNewExpression(new ByteBeatExpression(name, expression, 0.5,50,50,50,false));
            }
            catch (IOException e) {
                errorLoading = file.getName();
            }
        }
        if(errorLoading != null) throw new ExpressionLoadingException(errorLoading);
    }

    private String readFile(File file) throws IOException {
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            char[] buf = new char[1024];
            int numRead;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                contents.append(readData);
            }
            return contents.toString();
        }
        finally {
            if(reader != null) reader.close();
        }
    }
}
