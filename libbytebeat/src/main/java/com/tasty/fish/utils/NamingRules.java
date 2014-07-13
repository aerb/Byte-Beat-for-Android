package com.tasty.fish.utils;

import java.util.regex.Pattern;

public class NamingRules {
    private static Pattern validNames = Pattern.compile("[a-zA-Z0-9_]+");

    public static boolean check(String name) {
        return validNames.matcher(name).matches();
    }
}
