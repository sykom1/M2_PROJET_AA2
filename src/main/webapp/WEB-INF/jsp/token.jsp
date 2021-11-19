${token}

<script>
    var cookievalue,
        cookieexpire,
        cookiepath,
        date;

    cookievalue ="${token}";
    date = new Date();
    date.setTime(date.getTime() + 3600000); // will last 3600 seconds (1 hour)
    cookieexpire = date.toGMTString();

    cookiepath = "/"; // accessible from every web page of the domain

    if("${token}" != null){
        document.cookie="access_token=" + cookievalue + "; expires=" + cookieexpire + "; path=" + cookiepath;

    }else{
        document.cookie="access_token=" + ""
    }
    window.location.href = "/app";
</script>