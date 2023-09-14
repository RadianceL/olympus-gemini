package cn.fuxi.monocase;

import com.olympus.gemini.invoke.annotation.GeminiIdempotent;

/**
 * @author eddie.lys
 * @since 2023/8/30
 */
public class DemoService {

   @GeminiIdempotent(duration = 5L)
    public String test(String s1, String s2) {
        System.out.printf("run test method -> s1 = %s, s2 = %s, result = %s", s1, s2, s1 + s2);
        System.out.println();
        return s1 + s2;
    }
}
