    package com.example.Evenementiel.Controller;


    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/test")
    public class ControllerApp {
        @GetMapping("/string")
        public String getString() {
            return "BONJOUR À TOUS JE SUIS UN TEST";
        }
    }
