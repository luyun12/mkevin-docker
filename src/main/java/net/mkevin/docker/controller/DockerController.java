package net.mkevin.docker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * @author Kevin.Yin
 */
@RestController
@RequestMapping("/docker")
public class DockerController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello Docker4310!";
    }

 /*   @RequestMapping("/write")
    public String write() throws IOException {
        File file = new File("/data/tmp.log");
        file.createNewFile();
        return "write over!";
    }*/

}
