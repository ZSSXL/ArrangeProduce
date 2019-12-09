$("#load-arrange").click(function () {
    localStorage.setItem("arrangeCurrentPage", "arrange");
    $("#load-area").load("/arrange.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-error-prof").click(function () {
    localStorage.setItem("arrangeCurrentPage", "errorProf");
    $("#load-area").load("/errorProf.html");
    $(this).attr("class", "active");
    $("#load-arrange").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-history").click(function () {
    localStorage.setItem("arrangeCurrentPage", "history");
    $("#load-area").load("/history.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-setting").click(function () {
    localStorage.setItem("arrangeCurrentPage", "settings");
    $("#load-area").load("/settings.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-employee").click(function () {
    localStorage.setItem("arrangeCurrentPage", "employee");
    $("#load-area").load("/employee.html");
    $(this).attr("class", "active");
    $("#load-error-prof").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-history").removeAttr("class");
    $("#load-setting").removeAttr("class");
});

function currentPage(){
    let arrangeCurrentPage = localStorage.getItem("arrangeCurrentPage");
    if (arrangeCurrentPage === null) {
        $("#load-area").load("/arrange.html");
        $("#load-arrange").attr("class", "active");
    } else {
        if (arrangeCurrentPage === "errorProf") {
            $("#load-error-prof").attr("class", "active");
        } else {
            $("#load-" + arrangeCurrentPage).attr("class", "active");
        }
        $("#load-area").load("\\" + arrangeCurrentPage + ".html");
    }
}
