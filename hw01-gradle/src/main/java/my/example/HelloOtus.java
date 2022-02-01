package my.example;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {
    public static void main(String... args) {
        List<String> words = Lists.newArrayList("Hello", "World", "Gradle");
        String joinedWords = Joiner.on(",").join(words);
        System.out.println(joinedWords);
    }
}
