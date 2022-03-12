package lab_3;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GPL {

    public static void main(String[] args) throws IOException {

        String path = "src/main/java/lab_3/input.txt";
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        List<Token> tokens = new Lexer(new String(bytes, Charset.defaultCharset())).readTokens();
        System.out.println(tokens);
    }
}
