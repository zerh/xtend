package com.github.zerh.xtend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import j2html.tags.ContainerTag;

public abstract class Activity extends android.app.Activity {

    WebView webView;

    Object object;

    @SuppressLint("JavascriptInterface")
    protected void addJavascriptInterface(Object object) {

        this.object = object;
        /*runOnUiThread(new Runnable() {
            @SuppressLint("JavascriptInterface")
            @Override
            public void run() {
                webView.addJavascriptInterface(object, "$this");
            }
        });*/
        webView.addJavascriptInterface(object, "$this");
    }

    @JavascriptInterface
    public void run() {
        Log.d("pepe", "Me Rindo");
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(Activity.this);
        webView.loadDataWithBaseURL(null, render().render(), "text/HTML", "UTF-8", null);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Doc doc = new Doc(webView);
                doc.execute(ugly);
                onRendered(doc);
            }
        });
        addJavascriptInterface(this);
        setContentView(webView);
        addJavascriptInterface(this);


    }

    public abstract ContainerTag render();

    public abstract void onRendered(Doc webView);

    public static class Doc {

        private WebView webView;
        private StringBuilder stringBuilder;
        private Object objectToBind;


        public void bind(Object objectToBind) {
            this.objectToBind = objectToBind;
        }

        Object getObjectToBind() {
            return objectToBind;
        }


        Doc(WebView webView) {
            this.webView = webView;

        }

        public Doc $(String el) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("$('" + el + "')");
            return this;
        }

        public Doc css(String prop, String value) {
            if (stringBuilder != null)
                stringBuilder.append(".css('" + prop + "','" + value + "')");
            return this;
        }

        public Doc event(String event, String js) {
            if (stringBuilder != null)
                stringBuilder.append("");
            return this;
        }

        @SuppressLint("ObsoleteSdkInt")

        public Doc execute() {
            execute(null);
            return this;
        }

        @SuppressLint("ObsoleteSdkInt")
        public Doc execute(String javascript) {

            String script = javascript != null ? javascript : stringBuilder.toString();
            stringBuilder = new StringBuilder();

            Log.d("pepe", stringBuilder.toString());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(script, null);
            } else {
                webView.loadUrl("javascript:" + script);
            }

            return this;
        }
    }

    private static final String ugly = "// Sprint v0.9.2 - sprintjs.com/license\n" +
            "var Sprint;\n" +
            "(function(){var D=function(a,b){for(var c=Sprint(b),d=Object.keys(a),e=d.length,f=0;f<e;f++)for(var g=d[f],h=a[g],k=h.length,l=0;l<k;l++)c.on(g,h[l])},w=function(){var a=\"animation-iteration-count column-count flex-grow flex-shrink font-weight line-height opacity order orphans widows z-index\".split(\" \");return function(b,c){if(v(b,a))return c;var d=\"string\"==typeof c?c:c.toString();c&&!/\\D/.test(d)&&(d+=\"px\");return d}}(),K={afterbegin:function(a){this.insertBefore(a,this.firstChild)},afterend:function(a){var b=\n" +
            "this.parentElement;b&&b.insertBefore(a,this.nextSibling)},beforebegin:function(a){var b=this.parentElement;b&&b.insertBefore(a,this)},beforeend:function(a){this.appendChild(a)}},E=function(a,b){if(!(1<a.nodeType)){var c=a.sprintEventListeners;c&&D(c,b);for(var d=r(\"*\",a),e=d.length,f,g=0;g<e;g++)if(c=d[g].sprintEventListeners)f||(f=r(\"*\",b)),D(c,f[g])}},z=function(a,b,c,d,e){var f=[],g=this;this.each(function(){for(var h=a?this.parentElement:this;h&&(!e||e!=h);){if(!d||g.is(d,h))if(f.push(h),c)break;\n" +
            "if(b)break;h=h.parentElement}});return Sprint(x(f))},F=function(a,b){return Object.keys(a.sprintEventListeners).filter(function(a){return q(b).every(function(b){return v(b,q(a))})})},G=function(a,b,c){if(null==c){var d=a.get(0);if(!d||1<d.nodeType)return;a=b[0].toUpperCase()+b.substring(1);return d==document?(d=m[\"offset\"+a],a=window[\"inner\"+a],d>a?d:a):d==window?window[\"inner\"+a]:d.getBoundingClientRect()[b]}var e=\"function\"==typeof c,f=e?\"\":w(b,c);return a.each(function(a){this==document||this==\n" +
            "window||1<this.nodeType||(e&&(f=w(b,c.call(this,a,Sprint(this)[b]()))),this.style[b]=f)})},p=function(a,b){var c=b.length,d=b;if(1<c&&-1<a.indexOf(\"after\"))for(var d=[],e=c;e--;)d.push(b[e]);for(e=0;e<c;e++){var f=d[e];if(\"string\"==typeof f||\"number\"==typeof f)this.each(function(){this.insertAdjacentHTML(a,f)});else if(\"function\"==typeof f)this.each(function(b){b=f.call(this,b,this.innerHTML);p.call(Sprint(this),a,[b])});else{var g=f instanceof n,h=[],k=g?f.get():Array.isArray(f)?A(f,!0,!0):f.nodeType?\n" +
            "[f]:t(f),l=k.length;this.each(function(b){for(var c=document.createDocumentFragment(),d=0;d<l;d++){var e=k[d],f;b?(f=e.cloneNode(!0),E(e,f)):f=e;c.appendChild(f);h.push(f)}K[a].call(this,c)});g&&(f.dom=h,f.length=h.length);if(!(e<c-1))return h}}},v=function(a,b){for(var c=b.length;c--;)if(b[c]===a)return!0;return!1},B=function(a,b,c){if(null==b)return\"add\"==a?this:this.removeAttr(\"class\");var d,e,f;\"string\"==typeof b&&(d=!0,e=b.trim().split(\" \"),f=e.length);return this.each(function(g,h){if(!(1<this.nodeType)){if(!d){var k=\n" +
            "b.call(h,g,h.className);if(!k)return;e=k.trim().split(\" \");f=e.length}for(k=0;k<f;k++){var l=e[k];l&&(null==c?h.classList[a](l):h.classList.toggle(l,c))}}})},L=function(){for(var a=[\"mozMatchesSelector\",\"webkitMatchesSelector\",\"msMatchesSelector\",\"matches\"],b=a.length;b--;){var c=a[b];if(Element.prototype[c])return c}}(),x=function(a){for(var b=[],c=0,d=a.length,e=0;e<d;e++){for(var f=a[e],g=!1,h=0;h<c;h++)if(f===b[h]){g=!0;break}g||(b[c++]=f)}return b},H=function(){var a=function(a,b,c){return 2>\n" +
            "Object.keys(a.sprintEventListeners).filter(function(a){return q(b)[0]===q(a)[0]}).map(function(b){return a.sprintEventListeners[b]}).reduce(function(a,b){return a.concat(b)}).filter(function(a){return a===c}).length?!1:!0},b=function(b,c,f){return function(g){f&&f!==g||(b.removeEventListener(c,g),/\\./.test(c)&&!a(b,c,g)&&b.removeEventListener(q(c)[0],g))}},c=function(a,b){return a.filter(function(a){return b&&b!==a})};return function(a,e){return function(f){a.sprintEventListeners[f].forEach(b(a,f,\n" +
            "e));a.sprintEventListeners[f]=c(a.sprintEventListeners[f],e)}}}(),M=function(a,b){return function(c){F(a,c).forEach(H(a,b))}},m=document.documentElement,A=function(a,b,c){for(var d=a.length,e=d;e--;)if(!a[e]&&0!==a[e]||b&&a[e]instanceof n||c&&(\"string\"==typeof a[e]||\"number\"==typeof a[e])){for(var e=[],f=0;f<d;f++){var g=a[f];if(g||0===g)if(b&&g instanceof n)for(var h=0;h<g.length;h++)e.push(g.get(h));else!c||\"string\"!=typeof g&&\"number\"!=typeof g?e.push(g):e.push(document.createTextNode(g))}return e}return a},\n" +
            "I=function(){var a;return function(b,c,d){if(!a){var e=m.scrollTop;m.scrollTop=e+1;var f=m.scrollTop;m.scrollTop=e;a=f>e?m:document.body}if(null==d){b=b.get(0);if(!b)return;if(b==window||b==document)b=a;return b[c]}return b.each(function(){var b=this;if(b==window||b==document)b=a;b[c]=d})}}(),y=function(a,b,c,d){var e=[],f=b+\"ElementSibling\";a.each(function(){for(var b=this;(b=b[f])&&(!d||!a.is(d,b));)c&&!a.is(c,b)||e.push(b)});return Sprint(x(e))},J=function(a,b,c){var d=b+\"ElementSibling\";return a.map(function(){var b=\n" +
            "this[d];if(b&&(!c||a.is(c,b)))return b},!1)},r=function(a,b){b=b||document;if(/^[\\#.]?[\\w-]+$/.test(a)){var c=a[0];return\".\"==c?t(b.getElementsByClassName(a.slice(1))):\"#\"==c?(c=b.getElementById(a.slice(1)))?[c]:[]:\"body\"==a?[document.body]:t(b.getElementsByTagName(a))}return t(b.querySelectorAll(a))},q=function(a){return A(a.split(\".\"))},t=function(a){for(var b=[],c=a.length;c--;)b[c]=a[c];return b},C=function(){var a=function(a,c){var d=Sprint(a).clone(!0).get(0),e=d;if(d&&!(1<this.nodeType)){for(;e.firstChild;)e=\n" +
            "e.firstChild;if(\"inner\"==c){for(;this.firstChild;)e.appendChild(this.firstChild);this.appendChild(d)}else{var f=\"all\"==c?this.get(0):this,g=f.parentNode,h=f.nextSibling;\"all\"==c?this.each(function(){e.appendChild(this)}):e.appendChild(f);g.insertBefore(d,h)}}};return function(b,c){\"function\"==typeof b?this.each(function(a){Sprint(this)[\"inner\"==c?\"wrapInner\":\"wrap\"](b.call(this,a))}):\"all\"==c?a.call(this,b,c):this.each(function(){a.call(this,b,c)});return this}}(),u={legend:{intro:\"<fieldset>\",outro:\"</fieldset>\"},\n" +
            "area:{intro:\"<map>\",outro:\"</map>\"},param:{intro:\"<object>\",outro:\"</object>\"},thead:{intro:\"<table>\",outro:\"</table>\"},tr:{intro:\"<table><tbody>\",outro:\"</tbody></table>\"},col:{intro:\"<table><tbody></tbody><colgroup>\",outro:\"</colgroup></table>\"},td:{intro:\"<table><tbody><tr>\",outro:\"</tr></tbody></table>\"}};[\"tbody\",\"tfoot\",\"colgroup\",\"caption\"].forEach(function(a){u[a]=u.thead});u.th=u.td;var n=function(a,b){if(\"string\"==typeof a)if(\"<\"==a[0]){var c=document.createElement(\"div\"),d=/[\\w:-]+/.exec(a)[0],\n" +
            "d=u[d],e=a.trim();d&&(e=d.intro+e+d.outro);c.insertAdjacentHTML(\"afterbegin\",e);e=c.lastChild;if(d)for(d=d.outro.match(/</g).length;d--;)e=e.lastChild;c.textContent=\"\";this.dom=[e]}else this.dom=b&&b instanceof n?b.find(a).get():r(a,b);else if(Array.isArray(a))this.dom=A(a);else if(a instanceof NodeList||a instanceof HTMLCollection)this.dom=t(a);else{if(a instanceof n)return a;if(\"function\"==typeof a)return this.ready(a);this.dom=a?[a]:[]}this.length=this.dom.length};n.prototype={add:function(a){var b=\n" +
            "this.get();a=Sprint(a);for(var c=a.get(),d=0;d<a.length;d++)b.push(c[d]);return Sprint(x(b))},addClass:function(a){return B.call(this,\"add\",a)},after:function(){p.call(this,\"afterend\",arguments);return this},append:function(){p.call(this,\"beforeend\",arguments);return this},appendTo:function(a){return Sprint(p.call(Sprint(a),\"beforeend\",[this]))},attr:function(a,b){var c=\"function\"==typeof b;if(\"string\"==typeof b||\"number\"==typeof b||c)return this.each(function(d){1<this.nodeType||this.setAttribute(a,\n" +
            "c?b.call(this,d,this.getAttribute(a)):b)});if(\"object\"==typeof a){var d=Object.keys(a),e=d.length;return this.each(function(){if(!(1<this.nodeType))for(var b=0;b<e;b++){var c=d[b];this.setAttribute(c,a[c])}})}var f=this.get(0);if(f&&!(1<f.nodeType))return f=f.getAttribute(a),null==f?void 0:f?f:a},before:function(){p.call(this,\"beforebegin\",arguments);return this},children:function(a){var b=[],c=this;this.each(function(){if(!(1<this.nodeType))for(var d=this.children,e=d.length,f=0;f<e;f++){var g=d[f];\n" +
            "a&&!c.is(a,g)||b.push(g)}});return Sprint(b)},clone:function(a){return this.map(function(){if(this){var b=this.cloneNode(!0);a&&E(this,b);return b}},!1)},closest:function(a,b){return z.call(this,!1,!1,!0,a,b)},css:function(a,b){var c=typeof b,d=\"string\"==c;if(d||\"number\"==c){var e=d&&/=/.test(b);if(e)var f=parseInt(b[0]+b.slice(2));return this.each(function(){if(!(1<this.nodeType)){if(e)var c=parseInt(getComputedStyle(this).getPropertyValue(a))+f;this.style[a]=w(a,e?c:b)}})}if(\"function\"==c)return this.each(function(c){if(!(1<\n" +
            "this.nodeType)){var d=getComputedStyle(this).getPropertyValue(a);this.style[a]=b.call(this,c,d)}});if(\"string\"==typeof a)return d=this.get(0),!d||1<d.nodeType?void 0:getComputedStyle(d).getPropertyValue(a);if(Array.isArray(a)){d=this.get(0);if(!d||1<d.nodeType)return;for(var c={},d=getComputedStyle(d),g=a.length,h=0;h<g;h++){var k=a[h];c[k]=d.getPropertyValue(k)}return c}var l=Object.keys(a),m=l.length;return this.each(function(){if(!(1<this.nodeType))for(var b=0;b<m;b++){var c=l[b];this.style[c]=\n" +
            "w(c,a[c])}})},detach:function(){return this.map(function(){var a=this.parentElement;if(a)return a.removeChild(this),this},!1)},each:function(a){for(var b=this.dom,c=this.length,d=0;d<c;d++){var e=b[d];a.call(e,d,e)}return this},empty:function(){return this.each(function(){this.innerHTML=\"\"})},eq:function(a){return Sprint(this.get(a))},filter:function(a){var b=\"function\"==typeof a,c=this;return this.map(function(d){if(!(1<this.nodeType||!b&&!c.is(a,this)||b&&!a.call(this,d,this)))return this},!1)},\n" +
            "find:function(a){if(\"string\"==typeof a){var b=[];this.each(function(){if(!(1<this.nodeType))for(var c=r(a,this),d=c.length,e=0;e<d;e++)b.push(c[e])});return Sprint(x(b))}for(var c=a.nodeType?[a]:a.get(),d=c.length,e=[],f=0,g=0;g<this.length;g++){var h=this.get(g);if(!(1<h.nodeType))for(var k=0;k<d;k++){var l=c[k];if(h.contains(l)&&(e[f++]=l,!(f<d)))return Sprint(e)}}return Sprint(e)},first:function(){return this.eq(0)},get:function(a){if(null==a)return this.dom;0>a&&(a+=this.length);return this.dom[a]},\n" +
            "has:function(a){if(\"string\"==typeof a)return this.map(function(){if(!(1<this.nodeType)&&r(a,this)[0])return this},!1);for(var b=[],c=this.length;c--;){var d=this.get(c);if(d.contains(a)){b.push(d);break}}return Sprint(b)},hasClass:function(a){for(var b=this.length;b--;){var c=this.get(b);if(1<c.nodeType)return;if(c.classList.contains(a))return!0}return!1},height:function(a){return G(this,\"height\",a)},html:function(a){if(null==a){var b=this.get(0);return b?b.innerHTML:void 0}return\"function\"==typeof a?\n" +
            "this.each(function(b){b=a.call(this,b,this.innerHTML);Sprint(this).html(b)}):this.each(function(){this.innerHTML=a})},index:function(a){if(this.length){var b;a?\"string\"==typeof a?(b=this.get(0),a=Sprint(a)):(b=a instanceof n?a.get(0):a,a=this):(b=this.get(0),a=this.first().parent().children());a=a.get();for(var c=a.length;c--;)if(a[c]==b)return c;return-1}},insertAfter:function(a){Sprint(a).after(this);return this},insertBefore:function(a){Sprint(a).before(this);return this},is:function(a,b){var c=\n" +
            "b?[b]:this.get(),d=c.length;if(\"string\"==typeof a){for(var e=0;e<d;e++){var f=c[e];if(!(1<f.nodeType)&&f[L](a))return!0}return!1}if(\"object\"==typeof a){for(var f=a instanceof n?a.get():a.length?a:[a],g=f.length,e=0;e<d;e++)for(var h=0;h<g;h++)if(c[e]===f[h])return!0;return!1}if(\"function\"==typeof a){for(e=0;e<d;e++)if(a.call(this,e,this))return!0;return!1}},last:function(){return this.eq(-1)},map:function(a,b){null==b&&(b=!0);for(var c=this.get(),d=this.length,e=[],f=0;f<d;f++){var g=c[f],g=a.call(g,\n" +
            "f,g);if(b&&Array.isArray(g))for(var h=g.length,k=0;k<h;k++)e.push(g[k]);else e.push(g)}return Sprint(e)},next:function(a){return J(this,\"next\",a)},nextAll:function(a){return y(this,\"next\",a)},nextUntil:function(a,b){return y(this,\"next\",b,a)},not:function(a){var b=\"function\"==typeof a,c=this;return this.map(function(d){if(b){if(a.call(this,d,this))return}else if(c.is(a,this))return;return this},!1)},off:function(a,b){if(\"object\"==typeof a)return Object.keys(a).forEach(function(b){this.off(b,a[b])},\n" +
            "this),this;a&&(a=a.trim().split(\" \"));return this.each(function(){this.sprintEventListeners&&(a?a.forEach(M(this,b)):Object.keys(this.sprintEventListeners).forEach(H(this)))})},offset:function(a){if(!a){var b=this.get(0);if(!b||1<b.nodeType)return;b=b.getBoundingClientRect();return{top:b.top,left:b.left}}if(\"object\"==typeof a)return this.each(function(){if(!(1<this.nodeType)){var b=Sprint(this);\"static\"==b.css(\"position\")?b.css(\"position\",\"relative\"):b.css({top:0,left:0});var d=b.offset();b.css({top:a.top-\n" +
            "d.top+\"px\",left:a.left-d.left+\"px\"})}});if(\"function\"==typeof a)return this.each(function(b){var d=Sprint(this);b=a.call(this,b,d.offset());d.offset(b)})},offsetParent:function(){var a=[];this.each(function(){if(!(1<this.nodeType)){for(var b=this;b!=m;){var b=b.parentNode,c=getComputedStyle(b).getPropertyValue(\"position\");if(!c)break;if(\"static\"!=c){a.push(b);return}}a.push(m)}});return Sprint(a)},on:function(a,b){if(b){var c=a.trim().split(\" \");return this.each(function(){this.sprintEventListeners||\n" +
            "(this.sprintEventListeners={});c.forEach(function(a){this.sprintEventListeners[a]||(this.sprintEventListeners[a]=[]);this.sprintEventListeners[a].push(b);this.addEventListener(a,b);/\\./.test(a)&&this.addEventListener(q(a)[0],b)},this)})}Object.keys(a).forEach(function(b){this.on(b,a[b])},this);return this},parent:function(a){return z.call(this,!0,!0,!1,a)},parents:function(a){return z.call(this,!0,!1,!1,a)},position:function(){var a=this.offset(),b=this.parent().offset();if(a)return{top:a.top-b.top,\n" +
            "left:a.left-b.left}},prop:function(a,b){if(\"object\"==typeof a){var c=Object.keys(a),d=c.length;return this.each(function(){for(var b=0;b<d;b++){var e=c[b];this[e]=a[e]}})}if(null==b){var e=this.get(0);return e?e[a]:void 0}var f=\"function\"==typeof b;return this.each(function(c){this[a]=f?b.call(this,c,this[a]):b})},prepend:function(){p.call(this,\"afterbegin\",arguments);return this},prependTo:function(a){return Sprint(p.call(Sprint(a),\"afterbegin\",[this]))},prev:function(a){return J(this,\"previous\",\n" +
            "a)},prevAll:function(a){return y(this,\"previous\",a)},prevUntil:function(a,b){return y(this,\"previous\",b,a)},ready:function(a){this.dom=[document];this.length=1;return this.on(\"DOMContentLoaded\",a)},remove:function(a){var b=this;return this.each(function(){var c=this.parentElement;c&&(a&&!b.is(a,this)||c.removeChild(this))})},removeAttr:function(a){if(a){var b=a.trim().split(\" \"),c=b.length;this.each(function(){if(!(1<this.nodeType))for(var a=0;a<c;a++)this.removeAttribute(b[a])})}return this},removeClass:function(a){return B.call(this,\n" +
            "\"remove\",a)},removeProp:function(a){return this.each(function(){this[a]=void 0})},replaceAll:function(a){Sprint(a).replaceWith(this);return this},replaceWith:function(a){return\"function\"==typeof a?this.each(function(b){Sprint(this).replaceWith(a.call(this,b,this))}):this.before(a).remove()},scrollLeft:function(a){return I(this,\"scrollLeft\",a)},scrollTop:function(a){return I(this,\"scrollTop\",a)},siblings:function(a){var b=[],c=this;this.each(function(d,e){Sprint(this).parent().children().each(function(){this==\n" +
            "e||a&&!c.is(a,this)||b.push(this)})});return Sprint(b)},size:function(){return this.length},slice:function(a,b){var c=this.get(),d=[],e=0<=a?a:a+this.length,f=this.length;for(0>b?f+=b:0<=b&&(f=b>this.length?this.length:b);e<f;e++)d.push(c[e]);return Sprint(d)},text:function(a){if(null==a){var b=[];this.each(function(){b.push(this.textContent)});return b.join(\"\")}var c=\"function\"==typeof a;return this.each(function(b){this.textContent=c?a.call(this,b,this.textContent):a})},toggleClass:function(a,b){return B.call(this,\n" +
            "\"toggle\",a,b)},trigger:function(a){if(!window.CustomEvent||\"function\"!==typeof window.CustomEvent){var b=function(a,b){var e;b=b||{bubbles:!1,cancelable:!1,detail:void 0};e=document.createEvent(\"CustomEvent\");e.initCustomEvent(a,b.bubbles,b.cancelable,b.detail);return e};b.prototype=window.Event.prototype;window.CustomEvent=b}return this.each(function(){F(this,a).forEach(function(a){this.dispatchEvent(new b(a,{bubbles:!0,cancelable:!0}))},this)})},unwrap:function(){this.parent().each(function(){this!=\n" +
            "document.body&&this!=m&&Sprint(this).replaceWith(this.childNodes)});return this},val:function(a){if(null==a){var b=this.get(0);if(!b)return;if(b.multiple){var c=[];this.first().children(\":checked\").each(function(){c.push(this.value)});return c}return b.value}if(Array.isArray(a)){var d=this;return this.each(function(){this.multiple?d.children().each(function(){this.selected=v(this.value,a)}):this.checked=v(this.value,a)})}return\"function\"==typeof a?this.each(function(b){Sprint(this).val(a.call(this,\n" +
            "b,this.value))}):this.each(function(){this.value=a})},width:function(a){return G(this,\"width\",a)},wrap:function(a){return C.call(this,a)},wrapAll:function(a){return C.call(this,a,\"all\")},wrapInner:function(a){return C.call(this,a,\"inner\")}};Sprint=function(a,b){return new n(a,b)};null==window.$&&(window.$=Sprint)})();\n";

}
