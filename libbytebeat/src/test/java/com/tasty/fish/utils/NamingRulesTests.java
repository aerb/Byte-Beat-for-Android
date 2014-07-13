package com.tasty.fish.utils;

import org.junit.Assert;
import org.junit.Test;
public class NamingRulesTests {

    private void assertValid(String name){
        Assert.assertTrue(NamingRules.check(name));
    }

    private void assertInvalid(String name){
        Assert.assertFalse(NamingRules.check(name));
    }

    @Test
    public void checkValidNames() {
        assertValid("helloworld");
        assertValid("hello_world");
        assertValid("hello_world_0");
    }

    @Test
    public void checkInvalidNames() {
        assertInvalid("");
        assertInvalid(" ");
        assertInvalid("dashes-could-be-ok-but-lets-keep-it-simple");
        assertInvalid("nope<");
        assertInvalid("no.dots");
    }

}
