package com.github.zerh.xtend;

import j2html.tags.ContainerTag;

public class Component {

    private ContainerTag containerTag;

    public static Component create(ContainerTag containerTag){
        return new Component(containerTag.render());
    }

    private Component(String html){

    }

    public void attr(String prop, String value){

    }


}
