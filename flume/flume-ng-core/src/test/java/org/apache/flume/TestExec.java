package org.apache.flume;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/25
 */
public class TestExec {

    private static final Logger logger = LoggerFactory.getLogger(TestExec.class);

    @Test
    public void exec() {
        String command = "tail -f /Users/apple/exec.log";
        BufferedReader reader = null;
        try {
            String[] commandArgs = command.split("\\s+");
            Process process = new ProcessBuilder(commandArgs).start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            logger.error("Failed while running command:{} - Exception follows.", command, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Failed to close reader for exec source", e);
                }
            }
        }
    }
}
