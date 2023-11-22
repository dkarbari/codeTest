package com.opentext.bn.solutiondesigner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularRouterController {

@RequestMapping({ "/login", "/designer", "/logout", "/service-dev", "/service-dev/service-editor" })
   public String index() {
       return "forward:/index.html";
   }
}
