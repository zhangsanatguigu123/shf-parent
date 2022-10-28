var util = {
    getQueryVariable(variable) {
       // http://139.198.152.148:8002/info.html?id=1&name=tom&
        //window.location.search
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
            if(pair[0] == variable){return pair[1];}
        }
        return(false);
    },

    getOriginUrl() {
        var query = window.location.search.substring(1);
        if(query.indexOf("originUrl") != -1) {
            //"login.html?originUrl="+window.location.href
            var vars = query.split("originUrl=");
            return vars[1];
        }
        return ""
    }
}