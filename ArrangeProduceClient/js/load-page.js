$("#load-arrange").click(function () {
    $("#load-area").load("/arrange.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-error-prof").click(function () {
    $("#load-area").load("/errorProf.html");
    $(this).attr("class", "active");
    $("#load-arrange").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-history").click(function () {
    $("#load-area").load("/history.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-setting").click(function () {
    $("#load-area").load("/settings.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-employee").click(function () {
    $("#load-area").load("/employee.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-setting").removeAttr("class");
});