$("#load-arrange").click(function () {
    localStorage.setItem("arrangeCurrentPage", "arrange");
    $("#load-area").load("/arrange.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
    getAllGaugeSelected();
    getAllMachineSelected();
});

$("#load-aw").click(function () {
    localStorage.setItem("arrangeCurrentPage", "aw");
    $("#load-area").load("/aw.html");
    $(this).attr("class", "active");
    $("#load-arrange").removeAttr("class");
    $("#load-setting").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-setting").click(function () {
    localStorage.setItem("arrangeCurrentPage", "settings");
    $("#load-area").load("/settings.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-employee").removeAttr("class");
});

$("#load-employee").click(function () {
    localStorage.setItem("arrangeCurrentPage", "employee");
    $("#load-area").load("/employee.html");
    $(this).attr("class", "active");
    $("#load-aw").removeAttr("class");
    $("#load-arrange").removeAttr("class");
    $("#load-setting").removeAttr("class");
});

function currentPage() {
    let arrangeCurrentPage = localStorage.getItem("arrangeCurrentPage");
    if (arrangeCurrentPage === null) {
        $("#load-area").load("/arrange.html");
        getAllMachineSelected();
        getAllGaugeSelected();
        $("#load-arrange").attr("class", "active");
    } else {
        $("#load-" + arrangeCurrentPage).attr("class", "active");
        if (arrangeCurrentPage === "arrange") {
            getAllGaugeSelected();
            getAllMachineSelected();
        }
        $("#load-area").load("\\" + arrangeCurrentPage + ".html");
    }
}

function getAllGaugeSelected() {
    $.ajax({
        url: serverUrl + "/gauge",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                showGauge(result.data);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function getAllMachineSelected() {
    $.ajax({
        url: serverUrl + "/machine",
        contentType: "application/json; charset=utf-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("token", token);
        },
        type: "GET",
        success: function (result) {
            if (result.status === 0) {
                showMachine(result.data);
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
}

function showGauge(data) {
    $("#gauge").empty();
    $.each(data, function (index, item) {
        const option = $("<option></option>").append(item.gauge);
        option.attr("value", item.gauge);
        $("#gauge").append(option);
    })
}

function showMachine(data) {
    $("#machine").empty();
    $.each(data, function (index, item) {
        const option = $("<option></option>").append(item.machineName).attr("value", item.machineNumber);
        $("#machine").append(option);
    })
}
